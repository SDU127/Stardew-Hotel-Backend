package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.DTO.ParkingSpotDTO;
import com.sdu127.Data.DTO.PaymentDTO;
import com.sdu127.Data.PO.ParkingSpot;
import com.sdu127.Data.PO.Payment;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.ParkingSpotMapper;
import com.sdu127.Mapper.PaymentMapper;
import com.sdu127.exception.Exceptions.InfoMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ParkService {
    @Resource
    UserServiceIns userServiceIns;
    @Resource
    ParkingSpotMapper parkingSpotMapper;
    @Resource
    PaymentMapper paymentMapper;

    /**
     * 获取我的车位信息
     */
    public Result getMyParkingSpots(Integer current, Integer size) {
        return getParkingSpots("", -1, -1, CurrentUser.getId(), current, size);
    }

    /**
     * 显示车位信息
     */
    public Result showParkingSpots(String area, Integer position, Integer isRented, Integer current, Integer size) {
        return getParkingSpots(area, position, isRented, null, current, size);
    }

    /**
     * 获取车位信息的通用方法
     */
    private Result getParkingSpots(String area, Integer position, Integer isRented, Integer userId, Integer current, Integer size) {
        Page<ParkingSpot> page = new Page<>(current, size);

        LambdaQueryWrapper<ParkingSpot> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(ParkingSpot::getArea, area);

        if (position != -1) {
            queryWrapper.eq(ParkingSpot::getPosition, position);
        }

        if (isRented != -1) {
            queryWrapper.eq(ParkingSpot::getIsRented, isRented);
        }

        if (userId != null) {
            queryWrapper.eq(ParkingSpot::getUserId, userId);
        }

        queryWrapper.orderByAsc(ParkingSpot::getArea, ParkingSpot::getPosition);

        IPage<ParkingSpotDTO> resultPage = parkingSpotMapper.searchParkingSpots(page, queryWrapper);

        return Result.success(resultPage);
    }

    /**
     * 租用停车位
     */
    @Transactional
    public Result rentParkingSpot(Integer parkingSpotId, String carNumber) {
        // 判断停车位是否可用
        ParkingSpot parkingSpot = parkingSpotMapper.selectById(parkingSpotId);
        if (parkingSpot == null || parkingSpot.getIsRented() != 0) throw new InfoMessage(ResponseData.PARKING_SPOT_NOT_AVAILABLE);

        parkingSpot.setIsRented(1);
        parkingSpot.setUserId(CurrentUser.getId());
        parkingSpot.setCarNumber(carNumber);
        parkingSpotMapper.updateById(parkingSpot);

        // 增加缴费信息
        paymentMapper.insert(Payment.builder()
                .type(2)
                .content("车位： " + parkingSpot.getArea() + parkingSpot.getPosition() + "\n" +
                        "费用： " + parkingSpot.getRentalFee() + "\n" +
                        "请及时支付")
                .userId(CurrentUser.getId())
                .fee(parkingSpot.getRentalFee())
                .payTo(parkingSpotId)
                .payTime(LocalDateTime.now())
                .deadline(LocalDateTime.now().plusMonths(1))
                .build());

        return Result.ok();
    }

    /**
     * 取消租用停车位
     */
    public Result cancelParkingSpot(Integer parkingSpotId) {
        // 判断停车位是否可用
        ParkingSpot parkingSpot = parkingSpotMapper.selectById(parkingSpotId);
        if (parkingSpot == null || !Objects.equals(parkingSpot.getUserId(), CurrentUser.getId())) throw new InfoMessage(ResponseData.PARKING_SPOT_NOT_AVAILABLE);

        LambdaUpdateWrapper<ParkingSpot> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(ParkingSpot::getId, parkingSpotId)
                .set(ParkingSpot::getIsRented, 0)
                .set(ParkingSpot::getUserId, null)
                .set(ParkingSpot::getCarNumber, null);

        parkingSpotMapper.update(null, updateWrapper);

        // 如果未缴费则撤回缴费
        PaymentDTO payment = paymentMapper.getUnpaidPayment(2, CurrentUser.getId(), parkingSpotId);
        if (payment != null) {
            paymentMapper.deleteById(payment);
        }

        return Result.ok();
    }





    /**
     * 添加车位
     */
    @Transactional
    public Result addParkingSpot(String area, Integer position, Double rentalFee) {
        ParkingSpot parkingSpot = parkingSpotMapper.getParkingSpotByPosition(area, position);
        if (parkingSpot != null) throw new InfoMessage(ResponseData.PARKING_SPOT_EXISTS);

        ParkingSpot newParkingSpot = ParkingSpot.builder()
                .area(area)
                .position(position)
                .rentalFee(rentalFee)
                .build();
        parkingSpotMapper.insert(newParkingSpot);
        return Result.ok();
    }

    /**
     * 删除车位
     *
     * @param ids 车位id
     */
    @Transactional
    public Result deleteParkingSpot(List<Integer> ids) {
        for (Integer id: ids) {
            parkingSpotMapper.deleteById(id);
        }
        return Result.ok();
    }

    /**
     * 修改车位状态
     */
    @Transactional
    public Result modifyParkingSpotStatus(ParkingSpot parkingSpot) {
        ParkingSpot oldParkingSpot = parkingSpotMapper.selectById(parkingSpot.getId());
        if (oldParkingSpot == null) throw new InfoMessage(ResponseData.PARKING_SPOT_NOT_AVAILABLE);

        ParkingSpot ps = parkingSpotMapper.getParkingSpotByPosition(parkingSpot.getArea(), parkingSpot.getPosition());
        if (ps != null && !Objects.equals(parkingSpot.getId(), ps.getId())) throw new InfoMessage(ResponseData.PARKING_SPOT_EXISTS);

        LambdaUpdateWrapper<ParkingSpot> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(ParkingSpot::getId, parkingSpot.getId());

        switch (parkingSpot.getIsRented()) {
            case 0 -> {
                updateWrapper.set(ParkingSpot::getUserId, null);
                updateWrapper.set(ParkingSpot::getCarNumber, null);
            }
            case 1 -> {
                if (parkingSpot.getUserId() == null || parkingSpot.getUserId() == 0 || parkingSpot.getCarNumber() == null || parkingSpot.getCarNumber().isEmpty())
                    throw new InfoMessage(ResponseData.PARAM_WRONG);

                if (userServiceIns.getUser(parkingSpot.getUserId()) == null)
                    throw new InfoMessage(ResponseData.USER_NOT_FOUND);

                updateWrapper
                        .set(ParkingSpot::getUserId, parkingSpot.getUserId())
                        .set(ParkingSpot::getCarNumber, parkingSpot.getCarNumber());
            }
            default -> throw new InfoMessage(ResponseData.PARAM_WRONG);
        }

        updateWrapper
                .set(ParkingSpot::getArea, parkingSpot.getArea())
                .set(ParkingSpot::getPosition, parkingSpot.getPosition())
                .set(ParkingSpot::getIsRented, parkingSpot.getIsRented())
                .set(ParkingSpot::getRentalFee, parkingSpot.getRentalFee());

        parkingSpotMapper.update(null, updateWrapper);

        return Result.ok();
    }

}

package com.sdu127.Controller;

import com.sdu127.Annotation.RequiredLogin;
import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.PO.ParkingSpot;
import com.sdu127.Data.VO.Result;
import com.sdu127.Service.ParkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/park")
public class ParkController {
    @Resource
    ParkService parkService;

    /**
     * 获取我的停车位信息
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @GetMapping("/getMyParkingSpots")
    public Result getMyParkingSpots(@RequestParam Integer current, @RequestParam Integer size) {
        return parkService.getMyParkingSpots(current, size);
    }

    /**
     * 显示车位信息
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @GetMapping("/showParkingSpots")
    public Result getParkingSpots(@RequestParam(required = false, defaultValue = "") String area,
                                  @RequestParam(required = false, defaultValue = "-1") Integer position,
                                  @RequestParam(required = false, defaultValue = "-1") Integer isRented,
                                  @RequestParam Integer current,
                                  @RequestParam Integer size) {
        return parkService.showParkingSpots(area, position, isRented, current, size);
    }

    /**
     * 租用停车位
     *
     * @param parkingSpotId 停车位id
     * @param carNumber 车牌号
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @PostMapping("/rentParkingSpot")
    public Result rentParkingSpot(@RequestParam Integer parkingSpotId, @RequestParam String carNumber) {
        return parkService.rentParkingSpot(parkingSpotId, carNumber);
    }

    /**
     * 取消租用
     *
     * @param parkingSpotId 车位id
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @PostMapping("/cancelParkingSpot")
    public Result cancelParkingSpot(@RequestParam Integer parkingSpotId) {
        return parkService.cancelParkingSpot(parkingSpotId);
    }




    /**
     * 添加车位
     *
     * @param area 区
     * @param position 位置
     * @param rentalFee 租用费
     */
    @RequiredLogin
    @PostMapping("/addParkingSpot")
    public Result addParkingSpot(@RequestParam String area, @RequestParam Integer position, @RequestParam Double rentalFee) {
        return parkService.addParkingSpot(area, position, rentalFee);
    }

    /**
     * 删除车位
     *
     * @param ids 车位id
     */
    @RequiredLogin
    @DeleteMapping("/deleteParkingSpot")
    public Result deleteParkingSpot(@RequestBody List<Integer> ids) {
        return parkService.deleteParkingSpot(ids);
    }

    /**
     * 修改车位状态
     *
     * @param parkingSpot 车位信息
     */
    @RequiredLogin
    @PatchMapping("/modifyParkingSpot")
    public Result modifyParkingSpotStatus(@RequestBody ParkingSpot parkingSpot) {
        return parkService.modifyParkingSpotStatus(parkingSpot);
    }
}

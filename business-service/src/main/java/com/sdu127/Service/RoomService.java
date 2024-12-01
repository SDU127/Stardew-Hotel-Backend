package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.DTO.RoomDTO;
import com.sdu127.Data.PO.Room;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.RoomMapper;
import com.sdu127.exception.Exceptions.InfoMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RoomService {
    @Resource
    RoomMapper roomMapper;

    /**
     * 获取我的房屋信息
     */
    public Result getMyRoom(Integer current, Integer size) {
        return getRooms(-1, -1, "", -1, CurrentUser.getId(), current, size);
    }

    /**
     * 显示房屋信息
     */
    public Result showRooms(Integer floor, Integer room, String type, Integer isUsed, Integer current, Integer size) {
        return getRooms(floor, room, type, isUsed, null, current, size);
    }

    /**
     * 获取房屋信息的通用方法
     */
    public Result getRooms(Integer floor, Integer number, String type, Integer isUsed, Integer userId, Integer current, Integer size) {
        Page<RoomDTO> page = new Page<>(current, size);

        QueryWrapper<RoomDTO> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("type", type);

        if (floor != -1) {
            queryWrapper.eq("floor", floor);
        }

        if (number != -1) {
            queryWrapper.eq("number", number);
        }

        if (isUsed != -1) {
            queryWrapper.eq("IF(room_rent.user_id IS NULL, 0, 1)", isUsed);
        }

        if (userId != null) {
            queryWrapper.eq("room_rent.user_id", userId);
        }

        queryWrapper.orderByAsc("floor", "number");

        IPage<RoomDTO> resultPage = roomMapper.searchRooms(page, queryWrapper);

        return Result.success(resultPage);
    }





    /**
     * 添加新房屋
     */
    @Transactional
    public Result addRoom(Room room) {
        if (room.checkNull()) throw new InfoMessage(ResponseData.PARAM_WRONG);
        room.modifyDiscount();

        Room oldRoom = roomMapper.getRoomByPosition(room.getFloor(), room.getNumber());
        if (oldRoom != null) throw new InfoMessage(ResponseData.ROOM_EXISTS);

        room.setId(null);

        roomMapper.insert(room);
        return Result.ok();
    }

    /**
     * 删除房屋
     */
    @Transactional
    public Result deleteRoom(List<Integer> roomIds) {
        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Room::getId, roomIds);
        roomMapper.markDeleted(true, updateWrapper);
        return Result.ok();
    }

    /**
     * 修改房屋信息
     */
    @Transactional
    public Result modifyRoom(Room room) {
        Room oldRoom = roomMapper.getById(room.getId());
        if (oldRoom == null) throw new InfoMessage(ResponseData.ROOM_NOT_AVAILABLE);

        // 校验并改正参数
        if (room.checkNull()) throw new InfoMessage(ResponseData.PARAM_WRONG);
        room.modifyDiscount();

        // 判断是否会冲突
        Room r = roomMapper.getRoomByPosition(room.getFloor(), room.getNumber());
        if (r != null && !Objects.equals(room.getId(), r.getId())) throw new InfoMessage(ResponseData.ROOM_EXISTS);

        // 更新条件
        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Room::getId, room.getId());
        updateWrapper
                .set(Room::getFloor, room.getFloor())
                .set(Room::getNumber, room.getNumber())
                .set(Room::getArea, room.getArea())
                .set(Room::getType, room.getType())
                .set(Room::getFee, room.getFee())
                .set(Room::getDiscount, room.getDiscount())
                .set(Room::getVipDiscount, room.getVipDiscount());

        roomMapper.update(null, updateWrapper);
        return Result.ok();
    }
}

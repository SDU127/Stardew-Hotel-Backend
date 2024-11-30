package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    UserServiceIns userServiceIns;
    @Resource
    RoomMapper roomMapper;

    /**
     * 获取我的房屋信息
     */
    public Result getMyHouse(Integer current, Integer size) {
        return getHouses(-1, -1, -1, "", -1, CurrentUser.getId(), current, size);
    }

    /**
     * 显示房屋信息
     */
    public Result showHouses(Integer building, Integer floor, Integer room, String type, Integer isUsed, Integer current, Integer size) {
        return getHouses(building, floor, room, type, isUsed, null, current, size);
    }

    /**
     * 获取房屋信息的通用方法
     */
    public Result getHouses(Integer building, Integer floor, Integer room, String type, Integer isUsed, Integer userId, Integer current, Integer size) {
        Page<Room> page = new Page<>(current, size);

        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(Room::getType, type);

        if (building != -1) {
            queryWrapper.eq(Room::getBuilding, building);
        }

        if (floor != -1) {
            queryWrapper.eq(Room::getFloor, floor);
        }

        if (room != -1) {
            queryWrapper.eq(Room::getRoom, room);
        }

        if (isUsed != -1) {
            queryWrapper.eq(Room::getIsUsed, isUsed);
        }

        if (userId != null) {
            queryWrapper.eq(Room::getUserId, userId);
        }

        queryWrapper.orderByAsc(Room::getBuilding, Room::getFloor, Room::getRoom);

        IPage<RoomDTO> resultPage = roomMapper.searchHouses(page, queryWrapper);

        return Result.success(resultPage);
    }





    /**
     * 添加新房屋
     */
    @Transactional
    public Result addHouse(Integer building, Integer floor, Integer room, Double area, String type, Double fee) {
        Room house = roomMapper.getHouseByPosition(building, floor, room);
        if (house != null) throw new InfoMessage(ResponseData.HOUSE_EXISTS);

        Room newRoom = Room.builder()
                .building(building)
                .floor(floor)
                .room(room)
                .area(area)
                .type(type)
                .fee(fee)
                .build();
        roomMapper.insert(newRoom);
        return Result.ok();
    }

    /**
     * 删除房屋
     */
    @Transactional
    public Result deleteHouse(List<Integer> houseIds) {
        for (Integer id: houseIds) {
            roomMapper.deleteById(id);
        }
        return Result.ok();
    }

    /**
     * 修改房屋信息
     */
    @Transactional
    public Result modifyHouse(Room room) {
        Room oldRoom = roomMapper.selectById(room.getId());
        if (oldRoom == null) throw new InfoMessage(ResponseData.HOUSE_NOT_AVAILABLE);

        Room h = roomMapper.getHouseByPosition(room.getBuilding(), room.getFloor(), room.getRoom());
        if (h != null && !Objects.equals(room.getId(), h.getId())) throw new InfoMessage(ResponseData.HOUSE_EXISTS);

        LambdaUpdateWrapper<Room> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(Room::getId, room.getId());

        switch (room.getIsUsed()) {
            case 0 -> updateWrapper.set(Room::getUserId, null);
            case 1 -> {
                if (room.getUserId() == null || room.getUserId() == -1)
                    throw new InfoMessage(ResponseData.PARAM_WRONG);

                if (userServiceIns.getUser(room.getUserId()) == null)
                    throw new InfoMessage(ResponseData.USER_NOT_FOUND);

                updateWrapper.set(Room::getUserId, room.getUserId());
            }
            default -> throw new InfoMessage(ResponseData.PARAM_WRONG);
        }

        updateWrapper
                .set(Room::getBuilding, room.getBuilding())
                .set(Room::getFloor, room.getFloor())
                .set(Room::getRoom, room.getRoom())
                .set(Room::getArea, room.getArea())
                .set(Room::getType, room.getType())
                .set(Room::getFee, room.getFee())
                .set(Room::getIsUsed, room.getIsUsed());

        roomMapper.update(null, updateWrapper);
        return Result.ok();
    }
}

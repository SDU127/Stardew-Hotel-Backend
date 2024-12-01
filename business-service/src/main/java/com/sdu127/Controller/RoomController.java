package com.sdu127.Controller;

import com.sdu127.Annotation.RequiredLogin;
import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.PO.Room;
import com.sdu127.Data.VO.Result;
import com.sdu127.Service.RoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Resource
    RoomService roomService;

    /**
     * 获取我的房屋信息
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @GetMapping("/getMyRoom")
    public Result getMyRoom(@RequestParam Integer current, @RequestParam Integer size) {
        return roomService.getMyRoom(current, size);
    }

    /**
     * 显示房屋信息
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @GetMapping("/showRooms")
    public Result showRooms(@RequestParam(required = false, defaultValue = "-1") Integer floor,
                             @RequestParam(required = false, defaultValue = "-1") Integer room,
                             @RequestParam(required = false, defaultValue = "") String type,
                             @RequestParam(required = false, defaultValue = "-1") Integer isUsed,
                             @RequestParam Integer current,
                             @RequestParam Integer size) {
        return roomService.showRooms(floor, room, type, isUsed, current, size);
    }





    /**
     * 添加新房屋
     *
     * @param room 房间信息
     */
    @RequiredLogin
    @PostMapping("/addRoom")
    public Result addRoom(@RequestBody Room room) {
        return roomService.addRoom(room);
    }

    /**
     * 删除房屋
     *
     * @param roomIds 房屋id
     */
    @RequiredLogin
    @DeleteMapping("/deleteRoom")
    public Result deleteRoom(@RequestParam List<Integer> roomIds) {
        return roomService.deleteRoom(roomIds);
    }

    /**
     * 修改房屋信息
     *
     */
    @RequiredLogin
    @PatchMapping("/modifyRoom")
    public Result modifyRoom(@RequestBody Room room) {
        return roomService.modifyRoom(room);
    }


}

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
    @GetMapping("/getMyHouse")
    public Result getMyHouse(@RequestParam Integer current, @RequestParam Integer size) {
        return roomService.getMyHouse(current, size);
    }

    /**
     * 显示房屋信息
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @GetMapping("/showHouses")
    public Result showHouses(@RequestParam(required = false, defaultValue = "-1") Integer building,
                             @RequestParam(required = false, defaultValue = "-1") Integer floor,
                             @RequestParam(required = false, defaultValue = "-1") Integer room,
                             @RequestParam(required = false, defaultValue = "") String type,
                             @RequestParam(required = false, defaultValue = "-1") Integer isUsed,
                             @RequestParam Integer current,
                             @RequestParam Integer size) {
        return roomService.showHouses(building, floor, room, type, isUsed, current, size);
    }





    /**
     * 添加新房屋
     *
     * @param building 栋
     * @param floor 楼层
     * @param room 房间号
     * @param area 面积
     * @param type 类型
     * @param fee 物业费
     */
    @RequiredLogin
    @PostMapping("/addHouse")
    public Result addHouse(@RequestParam Integer building, @RequestParam Integer floor, @RequestParam Integer room, @RequestParam Double area, @RequestParam String type, @RequestParam Double fee) {
        return roomService.addHouse(building, floor, room, area, type, fee);
    }

    /**
     * 删除房屋
     *
     * @param houseIds 房屋id
     */
    @RequiredLogin
    @DeleteMapping("/deleteHouse")
    public Result deleteHouse(@RequestParam List<Integer> houseIds) {
        return roomService.deleteHouse(houseIds);
    }

    /**
     * 修改房屋信息
     *
     */
    @RequiredLogin
    @PatchMapping("/modifyHouse")
    public Result modifyHouse(@RequestBody Room room) {
        return roomService.modifyHouse(room);
    }


}

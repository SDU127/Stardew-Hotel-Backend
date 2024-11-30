package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.DTO.RoomDTO;
import com.sdu127.Data.PO.Room;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 房屋Mapper
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    /**
     * 删除用户相关房屋
     *
     * @param userId 用户id
     */
    @Delete("DELETE FROM house WHERE user_id = #{userId}")
    void deleteByUserId(Integer userId);


    /**
     * 获取房屋信息
     *
     * @param userId 用户id
     */
    @Select("SELECT * FROM house WHERE user_id = #{userId}")
    List<RoomDTO> getHouses(Integer userId);

    /**
     * 获取房屋信息
     *
     * @param building 栋
     * @param floor 楼层
     * @param room 房间号
     */
    @Select("SELECT * FROM house WHERE building = #{building} AND floor = #{floor} AND room = #{room}")
    RoomDTO getHouseByPosition(Integer building, Integer floor, Integer room);

    /**
     * 获取全部房屋
     */
    @Select("SELECT house.*, CONCAT(user.last_name, user.first_name) AS fullName, user.phone " +
            "FROM house " +
            "LEFT JOIN user ON house.user_id = user.id " +
            "WHERE ${ew.sqlSegment}")
    IPage<RoomDTO> searchHouses(Page<Room> page, @Param("ew") Wrapper<Room> queryWrapper);

}

package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.DTO.RoomDTO;
import com.sdu127.Data.PO.Room;
import org.apache.ibatis.annotations.*;

/**
 * 房屋Mapper
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    /**
     * 标记删除/取消标记
     *
     * @param deleted 状态
     * @param queryWrapper 对象
     */
    @Update("UPDATE room SET deleted = #{deleted} " +
            "WHERE ${ew.sqlSegment}")
    void markDeleted(@Param("deleted") Boolean deleted, @Param("ew") Wrapper<Room> queryWrapper);


    /**
     * 通过id获取房间
     *
     * @param roomId 房间id
     */
    @Select("SELECT * FROM room WHERE id = #{roomId} AND deleted = 0")
    Room getById(Integer roomId);

    /**
     * 获取房间信息
     *
     * @param floor 楼层
     * @param number 房间号
     */
    @Select("SELECT * FROM room WHERE floor = #{floor} AND number = #{number} AND deleted = 0")
    RoomDTO getRoomByPosition(Integer floor, Integer number);

    /**
     * 获取全部房屋
     *
     * @param queryWrapper 查询条件
     */
    @Select("SELECT room.*, IF(user_id IS NULL, 0, 1) AS isUsed, user.id AS userId, CONCAT(user.last_name, user.first_name) AS fullName, user.phone, user.id_number " +
            "FROM room " +
            "LEFT JOIN room_rent ON room.id = room_rent.room_id AND NOW() BETWEEN start_time AND end_time " +
            "LEFT JOIN user ON room_rent.user_id = user.id " +
            "WHERE deleted = 0 AND ${ew.sqlSegment}")
    IPage<RoomDTO> searchRooms(Page<RoomDTO> page, @Param("ew") Wrapper<RoomDTO> queryWrapper);

}

package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.DTO.ParkingSpotDTO;
import com.sdu127.Data.PO.ParkingSpot;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 停车位Mapper
 */
@Mapper
public interface ParkingSpotMapper extends BaseMapper<ParkingSpot> {
    /**
     * 删除用户相关停车位
     *
     * @param userId 用户id
     */
    @Delete("DELETE FROM parking_spot WHERE user_id = #{userId}")
    void deleteByUserId(Integer userId);


    /**
     * 获取指定车位信息
     *
     * @param area 区
     * @param position 位置
     */
    @Select("SELECT * FROM parking_spot WHERE area = #{area} AND position = #{position}")
    ParkingSpotDTO getParkingSpotByPosition(String area, Integer position);

    /**
     * 获取用户的车位信息
     *
     * @param userId 用户id
     */
    @Select("SELECT *, parking_spot.id AS id " +
            "FROM parking_spot " +
            "JOIN user ON parking_spot.user_id = user.id " +
            "WHERE parking_spot.user_id = #{userId} AND user.id = #{userId}")
    List<ParkingSpotDTO> getParkingSpots(Integer userId);

    /**
     * 获取全部车位信息
     */
    @Select("SELECT * " +
            "FROM parking_spot " +
            "LEFT JOIN user ON parking_spot.user_id = user.id " +
            "WHERE ${ew.sqlSegment}")
    IPage<ParkingSpotDTO> searchParkingSpots(Page<ParkingSpot> page, @Param("ew") Wrapper<ParkingSpot> queryWrapper);
}

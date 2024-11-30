package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.DTO.ReportDTO;
import com.sdu127.Data.DTO.ReportDetailDTO;
import com.sdu127.Data.PO.Report;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
    @Delete("DELETE FROM report_image WHERE report_id IN (SELECT id FROM report WHERE user_id = #{userId})")
    void deleteReportImagesByUserId(Integer userId);

    @Delete("DELETE FROM report WHERE user_id = #{userId}")
    void deleteReportByUserId(Integer userId);


    /**
     * 记录投诉图片
     *
     * @param reportId 投诉id
     * @param image 投诉图片
     */
    @Insert("INSERT INTO report_image (report_id, image) VALUES (#{reportId}, #{image})")
    void insertReportImage(Integer reportId, String image);

    /**
     * 获取所有投诉列表
     */
    @Select("SELECT report.*, CONCAT(u1.last_name, u1.first_name) AS fullName, u1.phone, CONCAT(u2.last_name, u2.first_name) AS dealerFullName, u2.phone AS dealerPhone " +
            "FROM report " +
            "LEFT JOIN user u1 ON report.user_id = u1.id " +
            "LEFT JOIN user u2 ON report.last_dealer = u2.id " +
            "WHERE ${ew.sqlSegment}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "images", column = "id", many = @Many(select = "getReportImages")),
            @Result(property = "details", column = "id", many = @Many(select = "getDetails"))
    })
    IPage<ReportDTO> searchReportList(Page<Report> page, @Param("ew") Wrapper<Report> queryWrapper);

    /**
     * 获取投诉列表
     *
     * @param userId 用户id
     */
    @Select("SELECT * FROM report WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "images", column = "id", many = @Many(select = "getReportImages"))
    })
    List<ReportDTO> getMyReportList(Integer userId);

    /**
     * 获取投诉详情
     *
     * @param reportId 投诉id
     */
    @Select("SELECT report_detail.*, CONCAT(user.last_name, user.first_name) AS dealerFullName, user.phone AS dealerPhone " +
            "FROM report_detail " +
            "LEFT JOIN user ON report_detail.dealer = user.id " +
            "WHERE report_id = #{reportId} " +
            "ORDER BY deal_time IS NULL")
    List<ReportDetailDTO> getDetails(Integer reportId);

    /**
     * 获取投诉图片
     *
     * @param reportId 投诉id
     */
    @Select("SELECT image FROM report_image WHERE report_id = #{reportId}")
    List<String> getReportImages(Integer reportId);

    /**
     * 删除投诉图片
     *
     * @param reportId 投诉id
     */
    @Delete("DELETE FROM report_image WHERE report_id = #{reportId}")
    void deleteReportImagesByReportId(Integer reportId);
}

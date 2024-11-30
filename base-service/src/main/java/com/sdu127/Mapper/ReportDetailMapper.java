package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sdu127.Data.PO.ReportDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportDetailMapper extends BaseMapper<ReportDetail> {
    /**
     * 删除相关投诉处理
     *
     * @param reportId 投诉id
     */
    @Delete("DELETE FROM report_detail WHERE report_id = #{reportId}")
    void deleteByReportId(Integer reportId);
}

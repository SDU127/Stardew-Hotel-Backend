package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.DTO.NoticeDTO;
import com.sdu127.Data.PO.Notice;
import org.apache.ibatis.annotations.*;

/**
 * 通知Mapper
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    /**
     * 获取通知列表
     */
    @Select("SELECT notice.*, IF(notice_read.user_id IS NULL, 0, 1) AS isRead " +
            "FROM notice " +
            "LEFT JOIN notice_read ON notice.id = notice_read.notice_id " +
            "WHERE ${ew.sqlSegment}")
    IPage<NoticeDTO> searchNoticeList(Page<Notice> page, @Param("ew") Wrapper<Notice> queryWrapper);

    /**
     * 获取通知列表
     */
    @Select("SELECT notice.*, IF(notice_read.user_id IS NULL, 0, 1) AS isRead " +
            "FROM notice " +
            "LEFT JOIN notice_read ON notice.id = notice_read.notice_id " +
            "WHERE IF(notice_read.user_id IS NULL, 0, 1) = #{isRead} AND ${ew.sqlSegment}")
    IPage<NoticeDTO> searchReadNoticeList(Page<Notice> page, @Param("ew") Wrapper<Notice> queryWrapper, @Param("isRead") Integer isRead);

    /**
     * 查询单个通知
     */
    @Select("SELECT notice.*, IF(notice_read.user_id IS NULL , 0, 1) AS isRead " +
            "FROM notice " +
            "LEFT JOIN notice_read ON notice.id = notice_read.notice_id " +
            "WHERE notice.id = #{noticeId} ")
    NoticeDTO getNotice(Integer noticeId);

    /**
     * 插入读记录
     */
    @Insert("INSERT INTO notice_read (user_id, notice_id) VALUES (#{userId}, #{noticeId})")
    void addRead(Integer noticeId, Integer userId);

    /**
     * 删除通知读记录
     *
     * @param noticeId 通知id
     */
    @Delete("DELETE FROM notice_read WHERE notice_id = #{noticeId}")
    void deleteReadById(Integer noticeId);

    /**
     * 删除用户读记录
     *
     * @param userId 用户id
     */
    @Delete("DELETE FROM notice_read WHERE user_id = #{userId}")
    void deleteReadByUserId(Integer userId);
}

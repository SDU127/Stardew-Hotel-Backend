package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.DTO.ReportDTO;
import com.sdu127.Data.DTO.ReportDetailDTO;
import com.sdu127.Data.DTO.ReportParams;
import com.sdu127.Data.PO.Report;
import com.sdu127.Data.PO.ReportDetail;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.ReportDetailMapper;
import com.sdu127.Mapper.ReportMapper;
import com.sdu127.Util.FileUtil;
import com.sdu127.exception.Exceptions.InfoMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService {
    @Resource
    ReportMapper reportMapper;
    @Resource
    ReportDetailMapper reportDetailMapper;
    @Resource
    UserServiceIns userServiceIns;
    @Resource
    FileUtil fileUtil;

    /**
     * 投诉
     */
    @Transactional
    public Result report(ReportParams reportParams) {
        Report report = new Report(CurrentUser.getId(), reportParams.getType(), reportParams.getContent());
        reportMapper.insert(report);

        for (String image : reportParams.getImages()) {
            reportMapper.insertReportImage(report.getId(), image);
        }
        return Result.ok();
    }

    /**
     * 再处理
     */
    public Result dealAgain(Integer reportId) {
        Report report = reportMapper.selectById(reportId);
        if (!Objects.equals(report.getUserId(), CurrentUser.getId()) || report.getDealStatus() != 3) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        LambdaUpdateWrapper<Report> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(Report::getId, reportId)
                .set(Report::getDealStatus, 0);

        reportMapper.update(null, updateWrapper);

        return Result.ok();
    }

    /**
     * 投诉评分
     */
    @Transactional
    public Result remarkReport(Integer reportId, Integer detailId, Integer remark, String remarkContent) {
        Report report = reportMapper.selectById(reportId);
        if (report == null || !Objects.equals(report.getUserId(), CurrentUser.getId()) || report.getDealStatus() != 2) throw new InfoMessage(ResponseData.REMARK_FORBIDDEN);

        ReportDetail reportDetail = reportDetailMapper.selectById(detailId);
        if (reportDetail == null || !Objects.equals(reportDetail.getReportId(), report.getId())) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        // 修改投诉状态
        LambdaUpdateWrapper<Report> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Report::getId, reportId)
                .set(Report::getDealStatus, 3);
        reportMapper.update(null, updateWrapper);

        // 修改处理评价
        LambdaUpdateWrapper<ReportDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ReportDetail::getId, detailId)
                .set(ReportDetail::getRemark, remark)
                .set(ReportDetail::getRemarkContent, remarkContent)
                .set(ReportDetail::getRemarkTime, LocalDateTime.now());
        reportDetailMapper.update(null, wrapper);

        return Result.ok();
    }

    /**
     * 删除投诉
     *
     * @param reportId 投诉id
     */
    @Transactional
    public Result deleteReport(Integer reportId) throws IOException {
        Report report = reportMapper.selectById(reportId);
        if (report == null || !Objects.equals(report.getUserId(), CurrentUser.getId())) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        reportMapper.deleteById(reportId);

        // 删除图片
        List<String> images = reportMapper.getReportImages(reportId);
        for (String image: images) {
            fileUtil.removeImage(image);
        }
        reportMapper.deleteReportImagesByReportId(reportId);

        // 删除投诉处理
        reportDetailMapper.deleteByReportId(reportId);

        return Result.ok();
    }

    /**
     * 获取我的投诉
     */
    public Result getMyReport(Integer type, Integer dealStatus, Integer current, Integer size) {
        Page<Report> page = new Page<>(current, size);

        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Report::getUserId, CurrentUser.getId());

        queryWrapper.like(Report::getContent, "");

        if (type != -1) {
            queryWrapper.eq(Report::getType, type);
        }

        if (dealStatus != -1) {
            queryWrapper.eq(Report::getDealStatus, dealStatus);
        }

        queryWrapper.orderByDesc(Report::getTime);

        IPage<ReportDTO> resultPage = reportMapper.searchReportList(page, queryWrapper);

        return Result.success(resultPage);
    }






    /**
     * 显示所有投诉
     */
    public Result showAllReport(Integer type, Integer dealStatus, Integer current, Integer size) {
        Page<Report> page = new Page<>(current, size);

        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(Report::getContent, "");

        if (type != -1) {
            queryWrapper.eq(Report::getType, type);
        }

        if (dealStatus != -1) {
            queryWrapper.eq(Report::getDealStatus, dealStatus);
        }

        queryWrapper.orderByDesc(Report::getTime);

        IPage<ReportDTO> resultPage = reportMapper.searchReportList(page, queryWrapper);

        return Result.success(resultPage);
    }

    /**
     * 接取投诉
     */
    @Transactional
    public Result receiveReport(Integer reportId) {
        Report report = reportMapper.selectById(reportId);
        if (report == null || report.getDealStatus() != 0) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        // 更新投诉状态
        LambdaUpdateWrapper<Report> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Report::getId, reportId)
                .set(Report::getLastDealer, CurrentUser.getId())
                .set(Report::getDealStatus, 1);
        reportMapper.update(null, updateWrapper);

        // 插入投诉处理
        reportDetailMapper.insert(ReportDetail.builder()
                .reportId(reportId)
                .dealer(CurrentUser.getId())
                .build());

        return Result.ok();
    }

    /**
     * 取消接取投诉
     */
    @Transactional
    public Result cancelReceiveReport(Integer reportId, Integer detailId) {
        Report report = reportMapper.selectById(reportId);
        if (report == null || !Objects.equals(report.getLastDealer(), CurrentUser.getId()) || report.getDealStatus() != 1) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        Integer dealer;
        List<ReportDetailDTO> details = reportMapper.getDetails(reportId);
        if (!details.isEmpty()) {
            dealer = details.get(details.size() - 1).getDealer();
        } else {
            dealer = null;
        }

        LambdaUpdateWrapper<Report> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Report::getId, reportId)
                .set(Report::getLastDealer, dealer)
                .set(Report::getDealStatus, 0);
        reportMapper.update(null, updateWrapper);

        reportDetailMapper.deleteById(detailId);

        return Result.ok();
    }

    /**
     * 处理投诉
     *
     * @param reportId 投诉id
     * @param content 投诉内容
     */
    public Result dealReport(Integer reportId, Integer detailId, String content) {
        Report report = reportMapper.selectById(reportId);
        if (report == null || !Objects.equals(report.getLastDealer(), CurrentUser.getId()) || report.getDealStatus() != 1) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        // 修改状态
        LambdaUpdateWrapper<Report> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Report::getId, reportId)
                .set(Report::getDealStatus, 2);
        reportMapper.update(null, updateWrapper);

        // 修改处理内容
        LambdaUpdateWrapper<ReportDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ReportDetail::getId, detailId)
                .set(ReportDetail::getDealContent, content)
                .set(ReportDetail::getDealTime, LocalDateTime.now());
        reportDetailMapper.update(null, wrapper);

        return Result.ok();
    }

    /**
     * 转交
     */
    public Result deliver(Integer reportId, Integer detailId, Integer userId) {
        Report report = reportMapper.selectById(reportId);
        if (report == null || !Objects.equals(report.getLastDealer(), CurrentUser.getId()) || report.getDealStatus() != 1) throw new InfoMessage(ResponseData.REPORT_NOT_AVAILABLE);

        User user = userServiceIns.getUser(userId);
        if (user == null || !Objects.equals(user.getRole(), "客服")) throw new InfoMessage(ResponseData.USER_NOT_FOUND);

        // 修改处理内容
        LambdaUpdateWrapper<ReportDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ReportDetail::getId, detailId)
                .set(ReportDetail::getDealContent, "投诉已转交给客服 " + user.getLastName() + user.getFirstName())
                .set(ReportDetail::getDealTime, LocalDateTime.now());
        reportDetailMapper.update(null, wrapper);

        // 插入新处理
        reportDetailMapper.insert(ReportDetail.builder()
                .reportId(reportId)
                .dealer(userId)
                .build());

        // 修改处理人
        LambdaUpdateWrapper<Report> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Report::getId, reportId)
                .set(Report::getLastDealer, userId);
        reportMapper.update(null, updateWrapper);

        return Result.ok();
    }
}

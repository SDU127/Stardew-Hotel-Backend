package com.sdu127.Controller;

import com.sdu127.Annotation.RequiredLogin;
import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.DTO.ReportParams;
import com.sdu127.Data.VO.Result;
import com.sdu127.Service.ReportService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    ReportService reportService;

    /**
     * 投诉
     *
     * @param reportParams 投诉请求体
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @PostMapping("/report")
    public Result report(@RequestBody ReportParams reportParams) {
        return reportService.report(reportParams);
    }

    /**
     * 再处理
     *
     * @param reportId 投诉id
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @PostMapping("/dealAgain")
    public Result dealAgain(@RequestParam Integer reportId) {
        return reportService.dealAgain(reportId);
    }

    /**
     * 投诉评分
     *
     * @param reportId 投诉id
     * @param detailId 处理id
     * @param remark 评分
     * @param remarkContent 评价
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @PostMapping("/remarkReport")
    public Result remarkReport(@RequestParam Integer reportId, @RequestParam Integer detailId, @RequestParam Integer remark, @RequestParam String remarkContent) {
        return reportService.remarkReport(reportId, detailId, remark, remarkContent);
    }

    /**
     * 删除投诉
     *
     * @param reportId 投诉id
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @DeleteMapping("/deleteReport")
    public Result deleteReport(@RequestParam Integer reportId) throws IOException {
        return reportService.deleteReport(reportId);
    }

    /**
     * 获取我的投诉
     */
    @RequiredLogin(roles = {Role.CLIENT})
    @GetMapping("/getMyReport")
    public Result getMyReport(@RequestParam(required = false, defaultValue = "-1") Integer type,
                              @RequestParam(required = false, defaultValue = "-1") Integer dealStatus,
                              @RequestParam Integer current,
                              @RequestParam Integer size) {
        return reportService.getMyReport(type, dealStatus, current, size);
    }





    /**
     * 显示所有投诉
     */
    @RequiredLogin(roles = {Role.SERVER})
    @GetMapping("/showAllReport")
    public Result showAllReport(@RequestParam(required = false, defaultValue = "-1") Integer type,
                                @RequestParam(required = false, defaultValue = "-1") Integer dealStatus,
                                @RequestParam Integer current,
                                @RequestParam Integer size) {
        return reportService.showAllReport(type, dealStatus, current, size);
    }

    /**
     * 接取投诉
     *
     * @param reportId 投诉id
     */
    @RequiredLogin(roles = {Role.SERVER})
    @PostMapping("/receiveReport")
    public Result receiveReport(@RequestParam Integer reportId) {
        return reportService.receiveReport(reportId);
    }

    /**
     * 取消接取投诉
     *
     * @param reportId 投诉id
     */
    @RequiredLogin(roles = {Role.SERVER})
    @PostMapping("/cancelReceiveReport")
    public Result cancelReceiveReport(@RequestParam Integer reportId, @RequestParam Integer detailId) {
        return reportService.cancelReceiveReport(reportId, detailId);
    }

    /**
     * 处理投诉
     *
     * @param reportId 投诉id
     * @param content 投诉内容
     */
    @RequiredLogin(roles = {Role.SERVER})
    @PostMapping("/dealReport")
    public Result dealReport(@RequestParam Integer reportId, @RequestParam Integer detailId, @RequestParam String content) {
        return reportService.dealReport(reportId, detailId, content);
    }

    /**
     * 转交
     *
     * @param reportId 投诉id
     * @param userId 用户id
     */
    @RequiredLogin(roles = {Role.SERVER})
    @PostMapping("/deliver")
    public Result deliver(@RequestParam Integer reportId, @RequestParam Integer detailId, @RequestParam Integer userId) {
        return reportService.deliver(reportId, detailId, userId);
    }
}

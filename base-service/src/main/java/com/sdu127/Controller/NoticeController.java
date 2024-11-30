package com.sdu127.Controller;

import com.sdu127.Annotation.RequiredLogin;
import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.VO.Result;
import com.sdu127.Service.NoticeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    NoticeService noticeService;

    /**
     * 获取我的通知信息
     *
     */
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @GetMapping("/getNoticeList")
    public Result getNoticeList(@RequestParam(required = false, defaultValue = "-1") Integer type,
                                @RequestParam(required = false, defaultValue = "-1") Integer isRead,
                                @RequestParam Integer current,
                                @RequestParam Integer size) {
        return noticeService.getNoticeList(type, isRead, current, size);
    }

    /**
     * 读通知
     */
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @PostMapping("/read")
    public Result read(@RequestParam Integer noticeId) {
        return noticeService.read(noticeId);
    }






    /**
     * 发布通知
     */
    @RequiredLogin
    @PostMapping("/addNotice")
    public Result addNotice(@RequestParam(required = false, defaultValue = "-1") Integer recipient,
                            @RequestParam Integer type,
                            @RequestParam String content) {
        return noticeService.addNotice(recipient, type, content);
    }

    /**
     * 删除通知
     *
     * @param noticeId 通知id
     */
    @RequiredLogin
    @DeleteMapping("/deleteNotice")
    public Result deleteNotice(@RequestParam Integer noticeId) {
        return noticeService.deleteNotice(noticeId);
    }

}

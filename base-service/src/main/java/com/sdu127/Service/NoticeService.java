package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.DTO.NoticeDTO;
import com.sdu127.Data.PO.Notice;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.NoticeMapper;
import com.sdu127.exception.Exceptions.InfoMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticeService {
    @Resource
    NoticeMapper noticeMapper;
    @Resource
    UserServiceIns userServiceIns;

    /**
     * 获取通知信息
     */
    @Transactional
    public Result getNoticeList(Integer type, Integer isRead, Integer current, Integer size) {
        Page<Notice> page = new Page<>(current, size);

        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Notice::getRecipient, CurrentUser.getId());

        if (type != -1) {
            queryWrapper.eq(Notice::getType, type);
        } else {
            queryWrapper.or().eq(Notice::getType, 0);

            if (CurrentUser.getRole().equals("业主")) {
                queryWrapper.or().eq(Notice::getType, 1);
            }
            if (CurrentUser.getRole().equals("客服")) {
                queryWrapper.or().eq(Notice::getType, 2);
            }
        }

        queryWrapper.orderByDesc(Notice::getTime);

        IPage<NoticeDTO> resultPage;
        if (isRead == 0 || isRead == 1) {
            resultPage = noticeMapper.searchReadNoticeList(page, queryWrapper, isRead);
        } else {
            resultPage = noticeMapper.searchNoticeList(page, queryWrapper);
        }

        return Result.success(resultPage);
    }

    /**
     * 读通知
     */
    @Transactional
    public Result read(Integer noticeId) {
        NoticeDTO notice = noticeMapper.getNotice(noticeId);
        if (
                notice == null
                        || (notice.getRecipient() == null && notice.getType() == 2)
                        || (notice.getRecipient() != null && !notice.getRecipient().equals(CurrentUser.getId()))
        )
            throw new InfoMessage(ResponseData.NOTICE_NOT_AVAILABLE);

        if (notice.getIsRead() != 1) {
            noticeMapper.addRead(noticeId, CurrentUser.getId());
        }

        return Result.ok();
    }






    /**
     * 删除通知
     */
    @Transactional
    public Result deleteNotice(Integer noticeId) {
        noticeMapper.deleteById(noticeId);
        noticeMapper.deleteReadById(noticeId);
        return Result.ok();
    }

    /**
     * 发布通知
     */
    @Transactional
    public Result addNotice(Integer recipient, Integer type, String content) {
        Notice notice = Notice.builder()
                .type(type)
                .content(content)
                .build();

        if (recipient != -1) {
            User user = userServiceIns.getUser(recipient);
            if (user == null) throw new InfoMessage(ResponseData.USER_NOT_FOUND);
            notice.setRecipient(recipient);
        }
        noticeMapper.insert(notice);
        return Result.ok();
    }

}

package com.sdu127.Service;

import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.CommonMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService {
    @Resource
    CommonMapper commonMapper;

    /**
     * 获取身份列表
     */
    public Result getRoles() {
        List<String> roles = commonMapper.getRoles();
        return Result.success(roles);
    }
}

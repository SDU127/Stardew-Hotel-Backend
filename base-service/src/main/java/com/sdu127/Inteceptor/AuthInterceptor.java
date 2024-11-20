package com.sdu127.Inteceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.Token;
import com.sdu127.Mapper.CommonMapper;
import com.sdu127.Util.JWTUtil;
import com.sdu127.exception.Exceptions.PathPermissionException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Optional;

/**
 * 鉴权拦截
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    ObjectMapper objectMapper;
    @Resource
    CommonMapper commonMapper;
    @Resource
    JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) throws JsonProcessingException {
        response.setContentType("application/json;charset=UTF-8");

        // 获取请求头内数据
        String accessToken = request.getHeader("Authorization");

        // 获取token的payload中信息
        DecodedJWT info = jwtUtil.getTokenInfo(accessToken, Token.ACCESS.getType());

        // 判断是否为accessToken
        String tokenType = info.getClaim("type").asString();
        if (!tokenType.equals(Token.ACCESS.getType())) return false;

        // 获取用户权限
        String userRole = info.getClaim("role").asString();

        // 获取身份权限组
        String rolePermission = commonMapper.getRolePermission(userRole);
        List<Integer> permissions = objectMapper.readValue(rolePermission, new TypeReference<>() {});

        // 获取请求路径
        String requestURI = request.getRequestURI();

        // 获取路径权限要求，默认为5
        Optional<Integer> pathLevelOption = commonMapper.getPathLevel(requestURI);
        Integer pathLevel = pathLevelOption.orElse(5);

        // 判断是否有权限
        if (!permissions.contains(pathLevel))
            throw new PathPermissionException("无权限", CurrentUser.getId(), requestURI);

        // 存储当前用户信息
        CurrentUser.setAll(
                Integer.parseInt(info.getClaim("id").asString()),
                info.getClaim("user_name").asString(),
                userRole
        );

        return true;
    }
}


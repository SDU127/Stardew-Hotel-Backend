package com.sdu127.Inteceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.Constant.Token;
import com.sdu127.Exception.Exceptions.PathPermissionException;
import com.sdu127.Util.JWTUtil;
import com.sdu127.Annotation.RequiredLogin;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 鉴权拦截
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    JWTUtil jwtUtil;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) {
        response.setContentType("application/json;charset=UTF-8");

        // 判断调用的是不是一个接口方法
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        // 获取请求头内数据
        String accessToken = request.getHeader("Authorization");

        // java反射
        Method method = ((HandlerMethod) handler).getMethod();

        // 如果类上有该注解则执行
        if(method.isAnnotationPresent(RequiredLogin.class)){
            // 如果注解中required = true则执行
            if(method.getAnnotation(RequiredLogin.class).required()){
                // 获取token的payload中信息
                DecodedJWT info = jwtUtil.getTokenInfo(accessToken, Token.ACCESS.getType());

                // 判断是否为accessToken
                String tokenType = info.getClaim("type").asString();
                if (!tokenType.equals(Token.ACCESS.getType())) return false;

                // 获取用户权限
                String userRole = info.getClaim("role").asString();

                // 存储当前用户信息
                CurrentUser.setAll(
                        Integer.parseInt(info.getClaim("id").asString()),
                        info.getClaim("user_name").asString(),
                        userRole
                );

                // 获取请求路径
                String requestURI = request.getRequestURI();

                // 获取需求的权限
                Role[] requiredRoles = method.getAnnotation(RequiredLogin.class).roles();

                // 放行ADMIN
                if (Objects.equals(userRole, Role.ADMIN.role)) return true;

                // 进行权限比对
                if(Arrays.stream(requiredRoles)
                        .noneMatch(role -> role.getRole().equals(userRole)))
                    throw new PathPermissionException("无权限", CurrentUser.getId(), requestURI);
            }
        }

        return true;
    }
}


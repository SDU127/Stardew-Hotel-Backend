package com.sdu127.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redis
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    ObjectMapper objectMapper;

    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        return execute(template -> template.getExpire(key, TimeUnit.SECONDS));
    }

    /**
     * 存入
     */
    public void set(String key, Object value) {
        execute(() -> redisTemplate.opsForValue().set(key, value));
    }

    /**
     * 存入，带过期时间
     */
    public void set(String key, Object value, long time) {
        execute(() -> {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
        });
    }

    /**
     * 存入List
     */
    public void setList(String key, Object value) {
        execute(() -> {
            try {
                String json = objectMapper.writeValueAsString(value);
                redisTemplate.opsForValue().set(key, json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    /**
     * 获取
      */
    public Object get(String key) {
        return execute(template -> template.opsForValue().get(key));
    }

    /**
     * 获取List
     */
    public <T> List<T> getList(String key) {
        return execute(template -> {
            Object json = template.opsForValue().get(key);
            if (json == null) {
                return new ArrayList<>();
            }
            try {
                return objectMapper.readValue(json.toString(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    /**
     * 删除
      */
    public void deleteKey(String key) {
        execute(() -> redisTemplate.delete(key));
    }

    private <T> T execute(Function<RedisTemplate<Object, Object>, T> function) {
        try {
            return function.apply(redisTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void execute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

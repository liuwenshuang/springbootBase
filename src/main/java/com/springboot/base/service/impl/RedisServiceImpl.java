package com.springboot.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.springboot.base.constant.SystemConstants;
import com.springboot.base.data.entity.UserInfo;
import com.springboot.base.service.RedisService;
import lombok.extern.log4j.Log4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 * Created by jay on 2017-9-28.
 */
@Service
@Log4j
public class RedisServiceImpl implements RedisService {

    private final String USER_TOKEN_KEY = "user_token_";//用户登录后存入redis的key

    private final String USER_PASSWORD_NUMBER_KEY = "user_password_";//用户猜密码次数存入redis的key

    @Inject
    private StringRedisTemplate template;

    @Override
    public void saveUser(UserInfo userInfo) {
        save(USER_TOKEN_KEY + userInfo.getKey(), userInfo, SystemConstants.USER_TOKEN_TIME_LONG, TimeUnit.MINUTES);
    }

/*    @Override
    public UserInfo getUserInfo(UserInfo userInfo) throws Exception {
        return get(USER_TOKEN_KEY + TokenUtils.getKey(userInfo), UserInfo.class);
    }*/

    /**
     * 保存该用户名猜密码次数
     * @param username 登录名
     * @param number 猜的次数
     */
    @Override
    public void saveUserPasswordNumber(String username, Integer number) {
        save(USER_PASSWORD_NUMBER_KEY + username, number, SystemConstants.USER_PASSWORD_TIME_LONG, TimeUnit.MINUTES);
    }

    /**
     * 获取该用户名猜密码次数
     * @param username 登录名
     */
    @Override
    public Integer getUserPasswordNumber(String username) {
        String numberString = get(USER_PASSWORD_NUMBER_KEY + username);
        Integer number = numberString == null ? 0 : Integer.parseInt(numberString);
        return number;
    }

    @Override
    public UserInfo getUserInfoByKey(String key) {
        return get(USER_TOKEN_KEY + key, UserInfo.class);
    }

    @Override
    public void removeUserTokenByKey(String key) {
        template.delete(USER_TOKEN_KEY + key);
    }

    @Override
    public void removeUserPasswordNumberByKey(String key) {
        template.delete(USER_PASSWORD_NUMBER_KEY + key);
    }

    private void save(String key, Object value, long time, TimeUnit timeUnit) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, JSON.toJSONString(value), time, timeUnit);
    }

    private <T> T get(String key, Class<T> clazz) {
        ValueOperations<String, String> ops = template.opsForValue();
        String value = ops.get(key);
        T t = null;
        if (value != null && value.length() > 0) {
            t = JSON.parseObject(value, clazz);
        }
        return t;
    }

    private String get(String key) {
        ValueOperations<String, String> ops = template.opsForValue();
        return ops.get(key);
    }

}
package com.anjuxing.platform.face.huawei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xiongt
 * @Description
 */
@Component
public class RedisRepository {

    private String PREFIX_TOKEN_KEY = "HUAWEI_TOKEN";

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public void save(String token,long expiresIn){
        redisTemplate.opsForValue().set(getTokenKey(),token ,expiresIn,TimeUnit.SECONDS);
    }

    public String getAccessToken(){
        return (String) redisTemplate.opsForValue().get(getTokenKey());
    }



    public String getTokenKey(){
        return PREFIX_TOKEN_KEY  ;
    }




}

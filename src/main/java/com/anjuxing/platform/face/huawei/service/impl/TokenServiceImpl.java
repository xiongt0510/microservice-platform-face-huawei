package com.anjuxing.platform.face.huawei.service.impl;

import com.anjuxing.platform.face.huawei.service.RedisRepository;
import com.anjuxing.platform.face.huawei.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiongt
 * @Description
 */
@Service
public class TokenServiceImpl implements TokenService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private ObjectMapper mapper;

    private final String url = "https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens";


    @Override
    public String getToken() {

        String requestBody = requestBody();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, requestHeaders);

        logger.info("HttpEntity request body :"+entity.getBody());

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,entity,String.class);
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        logger.info("ResponseEntity header : "+ responseEntity.getHeaders());
        logger.info("ResponseEntity body : "+ responseEntity.getBody());

        String token = "";
        if (responseHeaders.containsKey("X-Subject-Token")){
            List<String> tokens = responseHeaders.get("X-Subject-Token");
            token = tokens.get(0);
        }
        if (StringUtils.isEmpty(token)){
            return "找不到token";
        }

        String responseBody = responseEntity.getBody();

        String expiresAt = null;
        try {
            expiresAt = mapper.readTree(responseBody).path("token").path("expires_at").asText();
        } catch (IOException e) {
            throw new  RuntimeException("读取 expires_at 失败");
        }

        logger.info("expiresAt:" + expiresAt);

        redisRepository.save(token,12 * 3600);

        return token;
    }


//    {
//        "auth": {
//        "identity": {
//            "methods": [
//            "password"
//      ],
//            "password": {
//                "user": {
//                    "name": "anjuxingkeji",
//                            "password": "anjuxing2015",
//                            "domain": {
//                        "name": "anjuxingkeji"
//                    }
//                }
//            }
//        },
//        "scope": {
//            "project": {
//                "name": "cn-north-1"
//            }
//        }
//    }
//    }
    /**
     *
     * @return
     */
    private  String requestBody(){

   String paramJson = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\":{\"user\":{\"name\": \"anjuxingkeji\",\"password\": \"anjuxing2015\",\"domain\": {\"name\": \"anjuxingkeji\"}}}},\"scope\": {\"project\": {\"name\": \"cn-north-1\"}}}}";
        return  paramJson;
    }
}

package com.anjuxing.platform.face.huawei.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import com.anjuxing.platform.face.huawei.service.RedisRepository;
import static com.anjuxing.platform.face.huawei.service.TokenConstant.*;

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

    @Autowired
    private HuaweiProperties hw;

    @Override
    public String getToken() {

        String requestBody = requestBody();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, requestHeaders);

        logger.info("HttpEntity request body :"+entity.getBody());

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(hw.getTokenUrl(),entity,String.class);
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        logger.info("ResponseEntity header : "+ responseEntity.getHeaders());
        logger.info("ResponseEntity body : "+ responseEntity.getBody());

        String token = "";
        if (responseHeaders.containsKey(HEADER_X_SUBJECT_TOKEN)){
            List<String> tokens = responseHeaders.get(HEADER_X_SUBJECT_TOKEN);
            token = tokens.get(0);
        }
        if (StringUtils.isEmpty(token)){
            return "找不到token";
        }

        String responseBody = responseEntity.getBody();

        String expiresAt = null;
        try {
            expiresAt = mapper.readTree(responseBody).path(TOKEN).path(EXPIRES_AT).asText();
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
    //                  "password"
    //              ],
    //            "password": {
    //                "user": {
    //                    "name": "anjuxingkeji",
    //                    "password": "anjuxing2015",
    //                    "domain": {
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

        //domain
        JSONObject domain = new JSONObject();
        domain.put(REQUEST_JSON_KEY_NAME,hw.getAccount().getUsername());

        //User
        JSONObject user = new JSONObject();
        user.put(REQUEST_JSON_KEY_NAME,hw.getAccount().getUsername());
        user.put(REQUEST_JSON_KEY_PASSWORD,hw.getAccount().getPassword());
        user.put(REQUEST_JSON_KEY_DOMAIN, domain);

        //methods
        JSONArray methods = new JSONArray();
        methods.add(REQUEST_JSON_KEY_PASSWORD);

        //password
        JSONObject password = new JSONObject();
        password.put(REQUEST_JSON_KEY_USER,user);

        //identity
        JSONObject identity = new JSONObject();
        identity.put(REQUEST_JSON_KEY_METHODS,methods);
        identity.put(REQUEST_JSON_KEY_PASSWORD,password);

        //scope====

        JSONObject project = new JSONObject();
        project.put(REQUEST_JSON_KEY_NAME,hw.getProject().getProjectName());


        JSONObject scope = new JSONObject();
        scope.put(REQUEST_JSON_KEY_PROJECT,project);
        //====

        JSONObject auth = new JSONObject();
        auth.put(REQUEST_JSON_KEY_IDENTITY,identity);
        auth.put(REQUEST_JSON_KEY_SCOPE,scope);

        JSONObject data = new JSONObject();
        data.put(REQUEST_JSON_KEY_AUTH,auth);


        return  data.toString();

    }
}

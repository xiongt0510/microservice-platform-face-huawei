package com.anjuxing.platform.face.huawei.service.impl;

import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import com.anjuxing.platform.face.huawei.service.*;

import static  com.anjuxing.platform.face.huawei.service.FaceConstant.*;
import com.cloud.sdk.http.HttpMethodName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author xiongt
 * @Description
 */
@Service
public class FaceServiceImpl extends AbstractHttpService implements FaceService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HuaweiProperties huawei;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisRepository redis;



    /**
     * 通过url 图像比对，返回 相似度
     * @param url1
     * @param url2
     * @return
     */
    @Override
    public Double faceCompare(String url1, String url2) throws Exception {

        String faceCompareUrl =huawei.getCompare().getEndpoint()+
                String.format(FACE_COMPARE_URL,huawei.getProject().getProjectId());
        logger.info("url :" + faceCompareUrl);

        String key1 = url1;
        String key2 = url2;
        String params = params(key1,key2);

        HttpResponse response = super.template(faceCompareUrl, HttpMethodName.POST,params);
        String resp = super.httpEntityData(response);
        Map<String ,Object> map  = mapper.readValue(resp, Map.class);

        return (Double) map.get(FACE_COMPARE_RESULT_SIMILARITY);
    }

    /**
     * 获取url 人脸比对的url 参数
     * @param key1 在
     * @param key2
     * @return
     * @throws JsonProcessingException
     */
    private String params(String key1,String key2 ) throws JsonProcessingException {

        String url1 = String.format(REQUEST_PARAM_IMAGE_URL,huawei.getObs().getBucketName(),key1);
        String url2 = String.format(REQUEST_PARAM_IMAGE_URL,huawei.getObs().getBucketName(),key2);

        Map<String,String> urlMap = new HashMap<>();
        urlMap.put(PARAM_IMAGE1_URL,url1);
        urlMap.put(PARAM_IMAGE2_URL,url2);
        return mapper.writeValueAsString(urlMap);
    }

    @Override
    protected void addContent(HttpRequestBase request, String content) {
        HttpPost httpPost = request instanceof HttpPost ? (HttpPost)request : null;
        if (Objects.nonNull(httpPost)){
            httpPost. setEntity(new StringEntity(content, Charset.forName("UTF-8")));
        }

    }

    @Override
    protected void addHeader(HttpRequestBase request) {
        super.addHeader(request);
        if (StringUtils.isEmpty(redis.getAccessToken())){
            tokenService.getToken();
        }
        request.addHeader(TokenConstant.HEADER_X_AUTH_TOKEN,redis.getAccessToken());
    }





}

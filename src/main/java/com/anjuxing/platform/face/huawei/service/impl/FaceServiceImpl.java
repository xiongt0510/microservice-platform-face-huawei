package com.anjuxing.platform.face.huawei.service.impl;

import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import com.anjuxing.platform.face.huawei.service.AbstractHttpService;
import com.anjuxing.platform.face.huawei.service.FaceService;
import com.anjuxing.platform.face.huawei.service.RedisRepository;
import com.anjuxing.platform.face.huawei.service.TokenService;
import com.cloud.sdk.http.HttpMethodName;
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

    //{project_id}
    private static final String FACE_COMPARE_URL = "/v1/%s/face-compare";

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

        return (Double) map.get("similarity");
    }

    private String params(String key1 ,String key2){

        String params = "{\"image1_url\":\"%s\",\"image2_url\":\"%s\"}";
        String url = "/%s/%s";
        String url1 = String.format(url,huawei.getObs().getBucketName(),key1);
        String url2 = String.format(url,huawei.getObs().getBucketName(),key2);

        return String.format(params,url1,url2);
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
        request.addHeader("X-Auth-Token",redis.getAccessToken());
    }





}

package com.anjuxing.platform.face.huawei.service.impl;

import com.anjuxing.platform.face.huawei.model.UpredictResult;
import com.anjuxing.platform.face.huawei.service.AbstractHttpService;
import com.anjuxing.platform.face.huawei.service.RedisRepository;
import com.anjuxing.platform.face.huawei.service.TokenService;
import com.anjuxing.platform.face.huawei.service.UpredictService;
import com.anjuxing.platform.face.huawei.util.ImageUtil;
import com.cloud.sdk.http.HttpMethodName;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;

/**
 * @author xiongt
 * @Description
 */
@Service
public class UpredictServiceImpl extends AbstractHttpService implements UpredictService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisRepository redis;


    private static final String URL = "https://upredict-service.cn-north-1.myhuaweicloud.com/upredict/v1.0/infers/ivs-standard";

    private static final String params = "{\"meta\":{\"uuid\":\"%s\"},\"data\":{\"req_data\":[{%s}]}}";

    @Override
    public UpredictResult upredict(File idCardImage1,File idCardImage2, File faceImage ,boolean liveDetect) throws Exception {

        /**
         * 把图片转换为Base64
         */
        String idCardImage1Base64 = ImageUtil.ImageToBase64(idCardImage1);
        idCardImage1Base64 = replace(idCardImage1Base64);

        String idCardImage2Base64 = Objects.nonNull(idCardImage2)?ImageUtil.ImageToBase64(idCardImage2) : "";
        idCardImage2Base64 = replace(idCardImage2Base64);

        String faceImageBase64 = ImageUtil.ImageToBase64(faceImage);
        faceImageBase64 = replace(faceImageBase64);


        String reqData = "\"idcard_image1\":\"%s\",\"face_image\":\"%s\",\"live_detect\":%s";

        reqData = String.format(reqData,idCardImage1Base64,faceImageBase64,liveDetect);

        if (!StringUtils.isEmpty(idCardImage2Base64)){
            reqData += ",\"idcard_image2\":\""+idCardImage2Base64+"\"";
        }

        String reqParams  = String.format(params,UUID.randomUUID(),reqData);

        HttpResponse response = template(URL, HttpMethodName.POST,reqParams);

        String resp = super.httpEntityData(response);


        UpredictResult ur = paserResult(resp);

        return ur;
    }


    private String replace(String str){
        if(StringUtils.isEmpty(str)) return str;

        str = str.replaceAll("\n","");
        str = str.replaceAll("\r","");
        return str;
    }

    private UpredictResult paserResult(String data) throws IOException {

       JsonNode node =  mapper.readTree(data);
       JsonNode result = node.path("result");

       UpredictResult ur = new UpredictResult();
       ur.setUuid(node.path("meta").path("uuid").asText());
       ur.setCount(result.path("count").asInt());
       ur.setErrorCode(result.path("error_code").asText());
       ur.setErrorMsg(result.path("error_msg").asText());
       ur.setServiceName(result.path("service_name").asText());
       ur.setServiceVersion(result.path("service_version").asText());

       if(result.has("resp_data")) {
           ur.setVerificationResult(result.path("resp_data").get(0).path("verification_result").asText());
           ur.setVerificationMessage(result.path("resp_data").get(0).path("verification_message").asText());
       }

       return ur;
    }


    @Override
    protected void addContent(HttpRequestBase request,String content) {
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

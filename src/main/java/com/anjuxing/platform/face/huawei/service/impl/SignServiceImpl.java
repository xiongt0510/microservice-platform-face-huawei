package com.anjuxing.platform.face.huawei.service.impl;

import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import com.anjuxing.platform.face.huawei.service.SignService;
import com.cloud.sdk.DefaultRequest;
import com.cloud.sdk.Request;
import com.cloud.sdk.auth.credentials.BasicCredentials;
import com.cloud.sdk.auth.signer.Signer;
import com.cloud.sdk.auth.signer.SignerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * @author xiongt
 * @Description
 */
@Service
public class SignServiceImpl implements SignService {


    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private HuaweiProperties hw;

    @Override
    public Map<String, String> signHeaders(URL url)  {
        Request request = new DefaultRequest(hw.getCompare().getServiceName());

        try {
            request.setEndpoint(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Signer signer = SignerFactory.getSigner(hw.getCompare().getServiceName(),hw.getCompare().getRegion());

        signer.sign(request,new BasicCredentials(hw.getAccount().getAccessKey(),hw.getAccount().getAccessSecret()));

        logger.info("签名是："+ request.getHeaders().get("Authorization"));
        return request.getHeaders();

    }




}

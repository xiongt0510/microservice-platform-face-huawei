package com.anjuxing.platform.face.huawei.controller;

import com.anjuxing.platform.face.huawei.model.ObjectStoreResult;
import com.anjuxing.platform.face.huawei.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiongt
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ObjectStoreResult storeResult(){

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:10001/object/store/file";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();

        File file = new File("C:\\Users\\Administrator\\Pictures\\4a2ce919a3444bb19a000e6abbcea855.jpeg");

        param.add("file", file);
        param.add("bucketName", "");
        param.add("key", "");

        ObjectStoreResult result  = restTemplate.postForObject(url,param,ObjectStoreResult.class);
        return result;
    }


    @GetMapping("/token")
    public String testToken() throws Exception {
        return tokenService.getToken();
    }
}

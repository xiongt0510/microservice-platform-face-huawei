package com.anjuxing.platform.face.huawei.service.impl;

import com.anjuxing.platform.face.huawei.model.ObjectStoreResult;
import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import com.anjuxing.platform.face.huawei.service.ObjectStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author xiongt
 * @Description
 */
@Service
public class ObjectStoreServiceImpl implements ObjectStoreService {

    @Autowired
    private ObsClient obsClient;

    @Autowired
    private HuaweiProperties huawei;


    @Autowired
    private ObjectMapper mapper ;

    @Override
    public ObjectStoreResult putObject(String bucketName, String key, File file) {
        if (StringUtils.isEmpty(bucketName)){
            bucketName = huawei.getObs().getBucketName();
        }

        if (StringUtils.isEmpty(key)){
            key = file.getName();
        }

        File f = new File("C:\\Users\\Administrator\\Pictures\\4a2ce919a3444bb19a000e6abbcea855.jpeg");

        PutObjectResult result =  obsClient.putObject(bucketName,key,f);

        return mapper.convertValue(result,ObjectStoreResult.class);
    }
}

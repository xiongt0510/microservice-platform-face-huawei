package com.anjuxing.platform.face.huawei.controller;

import com.anjuxing.platform.face.huawei.model.ObjectStoreResult;
import com.anjuxing.platform.face.huawei.service.ObjectStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Objects;

/**
 * @author xiongt
 * @Description
 */
@RestController
@RequestMapping("/object/store")
public class ObjectStoreController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectStoreService objectStoreService;


    @PostMapping("/file")
    public ObjectStoreResult objectStoreResult(@RequestParam(name = "bucketName",required = false) String bucketName,
                                               @RequestParam(name="key",required = false) String key ,
                                               @RequestParam(name = "file") File file){
        if (Objects.isNull(file)){
            logger.debug("上传文件为空");
            throw new NullPointerException("上传文件为空");
        }

        return objectStoreService.putObject(bucketName,key,file);

    }
}

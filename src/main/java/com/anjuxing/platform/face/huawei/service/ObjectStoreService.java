package com.anjuxing.platform.face.huawei.service;

import com.anjuxing.platform.face.huawei.model.ObjectStoreResult;

import java.io.File;

/**
 * @author xiongt
 * @Description
 */
public interface ObjectStoreService {

    /**
     *
     * @param bucketName 存储桶的名称
     * @param key 可以传可以默认
     * @param file 文件
     * @return
     */
    ObjectStoreResult putObject(String bucketName, String key, File file);
}

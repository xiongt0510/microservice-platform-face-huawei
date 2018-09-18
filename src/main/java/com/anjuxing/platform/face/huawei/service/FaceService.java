package com.anjuxing.platform.face.huawei.service;


/**
 * @author xiongt
 * @Description 人脸
 */
public interface FaceService {

    /**
     * 人脸比对
     * @param url1 /bucketName/key1
     * @param url2 /bucketName/key2
     * @return
     * @throws Exception
     */
    Double faceCompare(String url1 ,String url2) throws Exception ;


}

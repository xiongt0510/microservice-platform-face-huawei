package com.anjuxing.platform.face.huawei.service;

import com.anjuxing.platform.face.huawei.model.UpredictResult;

import java.io.File;

/**
 * @author xiongt
 * @Description 人证比对接口
 */
public interface UpredictService {

        UpredictResult upredict(File idCardImage1 ,File idCardImage2,File faceImage ,boolean liveDetect) throws Exception;
}

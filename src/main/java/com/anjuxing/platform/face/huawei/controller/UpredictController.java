package com.anjuxing.platform.face.huawei.controller;

import com.anjuxing.platform.face.huawei.model.UpredictResult;
import com.anjuxing.platform.face.huawei.service.UpredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * @author xiongt
 * @Description
 */
@RestController
@RequestMapping("/upredict")
public class UpredictController {

    @Autowired
    private UpredictService upredictService;


    @GetMapping
    public UpredictResult upredictResult(
//            @RequestParam("idCardImage1")File idCardImage1,
//            @RequestParam(value = "idCardImage2",required = false)File idCardImage2,
//            @RequestParam("faceImae")File faceImage
    ) throws Exception {
        File idCardImage1 = new File("C:\\Users\\Administrator\\Pictures\\160704104207126.jpg");
        File faceImage = new File("C:\\Users\\Administrator\\Pictures\\微信截图_20180913102123.jpg");
        File idCardImage2 = null;

        boolean defaultLiveDetect = false;

        return upredictService.upredict(idCardImage1,idCardImage2,faceImage,defaultLiveDetect);
    }
}

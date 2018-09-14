package com.anjuxing.platform.face.huawei.controller;

import com.anjuxing.platform.face.huawei.service.FaceService;
import com.anjuxing.platform.face.huawei.service.impl.FaceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiongt
 * @Description
 */
@RestController
@RequestMapping("/huawei/face")
public class FaceController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FaceServiceImpl faceService;

    @GetMapping("/compare")
    public String faceCompare(@RequestParam("url1") String url1,
                              @RequestParam("url2") String url2)throws Exception{

      Double sim =   faceService.faceCompare(url1,url2);

      return sim.toString();

    }

    @GetMapping("/test/compare")
    public String testFaceCompare() throws Exception {

        String url1 = "4a2ce919a3444bb19a000e6abbcea855.jpeg\\\"";
        String url2 = "4a2ce919a3444bb19a000e6abbcea855.jpeg\\\"_5";

//        String sim =faceCompare(url1,url2);
        Double sim =   faceService.faceCompare(url1,url2);

        return sim.toString();

    }

}

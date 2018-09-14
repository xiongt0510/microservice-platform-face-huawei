package com.anjuxing.platform.face.huawei.service;


import java.net.URL;
import java.util.Map;

/**
 * @author xiongt
 * @Description 签名接口
 */
public interface SignService {

    Map<String ,String> signHeaders(URL url);

}

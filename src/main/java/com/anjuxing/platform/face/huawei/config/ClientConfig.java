package com.anjuxing.platform.face.huawei.config;

import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import com.obs.services.ObsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiongt
 * @Description
 */
@Configuration
public class ClientConfig {

    @Autowired
    private HuaweiProperties hw;

    @Bean
    public ObsClient obsClient(){
        return new ObsClient(hw.getAccount().getAccessKey(),
                hw.getAccount().getAccessSecret(),
                hw.getObs().getEndpoint());
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

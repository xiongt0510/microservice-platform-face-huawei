package com.anjuxing.platform.face.huawei.config;

import com.anjuxing.platform.face.huawei.properties.HuaweiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiongt
 * @Description
 */
@Configuration
@EnableConfigurationProperties(HuaweiProperties.class)
public class HuaweiPropertiesConfiguration {
}

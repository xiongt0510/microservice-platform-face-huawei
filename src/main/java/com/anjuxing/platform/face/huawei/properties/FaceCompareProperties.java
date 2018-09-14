package com.anjuxing.platform.face.huawei.properties;

/**
 * @author xiongt
 * @Description
 */
public class FaceCompareProperties {

    private String endpoint = "https://frs.cn-north-1.myhuaweicloud.com";

    private String serviceName = "iam.cn-north-1.myhuaweicloud.com";

    private String region = "cn-north-1";

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}

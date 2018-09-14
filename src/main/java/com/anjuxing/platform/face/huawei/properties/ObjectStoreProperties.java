package com.anjuxing.platform.face.huawei.properties;

/**
 * @author xiongt
 * @Description
 */
public class ObjectStoreProperties {

    private String endpoint = "https://obs.cn-north-1.myhwclouds.com";

    private String region = "cn-north-1";

    private String bucketName = "obs-1d4b";


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

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}

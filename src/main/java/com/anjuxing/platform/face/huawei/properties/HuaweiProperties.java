package com.anjuxing.platform.face.huawei.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiongt
 * @Description
 */
@ConfigurationProperties(prefix = "huawei")
public class HuaweiProperties {

    private AccountProperties account = new AccountProperties();

    private ObjectStoreProperties obs = new ObjectStoreProperties();

    private FaceCompareProperties compare = new FaceCompareProperties();

    private ProjectProperties project = new ProjectProperties();

    private String tokenUrl = "https://iam.cn-north-1.myhuaweicloud.com/v3/auth/tokens";

    public AccountProperties getAccount() {
        return account;
    }

    public void setAccount(AccountProperties account) {
        this.account = account;
    }

    public ObjectStoreProperties getObs() {
        return obs;
    }

    public void setObs(ObjectStoreProperties obs) {
        this.obs = obs;
    }

    public FaceCompareProperties getCompare() {
        return compare;
    }

    public void setCompare(FaceCompareProperties compare) {
        this.compare = compare;
    }

    public ProjectProperties getProject() {
        return project;
    }

    public void setProject(ProjectProperties project) {
        this.project = project;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
}

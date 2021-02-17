package com.ruoyi.common.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "igtpush")
public class IGtPushConfig
{
    private static String APPID;

    private static String AppKey;

    private static String AppSecret;

    private static String MasterSecret;

    private static String MsgHOST;

    public void setAPPID(String APPID) {
        this.APPID = APPID;
    }

    public void setAppKey(String appKey) {
        AppKey = appKey;
    }

    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    public void setMasterSecret(String masterSecret) {
        MasterSecret = masterSecret;
    }

    public void setMsgHOST(String msgHOST) {
        MsgHOST = msgHOST;
    }

    public static String getAPPID() {
        return APPID;
    }

    public static String getAppKey() {
        return AppKey;
    }

    public static String getAppSecret() {
        return AppSecret;
    }

    public static String getMasterSecret() {
        return MasterSecret;
    }

    public static String getMsgHOST() {
        return MsgHOST;
    }

    @Override
    public String toString() {
        return "IGtPushProperties{" +
                "APPID='" + APPID + '\'' +
                ", AppKey='" + AppKey + '\'' +
                ", AppSecret='" + AppSecret + '\'' +
                ", MasterSecret='" + MasterSecret + '\'' +
                ", MsgHOST='" + MsgHOST + '\'' +
                '}';
    }
}


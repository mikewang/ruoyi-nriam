package com.ruoyi.common.utils.push;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.ruoyi.common.config.IGtPushConfig;

public class PushMessageToApp {

    private static String appId = IGtPushConfig.getAPPID();
    private static String appKey = IGtPushConfig.getAppKey();
    private static String masterSecret = IGtPushConfig.getMasterSecret();
    public static String host = IGtPushConfig.getMsgHOST();

    /**
     * 对单个用户推送消息
     *
     * @return
     */
    public static boolean pushMessageToSingle(String clientId, String msgTitle ,String msgBody) {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        NotificationTemplate template = notificationTemplate(msgTitle, msgBody, "");
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(12 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null && ret.getResponse() != null && ret.getResponse().containsKey("result")) {
            System.out.println(ret.getResponse().toString());
            if (ret.getResponse().get("result").toString().equals("ok") && ret.getResponse().containsKey("status")) {
                return true;
            }
        }
        return false;
    }

    private static NotificationTemplate notificationTemplate(String title, String message, String obj) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent(obj);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(message);
        // 配置通知栏图标
        style.setLogo("");
        // 配置通知栏网络图标
        //style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        return template;
    }

}

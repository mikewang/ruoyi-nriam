package com.ruoyi.audit.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class AudSignpic extends BaseEntity {
    /**
     * ID
     */
    private Integer signpicId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 签名图片
     */
    private String signpicName;

    public Integer getSignpicId() {
        return signpicId;
    }

    public void setSignpicId(Integer signpicId) {
        this.signpicId = signpicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSignpicName() {
        return signpicName;
    }

    public void setSignpicName(String signpicName) {
        this.signpicName = signpicName;
    }

    @Override
    public String toString() {
        return "AudSignpic{" +
                "signpicId=" + signpicId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", signpicName='" + signpicName + '\'' +
                '}';
    }
}

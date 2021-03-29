package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PmProjectmember extends BaseEntity {

    private Integer projectid;

    private Integer userId;

    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
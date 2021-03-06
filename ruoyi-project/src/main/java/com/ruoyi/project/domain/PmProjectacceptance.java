package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class PmProjectacceptance extends BaseEntity {

    private Integer projectid;

    private String projectname;

    private Integer status;

    private String opinion;

    private String memo;

    private List<AudProjectdoc> projectdocList;

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public List<AudProjectdoc> getProjectdocList() {
        return projectdocList;
    }

    public void setProjectdocList(List<AudProjectdoc> projectdocList) {
        this.projectdocList = projectdocList;
    }

    @Override
    public String toString() {
        return "PmProjectacceptance{" +
                "projectid=" + projectid +
                ", opinion='" + opinion + '\'' +
                ", memo='" + memo + '\'' +
                ", projectdocList=" + projectdocList +
                '}';
    }
}
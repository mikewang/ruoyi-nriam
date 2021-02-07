package com.ruoyi.project.domain;

import java.util.List;

public class PmProjectacceptance {

    private Integer projectid;

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
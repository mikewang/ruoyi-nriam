package com.ruoyi.audit.domain;

public class AudApply {

    private Integer applyid;

    private String applytype;

    private Integer relatedid;

    private Integer applyuserid;

    private String applytime;

    private String applyreason;

    private Integer audituserid;

    private String audittime;

    private String auditopinion;

    private Integer applystatus;

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public String getApplytype() {
        return applytype;
    }

    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    public Integer getRelatedid() {
        return relatedid;
    }

    public void setRelatedid(Integer relatedid) {
        this.relatedid = relatedid;
    }

    public Integer getApplyuserid() {
        return applyuserid;
    }

    public void setApplyuserid(Integer applyuserid) {
        this.applyuserid = applyuserid;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getApplyreason() {
        return applyreason;
    }

    public void setApplyreason(String applyreason) {
        this.applyreason = applyreason;
    }

    public Integer getAudituserid() {
        return audituserid;
    }

    public void setAudituserid(Integer audituserid) {
        this.audituserid = audituserid;
    }

    public String getAudittime() {
        return audittime;
    }

    public void setAudittime(String audittime) {
        this.audittime = audittime;
    }

    public String getAuditopinion() {
        return auditopinion;
    }

    public void setAuditopinion(String auditopinion) {
        this.auditopinion = auditopinion;
    }

    public Integer getApplystatus() {
        return applystatus;
    }

    public void setApplystatus(Integer applystatus) {
        this.applystatus = applystatus;
    }

    @Override
    public String toString() {
        return "AudApply{" +
                "applyid=" + applyid +
                ", applytype='" + applytype + '\'' +
                ", relatedid=" + relatedid +
                ", applyuserid=" + applyuserid +
                ", applytime='" + applytime + '\'' +
                ", applyreason='" + applyreason + '\'' +
                ", audituserid=" + audituserid +
                ", audittime='" + audittime + '\'' +
                ", auditopinion='" + auditopinion + '\'' +
                ", applystatus=" + applystatus +
                '}';
    }
}
package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerScoreschangelog {

    private Integer logid;


    private Integer performanceid;


    private BigDecimal oldscores;


    private BigDecimal newscores;


    private String description;


    private String reason;


    private Integer userid;

    private String time;

    private String realname;

    public Integer getLogid() {
        return logid;
    }

    public void setLogid(Integer logid) {
        this.logid = logid;
    }

    public Integer getPerformanceid() {
        return performanceid;
    }

    public void setPerformanceid(Integer performanceid) {
        this.performanceid = performanceid;
    }

    public BigDecimal getOldscores() {
        return oldscores;
    }

    public void setOldscores(BigDecimal oldscores) {
        this.oldscores = oldscores;
    }

    public BigDecimal getNewscores() {
        return newscores;
    }

    public void setNewscores(BigDecimal newscores) {
        this.newscores = newscores;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    public String toString() {
        return "PerScoreschangelog{" +
                "logid=" + logid +
                ", performanceid=" + performanceid +
                ", oldscores=" + oldscores +
                ", newscores=" + newscores +
                ", description='" + description + '\'' +
                ", reason='" + reason + '\'' +
                ", userid=" + userid +
                ", time='" + time + '\'' +
                '}';
    }
}
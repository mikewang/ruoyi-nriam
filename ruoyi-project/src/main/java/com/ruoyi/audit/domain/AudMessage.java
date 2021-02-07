package com.ruoyi.audit.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class AudMessage extends BaseEntity {

    private Integer messageid;

    private String messagetitle;

    private String messagecontent;

    private String messagetime;

    private Integer touserid;

    private String relatedsheettype;

    private Integer relatedsheetid;

    private String processedtime;

    private Boolean ifprocessed;

    public Integer getMessageid() {
        return messageid;
    }

    public void setMessageid(Integer messageid) {
        this.messageid = messageid;
    }

    public String getMessagetitle() {
        return messagetitle;
    }

    public void setMessagetitle(String messagetitle) {
        this.messagetitle = messagetitle;
    }

    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }

    public Integer getTouserid() {
        return touserid;
    }

    public void setTouserid(Integer touserid) {
        this.touserid = touserid;
    }

    public String getRelatedsheettype() {
        return relatedsheettype;
    }

    public void setRelatedsheettype(String relatedsheettype) {
        this.relatedsheettype = relatedsheettype;
    }

    public Integer getRelatedsheetid() {
        return relatedsheetid;
    }

    public void setRelatedsheetid(Integer relatedsheetid) {
        this.relatedsheetid = relatedsheetid;
    }

    public String getProcessedtime() {
        return processedtime;
    }

    public void setProcessedtime(String processedtime) {
        this.processedtime = processedtime;
    }

    public Boolean getIfprocessed() {
        return ifprocessed;
    }

    public void setIfprocessed(Boolean ifprocessed) {
        this.ifprocessed = ifprocessed;
    }

    @Override
    public String toString() {
        return "AudMessage{" +
                "messageid=" + messageid +
                ", messagetitle='" + messagetitle + '\'' +
                ", messagecontent='" + messagecontent + '\'' +
                ", messagetime='" + messagetime + '\'' +
                ", touserid=" + touserid +
                ", relatedsheettype='" + relatedsheettype + '\'' +
                ", relatedsheetid=" + relatedsheetid +
                ", processedtime='" + processedtime + '\'' +
                ", ifprocessed=" + ifprocessed +
                '}';
    }
}

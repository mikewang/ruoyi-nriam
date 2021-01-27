package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;


public class AudProjectdoc extends BaseEntity {

    private Integer projectid;

    private Integer docid;

    private String doctype;

    private Boolean ifdeleted;

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public Boolean getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Boolean ifdeleted) {
        this.ifdeleted = ifdeleted;
    }
}
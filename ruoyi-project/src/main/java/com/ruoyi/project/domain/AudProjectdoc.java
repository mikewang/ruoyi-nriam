package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.model.BasDoc;

public class AudProjectdoc extends BasDoc {

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

    @Override
    public Integer getDocid() {
        return docid;
    }

    @Override
    public void setDocid(Integer docid) {
        super.setDocid(docid);
        this.docid = docid;
    }

    @Override
    public String getDoctype() {
        return doctype;
    }

    @Override
    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public Boolean getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Boolean ifdeleted) {
        this.ifdeleted = ifdeleted;
    }

    @Override
    public String toString() {
        return "AudProjectdoc{" +
                "projectid=" + projectid +
                ", docid=" + docid +
                ", doctype='" + doctype + '\'' +
                ", ifdeleted=" + ifdeleted +
                '}';
    }
}
package com.ruoyi.common.core.domain.model;

public class BasDoc {

    private Integer docid;

    private String docname;

    private String relativepath;

    private String attachtotype;

    private Integer relatedid;

    private String doctype;

    public String getUrl() {
        String url = relativepath + "/" + docname;
        return url;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getRelativepath() {
        return relativepath;
    }

    public void setRelativepath(String relativepath) {
        this.relativepath = relativepath;
    }

    public String getAttachtotype() {
        return attachtotype;
    }

    public void setAttachtotype(String attachtotype) {
        this.attachtotype = attachtotype;
    }

    public Integer getRelatedid() {
        return relatedid;
    }

    public void setRelatedid(Integer relatedid) {
        this.relatedid = relatedid;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    @Override
    public String toString() {
        return "BasDoc{" +
                "docid=" + docid +
                ", docname='" + docname + '\'' +
                ", relativepath='" + relativepath + '\'' +
                ", attachtotype='" + attachtotype + '\'' +
                ", relatedid=" + relatedid +
                ", doctype='" + doctype + '\'' +
                '}';
    }
}
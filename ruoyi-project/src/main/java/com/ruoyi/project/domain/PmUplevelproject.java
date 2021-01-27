package com.ruoyi.project.domain;

import java.math.BigDecimal;

public class PmUplevelproject {

    private Integer infoid;


    private Integer projectid;


    private String subjectname;


    private String manageorganization;


    private String manager;


    private BigDecimal funds;


    private Integer subjecttype;

    public Integer getInfoid() {
        return infoid;
    }

    public void setInfoid(Integer infoid) {
        this.infoid = infoid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getManageorganization() {
        return manageorganization;
    }

    public void setManageorganization(String manageorganization) {
        this.manageorganization = manageorganization;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public BigDecimal getFunds() {
        return funds;
    }

    public void setFunds(BigDecimal funds) {
        this.funds = funds;
    }

    public Integer getSubjecttype() {
        return subjecttype;
    }

    public void setSubjecttype(Integer subjecttype) {
        this.subjecttype = subjecttype;
    }
}
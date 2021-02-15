package com.ruoyi.sheet.domain;


import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class AudBudgetpayRecordQuery extends BaseEntity {
    private String sheettype;
    private Integer projectid;
    private List<Integer> supplieridList;

    public String getSheettype() {
        return sheettype;
    }

    public void setSheettype(String sheettype) {
        this.sheettype = sheettype;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public List<Integer> getSupplieridList() {
        return supplieridList;
    }

    public void setSupplieridList(List<Integer> supplieridList) {
        this.supplieridList = supplieridList;
    }

    @Override
    public String toString() {
        return "AudBudgetpayRecordQuery{" +
                "sheettype='" + sheettype + '\'' +
                ", projectid=" + projectid +
                ", supplieridList=" + supplieridList +
                '}';
    }
}
package com.ruoyi.contract.domain;

import com.ruoyi.common.core.domain.model.BasDoc;

public class AudContractdoc extends BasDoc {

    private Integer contractid;

    private Integer docid;

    private Integer ifdeleted;

    public Integer getContractid() {
        return contractid;
    }

    public void setContractid(Integer contractid) {
        this.contractid = contractid;
    }

    @Override
    public Integer getDocid() {
        return docid;
    }

    @Override
    public void setDocid(Integer docid) {
        this.docid = docid;
    }

    public Integer getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Integer ifdeleted) {
        this.ifdeleted = ifdeleted;
    }

    @Override
    public String toString() {
        return "AudContractdoc{" +
                "contractid=" + contractid +
                ", docid=" + docid +
                ", ifdeleted=" + ifdeleted +
                '}';
    }
}
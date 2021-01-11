package com.ruoyi.logis.domain;

import java.util.Date;

public class LogisContract {

    private Long contractId;


    private Long userId;


    private String contractNo;


    private String contractName;


    private String contractFile;


    private String status;

    private Date created;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "LogisContract{" +
                "contractId=" + contractId +
                ", userId=" + userId +
                ", contractNo='" + contractNo + '\'' +
                ", contractName='" + contractName + '\'' +
                ", contractFile='" + contractFile + '\'' +
                ", status='" + status + '\'' +
                ", created=" + created +
                '}';
    }
}
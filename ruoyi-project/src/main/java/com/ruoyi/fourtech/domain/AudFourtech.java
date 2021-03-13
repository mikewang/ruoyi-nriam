package com.ruoyi.fourtech.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.contract.domain.AudContractdoc;
import com.ruoyi.sheet.domain.AudSheet;

import java.math.BigDecimal;
import java.util.List;

public class AudFourtech extends BaseEntity {

    private Integer fourtechid;


    private String fourtechcode;


    private String fourtechname;


    private String fourtechtype;

    private Integer managerid;


    private Integer sheetuserid;


    private String sheettime;


    private Integer organizationid;


    private Integer teamid;


    private BigDecimal fourtechmoney;


    private String daxie;


    private String coperationunit;

    private String begindate;


    private String enddate;

    private String passtime;


    private Boolean iftemplate;


    private Integer sheetstatus;

    private String sheetuseridlinktext;
    private String manageridlinktext;
    private String teamidlinktext;
    private String organizationidlinktext;
    private String sheetstatuslinktext;

    private String fourtechDateRange;

    private List<AudContractdoc> contractdocList;

    // 附加功能，审批使用
    private Integer operateCode = 1;
    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;
    private String audittype;





    public Integer getFourtechid() {
        return fourtechid;
    }

    public void setFourtechid(Integer fourtechid) {
        this.fourtechid = fourtechid;
    }

    public String getFourtechcode() {
        return fourtechcode;
    }

    public void setFourtechcode(String fourtechcode) {
        this.fourtechcode = fourtechcode;
    }

    public String getFourtechname() {
        return fourtechname;
    }

    public void setFourtechname(String fourtechname) {
        this.fourtechname = fourtechname;
    }

    public String getFourtechtype() {
        return fourtechtype;
    }

    public void setFourtechtype(String fourtechtype) {
        this.fourtechtype = fourtechtype;
    }

    public Integer getManagerid() {
        return managerid;
    }

    public void setManagerid(Integer managerid) {
        this.managerid = managerid;
    }

    public Integer getSheetuserid() {
        return sheetuserid;
    }

    public void setSheetuserid(Integer sheetuserid) {
        this.sheetuserid = sheetuserid;
    }

    public String getSheettime() {
        return sheettime;
    }

    public void setSheettime(String sheettime) {
        this.sheettime = sheettime;
    }

    public Integer getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(Integer organizationid) {
        this.organizationid = organizationid;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public BigDecimal getFourtechmoney() {
        return fourtechmoney;
    }

    public void setFourtechmoney(BigDecimal fourtechmoney) {
        this.fourtechmoney = fourtechmoney;
    }

    public String getDaxie() {
        return daxie;
    }

    public void setDaxie(String daxie) {
        this.daxie = daxie;
    }

    public String getCoperationunit() {
        return coperationunit;
    }

    public void setCoperationunit(String coperationunit) {
        this.coperationunit = coperationunit;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    public Boolean getIftemplate() {
        return iftemplate;
    }

    public void setIftemplate(Boolean iftemplate) {
        this.iftemplate = iftemplate;
    }

    public Integer getSheetstatus() {
        return sheetstatus;
    }

    public void setSheetstatus(Integer sheetstatus) {
        this.sheetstatus = sheetstatus;
    }

    public String getSheetuseridlinktext() {
        return sheetuseridlinktext;
    }

    public void setSheetuseridlinktext(String sheetuseridlinktext) {
        this.sheetuseridlinktext = sheetuseridlinktext;
    }

    public String getManageridlinktext() {
        return manageridlinktext;
    }

    public void setManageridlinktext(String manageridlinktext) {
        this.manageridlinktext = manageridlinktext;
    }

    public String getTeamidlinktext() {
        return teamidlinktext;
    }

    public void setTeamidlinktext(String teamidlinktext) {
        this.teamidlinktext = teamidlinktext;
    }

    public String getOrganizationidlinktext() {
        return organizationidlinktext;
    }

    public void setOrganizationidlinktext(String organizationidlinktext) {
        this.organizationidlinktext = organizationidlinktext;
    }

    public String getSheetstatuslinktext() {
        return sheetstatuslinktext;
    }

    public void setSheetstatuslinktext(String sheetstatuslinktext) {
        this.sheetstatuslinktext = sheetstatuslinktext;
    }

    public String getFourtechDateRange() {

        if (fourtechDateRange == null) {
            return begindate + "至" + enddate;
        }
        else {
            return fourtechDateRange;
        }

    }

    public void setFourtechDateRange(String fourtechDateRange) {
        this.fourtechDateRange = fourtechDateRange;
    }

    public List<AudContractdoc> getContractdocList() {
        return contractdocList;
    }

    public void setContractdocList(List<AudContractdoc> contractdocList) {
        this.contractdocList = contractdocList;
    }

    public Integer getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(Integer operateCode) {
        this.operateCode = operateCode;
    }

    public Integer getConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(Integer confirmResult) {
        this.confirmResult = confirmResult;
    }

    public String getConfirmNote() {
        return confirmNote;
    }

    public void setConfirmNote(String confirmNote) {
        this.confirmNote = confirmNote;
    }

    public Integer getConfirmUserid() {
        return confirmUserid;
    }

    public void setConfirmUserid(Integer confirmUserid) {
        this.confirmUserid = confirmUserid;
    }

    public String getAudittype() {
        return audittype;
    }

    public void setAudittype(String audittype) {
        this.audittype = audittype;
    }

    @Override
    public String toString() {
        return "AudFourtech{" +
                "fourtechid=" + fourtechid +
                ", fourtechcode='" + fourtechcode + '\'' +
                ", fourtechname='" + fourtechname + '\'' +
                ", fourtechtype='" + fourtechtype + '\'' +
                ", managerid=" + managerid +
                ", sheetuserid=" + sheetuserid +
                ", sheettime='" + sheettime + '\'' +
                ", organizationid=" + organizationid +
                ", teamid=" + teamid +
                ", fourtechmoney=" + fourtechmoney +
                ", daxie='" + daxie + '\'' +
                ", coperationunit='" + coperationunit + '\'' +
                ", begindate='" + begindate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", passtime='" + passtime + '\'' +
                ", iftemplate=" + iftemplate +
                ", sheetstatus=" + sheetstatus +
                ", sheetuseridlinktext='" + sheetuseridlinktext + '\'' +
                ", manageridlinktext='" + manageridlinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", organizationidlinktext='" + organizationidlinktext + '\'' +
                ", sheetstatuslinktext='" + sheetstatuslinktext + '\'' +
                '}';
    }
}
package com.ruoyi.sheet.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.project.domain.DocFile;

import java.util.HashMap;
import java.util.List;



public class SrmSupplier extends BaseEntity {

    private Integer supplierid;

    private String suppliername;
    private String suppliershortname;
    private String address;
    private Integer character;
    private Integer type;
    private String website;
    private String person1name;
    private String person1phone1;
    private String person1phone2;
    private String person1email;
    private String person2name;
    private String person2phone1;
    private String person2phone2;
    private String person2email;
    private Integer supplierlevel;
    private String bankname;
    private String banknumber;

    private String organizationcode;

    private String taxcode;

    private String manager;
    private String memo;
    private Integer createuserid;

    private Boolean ifaudited;
    private Boolean ifdeleted;

    private String charactername;
    private String typename;
    private String levelname;

    private String createUserIDLinkText;
    private String characterLinkText;
    private String orgImgIdText;

    private Integer orgImgId;
    private List<DocFile> docList;


    public Integer getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(Integer supplierid) {
        this.supplierid = supplierid;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSuppliershortname() {
        return suppliershortname;
    }

    public void setSuppliershortname(String suppliershortname) {
        this.suppliershortname = suppliershortname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCharacter() {
        return character;
    }

    public void setCharacter(Integer character) {
        this.character = character;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPerson1name() {
        return person1name;
    }

    public void setPerson1name(String person1name) {
        this.person1name = person1name;
    }

    public String getPerson1phone1() {
        return person1phone1;
    }

    public void setPerson1phone1(String person1phone1) {
        this.person1phone1 = person1phone1;
    }

    public String getPerson1phone2() {
        return person1phone2;
    }

    public void setPerson1phone2(String person1phone2) {
        this.person1phone2 = person1phone2;
    }

    public String getPerson1email() {
        return person1email;
    }

    public void setPerson1email(String person1email) {
        this.person1email = person1email;
    }

    public String getPerson2name() {
        return person2name;
    }

    public void setPerson2name(String person2name) {
        this.person2name = person2name;
    }

    public String getPerson2phone1() {
        return person2phone1;
    }

    public void setPerson2phone1(String person2phone1) {
        this.person2phone1 = person2phone1;
    }

    public String getPerson2phone2() {
        return person2phone2;
    }

    public void setPerson2phone2(String person2phone2) {
        this.person2phone2 = person2phone2;
    }

    public String getPerson2email() {
        return person2email;
    }

    public void setPerson2email(String person2email) {
        this.person2email = person2email;
    }

    public Integer getSupplierlevel() {
        return supplierlevel;
    }

    public void setSupplierlevel(Integer supplierlevel) {
        this.supplierlevel = supplierlevel;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBanknumber() {
        return banknumber;
    }

    public void setBanknumber(String banknumber) {
        this.banknumber = banknumber;
    }

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(Integer createuserid) {
        this.createuserid = createuserid;
    }

    public Boolean getIfaudited() {
        return ifaudited;
    }

    public void setIfaudited(Boolean ifaudited) {
        this.ifaudited = ifaudited;
    }

    public Boolean getIfdeleted() {
        return ifdeleted;
    }

    public void setIfdeleted(Boolean ifdeleted) {
        this.ifdeleted = ifdeleted;
    }

    public String getCharactername() {
        return charactername;
    }

    public void setCharactername(String charactername) {
        this.charactername = charactername;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }


    public String getCreateUserIDLinkText() {
        return createUserIDLinkText;
    }

    public void setCreateUserIDLinkText(String createUserIDLinkText) {
        this.createUserIDLinkText = createUserIDLinkText;
    }

    public String getCharacterLinkText() {
        return characterLinkText;
    }

    public void setCharacterLinkText(String characterLinkText) {
        this.characterLinkText = characterLinkText;
    }

    public String getOrgImgIdText() {
        return orgImgIdText;
    }

    public void setOrgImgIdText(String orgImgIdText) {
        this.orgImgIdText = orgImgIdText;
    }

    public Integer getOrgImgId() {
        return orgImgId;
    }

    public void setOrgImgId(Integer orgImgId) {
        this.orgImgId = orgImgId;
    }

    public List<DocFile> getDocList() {
        return docList;
    }

    public void setDocList(List<DocFile> docList) {
        this.docList = docList;
    }

    @Override
    public String toString() {
        return "SrmSupplier{" +
                "supplierid=" + supplierid +
                ", suppliername='" + suppliername + '\'' +
                ", suppliershortname='" + suppliershortname + '\'' +
                ", address='" + address + '\'' +
                ", character=" + character +
                ", type=" + type +
                ", website='" + website + '\'' +
                ", person1name='" + person1name + '\'' +
                ", person1phone1='" + person1phone1 + '\'' +
                ", person1phone2='" + person1phone2 + '\'' +
                ", person1email='" + person1email + '\'' +
                ", person2name='" + person2name + '\'' +
                ", person2phone1='" + person2phone1 + '\'' +
                ", person2phone2='" + person2phone2 + '\'' +
                ", person2email='" + person2email + '\'' +
                ", supplierlevel=" + supplierlevel +
                ", bankname='" + bankname + '\'' +
                ", banknumber='" + banknumber + '\'' +
                ", organizationcode='" + organizationcode + '\'' +
                ", taxcode='" + taxcode + '\'' +
                ", manager='" + manager + '\'' +
                ", memo='" + memo + '\'' +
                ", createuserid=" + createuserid +
                ", ifaudited=" + ifaudited +
                ", ifdeleted=" + ifdeleted +
                ", charactername='" + charactername + '\'' +
                ", typename='" + typename + '\'' +
                ", levelname='" + levelname + '\'' +
                ", createUserIDLinkText='" + createUserIDLinkText + '\'' +
                ", characterLinkText='" + characterLinkText + '\'' +
                ", orgImgIdText='" + orgImgIdText + '\'' +
                ", orgImgId=" + orgImgId +
                ", docList=" + docList +
                '}';
    }
}
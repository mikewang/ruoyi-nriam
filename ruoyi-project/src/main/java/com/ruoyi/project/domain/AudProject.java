package com.ruoyi.project.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.performance.domain.PerFund;

import java.math.BigDecimal;
import java.util.List;

public class AudProject extends BaseEntity {

    private Integer projectid;

    private String projectname;


    private String projectcode;


    private String subjectcode;


    private BigDecimal projectfunds;


    private BigDecimal financefunds;


    private BigDecimal canusefunds;


    private String projectbegindate;


    private String projectenddate;


    private Integer projecttype;


    private Integer projectmanagerid;


    private Integer organizationid;


    private Integer teamid;


    private String memo;

    private Integer jointype;

    private Integer createuserid;

    private Boolean ifaudited;

    private Boolean ifacceptancefull;

    private Integer status;

    private String projecttypelinktext;
    private String dictionaryordernum;
    private String projectmanageridlinktext;
    private String organizationidlinktext;
    private String statuslinktext;
    private String teamidlinktext;
    private String createuseridlinktext;

    private List<PmProjectjoinorganization> projectjoinorganizationlist;
    private PmUplevelproject uplevelproject;
    private List<Integer> projectmemberUserIdList;
    private List<AudProjectdoc> projectdocList;

    //忘记这个 list 是做什么用的了。哦。项目的各种状态过滤用的，也是附加操作的属性，
    private List<Integer> statusList;


    // 附加操作的查询使用的属性
    private String projectyear;

    private String projectDateRange;

    private Integer projectDateRangeColor;

    private Integer projectColor = ProjectColor.Default.getCode();

    // 前端提交到后端的修改对象的操作代码，1,暂存，2，提交审核，3，撤回新建中。 4 ，删除， 5，新建审核
    // DaiQueRen(40, "新建待审核"), ZaiYan(41, "在研"), YiJieTi(42, "已完成"), YiShanChu(43, "已删除"), BuTongGuo(44, "新建审核不通过"), JieTiDaiQueRen(45, "验收待审核"), JietiBuTongGuo(46, "验收审核不通过"), XinJianZhong(48, "新建中");
    private  Integer operateCode = 1;

    private String confirmSubjectcode; // 二次修改经费编码
    private Integer confirmResult;
    private String confirmNote;
    private Integer confirmUserid;

    // 经费确认模块
    private PerFund fundreport;

    public PerFund getFundreport() {
        return fundreport;
    }

    public void setFundreport(PerFund fundreport) {
        this.fundreport = fundreport;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectcode() {
        return projectcode;
    }

    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }

    public String getSubjectcode() {
        return subjectcode;
    }

    public void setSubjectcode(String subjectcode) {
        this.subjectcode = subjectcode;
    }

    public BigDecimal getProjectfunds() {
        return projectfunds;
    }

    public void setProjectfunds(BigDecimal projectfunds) {
        this.projectfunds = projectfunds;
    }

    public BigDecimal getFinancefunds() {
        return financefunds;
    }

    public void setFinancefunds(BigDecimal financefunds) {
        this.financefunds = financefunds;
    }

    public BigDecimal getCanusefunds() {
        return canusefunds;
    }

    public void setCanusefunds(BigDecimal canusefunds) {
        this.canusefunds = canusefunds;
    }

    public String getProjectbegindate() {
        return projectbegindate;
    }

    public void setProjectbegindate(String projectbegindate) {
        this.projectbegindate = projectbegindate;
    }

    public String getProjectenddate() {
        return projectenddate;
    }

    public void setProjectenddate(String projectenddate) {
        this.projectenddate = projectenddate;
    }

    public Integer getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(Integer projecttype) {
        this.projecttype = projecttype;
    }

    public Integer getProjectmanagerid() {
        return projectmanagerid;
    }

    public void setProjectmanagerid(Integer projectmanagerid) {
        this.projectmanagerid = projectmanagerid;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getJointype() {
        return jointype;
    }

    public void setJointype(Integer jointype) {
        this.jointype = jointype;
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

    public Boolean getIfacceptancefull() {
        return ifacceptancefull;
    }

    public void setIfacceptancefull(Boolean ifacceptancefull) {
        this.ifacceptancefull = ifacceptancefull;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProjecttypelinktext() {
        return projecttypelinktext;
    }

    public void setProjecttypelinktext(String projecttypelinktext) {
        this.projecttypelinktext = projecttypelinktext;
    }

    public String getDictionaryordernum() {
        return dictionaryordernum;
    }

    public void setDictionaryordernum(String dictionaryordernum) {
        this.dictionaryordernum = dictionaryordernum;
    }

    public String getProjectmanageridlinktext() {
        return projectmanageridlinktext;
    }

    public void setProjectmanageridlinktext(String projectmanageridlinktext) {
        this.projectmanageridlinktext = projectmanageridlinktext;
    }

    public String getOrganizationidlinktext() {
        return organizationidlinktext;
    }

    public void setOrganizationidlinktext(String organizationidlinktext) {
        this.organizationidlinktext = organizationidlinktext;
    }

    public String getStatuslinktext() {
        return statuslinktext;
    }

    public void setStatuslinktext(String statuslinktext) {
        this.statuslinktext = statuslinktext;
    }

    public String getTeamidlinktext() {
        return teamidlinktext;
    }

    public void setTeamidlinktext(String teamidlinktext) {
        this.teamidlinktext = teamidlinktext;
    }

    public String getCreateuseridlinktext() {
        return createuseridlinktext;
    }

    public void setCreateuseridlinktext(String createuseridlinktext) {
        this.createuseridlinktext = createuseridlinktext;
    }

    public List<PmProjectjoinorganization> getProjectjoinorganizationlist() {
        return projectjoinorganizationlist;
    }

    public void setProjectjoinorganizationlist(List<PmProjectjoinorganization> projectjoinorganizationlist) {
        this.projectjoinorganizationlist = projectjoinorganizationlist;
    }

    public PmUplevelproject getUplevelproject() {
        return uplevelproject;
    }

    public void setUplevelproject(PmUplevelproject uplevelproject) {
        this.uplevelproject = uplevelproject;
    }

    public List<Integer> getProjectmemberUserIdList() {
        return projectmemberUserIdList;
    }

    public void setProjectmemberUserIdList(List<Integer> projectmemberUserIdList) {
        this.projectmemberUserIdList = projectmemberUserIdList;
    }

    public List<AudProjectdoc> getProjectdocList() {
        return projectdocList;
    }

    public void setProjectdocList(List<AudProjectdoc> projectdocList) {
        this.projectdocList = projectdocList;
    }

    public List<Integer>  getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer>  statusList) {
        this.statusList = statusList;
    }


    public String getProjectyear() {
        return projectyear;
    }

    public void setProjectyear(String projectyear) {
        this.projectyear = projectyear;
    }

    public String getProjectDateRange() {
        if (projectDateRange == null) {
            return projectbegindate + "至" + projectenddate;
        }
        else {
            return projectDateRange;
        }
    }

    public void setProjectDateRange(String projectDateRange) {
        this.projectDateRange = projectDateRange;
    }

    public Integer getProjectDateRangeColor() {
        return projectDateRangeColor;
    }

    public void setProjectDateRangeColor(Integer projectDateRangeColor) {
        this.projectDateRangeColor = projectDateRangeColor;
    }

    public Integer getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(Integer projectColor) {
        this.projectColor = projectColor;
    }

    public Integer getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(Integer operateCode) {
        this.operateCode = operateCode;
    }

    public String getConfirmSubjectcode() {
        return confirmSubjectcode;
    }

    public void setConfirmSubjectcode(String confirmSubjectcode) {
        this.confirmSubjectcode = confirmSubjectcode;
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


    @Override
    public String toString() {
        return "AudProject{" +
                "projectid=" + projectid +
                ", projectname='" + projectname + '\'' +
                ", projectcode='" + projectcode + '\'' +
                ", subjectcode='" + subjectcode + '\'' +
                ", projectfunds=" + projectfunds +
                ", financefunds=" + financefunds +
                ", canusefunds=" + canusefunds +
                ", projectbegindate='" + projectbegindate + '\'' +
                ", projectenddate='" + projectenddate + '\'' +
                ", projecttype=" + projecttype +
                ", projectmanagerid=" + projectmanagerid +
                ", organizationid=" + organizationid +
                ", teamid=" + teamid +
                ", memo='" + memo + '\'' +
                ", jointype=" + jointype +
                ", createuserid=" + createuserid +
                ", ifaudited=" + ifaudited +
                ", ifacceptancefull=" + ifacceptancefull +
                ", status=" + status +
                ", projecttypelinktext='" + projecttypelinktext + '\'' +
                ", dictionaryordernum='" + dictionaryordernum + '\'' +
                ", projectmanageridlinktext='" + projectmanageridlinktext + '\'' +
                ", organizationidlinktext='" + organizationidlinktext + '\'' +
                ", statuslinktext='" + statuslinktext + '\'' +
                ", teamidlinktext='" + teamidlinktext + '\'' +
                ", createuseridlinktext='" + createuseridlinktext + '\'' +
                ", projectjoinorganizationlist=" + projectjoinorganizationlist +
                ", uplevelproject=" + uplevelproject +
                ", projectmemberUserIdList=" + projectmemberUserIdList +
                ", projectdocList=" + projectdocList +
                ", statusList=" + statusList +
                ", projectyear='" + projectyear + '\'' +
                ", projectDateRange='" + projectDateRange + '\'' +
                ", projectDateRangeColor=" + projectDateRangeColor +
                ", projectColor=" + projectColor +
                ", operateCode=" + operateCode +
                ", confirmSubjectcode='" + confirmSubjectcode + '\'' +
                ", confirmResult=" + confirmResult +
                ", confirmNote='" + confirmNote + '\'' +
                ", confirmUserid=" + confirmUserid +
                ", fundreport=" + fundreport +
                '}';
    }
}
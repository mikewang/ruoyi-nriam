package com.ruoyi.project.service;

import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.domain.*;
import com.ruoyi.project.mapper.*;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AudProjectService {
    private static final Logger log = LoggerFactory.getLogger(AudProjectService.class);

    @Resource
    private AudProjectMapper audProjectMapper;

    @Resource
    private PmProjectjoinorganizationMapper joinorganizationMapper;

    @Resource
    private PmProjectmemberMapper projectmemberMapper;

    @Resource
    private AudProjectdocMapper projectdocMapper;

    @Resource
    private PmUplevelprojectMapper uplevelprojectMapper;

    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;


    private   List<AudProject> selectProjectList(AudProject statusProject) {

        List<AudProject> projectList = audProjectMapper.selectAudProjectList(statusProject);

        for (AudProject prj : projectList) {

            List<Integer> list = new ArrayList<>();
            list.add(ProjectStatus.DaiQueRen.getCode());
            list.add(ProjectStatus.JieTiDaiQueRen.getCode());
            list.add(ProjectStatus.XinJianZhong.getCode());
            List<Integer>  list2 = new ArrayList<>();
            list2.add(ProjectStatus.BuTongGuo.getCode());
            list2.add(ProjectStatus.JietiBuTongGuo.getCode());

            if (list.contains(prj.toString()) || list2.contains(prj.toString())) {

                if (list.contains(prj.getStatus())) {
                    prj.setProjectColor(ProjectColor.Red.getCode());
                }

                if (list2.contains(prj.getStatus())) {
                    prj.setProjectColor(ProjectColor.Green.getCode());
                }

                log.debug("prj.getProjectenddate() is " + prj.getProjectenddate());
                java.util.Date ended = DateUtils.getNowDate();

                try {
                    ended = DateUtils.parseDate(prj.getProjectenddate());
                } catch (Exception e) {

                }

                long days = (ended.getTime() - DateUtils.getNowDate().getTime()) / (1000 * 24 * 60 * 60);

                if (days < 90) {
                    prj.setProjectDateRange(prj.getProjectbegindate() + "至" + prj.getProjectenddate() + "(剩余 " + String.valueOf(days) + "天)");
                    prj.setProjectColor(ProjectColor.Red.getCode());
                } else {
                    prj.setProjectDateRange(prj.getProjectbegindate() + "至" + prj.getProjectenddate());
                }
            }
            else {
                prj.setProjectDateRange(prj.getProjectbegindate() + "至" + prj.getProjectenddate());
            }

        }

        return projectList;
    }

    public List<AudProject> selectProjectNewList(AudProject project) {

//
//        <!--  + " and ( a.Status = " + (int)Entity.Enums.ProjectStatus.DaiQueRen    //待确认-->
//                <!--  + " or a.Status = " + (int)Entity.Enums.ProjectStatus.ZaiYan           //在研-->
//                <!--  + " or a.Status = " + (int)Entity.Enums.ProjectStatus.JieTiDaiQueRen           //结题待确认-->
//                <!--  + " or a.Status = " + (int)Entity.Enums.ProjectStatus.JietiBuTongGuo      //结题不通过-->
//                <!--  + " or a.Status = " + (int)Entity.Enums.ProjectStatus.XinJianZhong            //新建中-->
//                <!--  + " or a.Status = " + (int)Entity.Enums.ProjectStatus.BuTongGuo + " ) ";-->

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatus.DaiQueRen.getCode());
        statusList.add(ProjectStatus.ZaiYan.getCode());
        statusList.add(ProjectStatus.JieTiDaiQueRen.getCode());
        statusList.add(ProjectStatus.JietiBuTongGuo.getCode());
        statusList.add(ProjectStatus.XinJianZhong.getCode());
        statusList.add(ProjectStatus.BuTongGuo.getCode());

        project.setStatusList(statusList);

        return selectProjectList(project);


    }


    public List<AudProject> selectProjectToconfirmList(AudProject project) {
        //+ " and a.Status = " + (int)Entity.Enums.ProjectStatus.DaiQueRen

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatus.DaiQueRen.getCode());
        project.setStatusList(statusList);

        return selectProjectList(project);

    }


    public List<AudProject> selectProjectToacceptanceconfirmList(AudProject project) {
        //+ " and a.Status = " + (int)Entity.Enums.ProjectStatus.JieTiDaiQueRen

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatus.JieTiDaiQueRen.getCode());
        project.setStatusList(statusList);

        return selectProjectList(project);

    }


    public List<AudProject> selectProjectToaddacceptanceList(AudProject project) {
        //+ " and a.Status = " + (int)Entity.Enums.ProjectStatus.JieTiDaiQueRen

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatus.YiJieTi.getCode());
        project.setStatusList(statusList);
        project.setIfacceptancefull(false);

        return selectProjectList(project);

    }


    public List<AudProject> selectProjectTofinishedList(AudProject project) {
        //+ " and a.Status = " + (int)Entity.Enums.ProjectStatus.JieTiDaiQueRen

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatus.YiJieTi.getCode());
        project.setStatusList(statusList);
        project.setIfacceptancefull(true);
        return selectProjectList(project);

    }


    public AudProject selectProjectById(Integer projectId) {

        return audProjectMapper.selectProjectById(projectId);
    }

    public Integer queryIfDuplicate(AudProject project) {
        return audProjectMapper.queryIfDuplicate(project);
    }


    public void updateProjectDetail(AudProject project) {

        Integer projectId = project.getProjectid();

        if (project.getJointype() != null) {
            if (project.getJointype() == 1) {
                List<PmProjectjoinorganization> joinList = project.getProjectJoinOrganizationList();
                List<Integer> ids = new ArrayList<>();
                for (PmProjectjoinorganization s : joinList) {
                    s.setProjectid(projectId.toString());

                    if (s.getJoid() != null) {
                        ids.add(s.getJoid());
                    }
                }

                PmProjectjoinorganization query = new PmProjectjoinorganization();
                query.setProjectid(projectId.toString());
                List<PmProjectjoinorganization> joinList2 = joinorganizationMapper.selectProjectjoinorganizationList(query);

                List<Integer> ids2 = new ArrayList<>();
                for (PmProjectjoinorganization s : joinList2) {
                    ids2.add(s.getJoid());
                }

                // 数据库里保存的比现在提交的多的joid. 删除。
                ids2.removeAll(ids);
                if (ids2.size() > 0) {
                    joinorganizationMapper.deleteProjectjoinorganizationByIds(ids2);
                }
                // 现在提交的数据库里保存的 多的joid. 新增或更新。
                for (PmProjectjoinorganization s : joinList) {
                    Integer joid = s.getJoid();
                    if (joid != null) {
                        joinorganizationMapper.updateProjectjoinorganization(s);
                    } else {
                        joinorganizationMapper.insertProjectjoinorganization(s);
                    }
                }
            } else if (project.getJointype() == 2) {

                PmUplevelproject uplevelProject = project.getUplevelproject();

                if (uplevelProject == null) {
                    PmUplevelproject query = new PmUplevelproject();
                    query.setProjectid(project.getProjectid());
                    uplevelprojectMapper.deleteUplevelProject(query);
                } else {
                    uplevelProject.setProjectid(projectId);

                    PmUplevelproject uplevelproject2 = uplevelprojectMapper.selectUplevelProjectByProjectid(projectId);

                    if (uplevelproject2 != null) {
                        if (uplevelProject.getInfoid() == uplevelproject2.getInfoid()) {
                            uplevelprojectMapper.updateUplevelProject(uplevelProject);
                        } else {
                            uplevelprojectMapper.deleteUplevelProject(uplevelproject2);
                            uplevelprojectMapper.insertUplevelProject(uplevelProject);
                        }
                    } else {
                        uplevelprojectMapper.insertUplevelProject(uplevelProject);
                    }
                }
            }
        }

        //同步项目员工
        projectmemberMapper.deleteProjectmemberByProjectId(projectId);

        log.debug("project.getProjectmemberList is " + project.getProjectmemberList().toString());
        for (Integer s : project.getProjectmemberList()) {
            PmProjectmember projectmember = new PmProjectmember();
            projectmember.setProjectid(projectId);
            projectmember.setUserid(s);
            projectmemberMapper.insertProjectmember(projectmember);
        }


        // 同步 项目文件。 使用merge方式。
        List<AudProjectdoc> doclist = project.getProjectdocList();

        for (AudProjectdoc s : doclist) {
            s.setProjectid(projectId);
            s.setIfdeleted(false);
        }

        log.debug("doclist is " + doclist.toString());

        if (doclist.size() > 0) {
            Integer rows = projectdocMapper.mergeProjectdoc(doclist);
            rows = projectdocMapper.sourceMergeProjectdoc(doclist);
        } else {
            AudProjectdoc query = new AudProjectdoc();
            query.setProjectid(project.getProjectid());
            Integer rows = projectdocMapper.deleteProjectdoc(query);
        }

    }


    @Transactional
    public Integer insertProject(AudProject project) {
        log.debug("insertProject is below.");

        project.setStatus(ProjectStatus.XinJianZhong.getCode());

        Integer rows = audProjectMapper.insertProject(project);

        Integer projectId = rows > 0 ? project.getProjectid() : rows;

        log.debug("insertProject rows is " + rows.toString() + " projectid is " + project.getProjectid().toString());

        if (projectId > 0) {

            log.debug("projectJoinOrganizationList is " + project.getProjectJoinOrganizationList());
            log.debug("uplevelproject is " + project.getUplevelproject());
            log.debug("projectmemberList is " + project.getProjectmemberList());
            log.debug("projectdocList is " + project.getProjectdocList());

            this.updateProjectDetail(project);

            if (project.getOperateCode() == 2) {
                project.setStatus(ProjectStatus.DaiQueRen.getCode());
                audProjectMapper.updateProjectStatus(project);

                String title = "新的项目信息待审核";
                String content = "项目：“" + project.getProjectname() + "”的信息待审核。";
                content = content + "<a href=\"/Project/ToConfirmProjectList.aspx?" + "kid=" + project.getProjectid() + "&f=todo\" target=\"_self\" >查看</a> ";
                // 下面写操作代码。

                String perms = "project:toconfirm:list";

                SysUserMenu userMenu = new SysUserMenu();
                userMenu.setPerms(perms);

                List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

                Set<Long> useridSet = new HashSet<>();
                for(SysUserMenu um : userMenuList) {
                    useridSet.add(um.getUserId());
                }

                for (Long userid : useridSet) {
                    AudMessage message = new AudMessage();
                    message.setMessagetime(DateUtils.dateTimeNow());
                    message.setMessagetitle(title);
                    message.setMessagecontent(content);
                    message.setTouserid(userid.intValue());
                    message.setRelatedsheettype("项目");
                    message.setRelatedsheetid(project.getProjectid());

                    messageMapper.insertAudMessage(message);
                }


            }
        }

        return projectId;
    }

    @Transactional
    public Integer updateProject(AudProject project) {

        Integer rows = audProjectMapper.updateProject(project);

        this.updateProjectDetail(project);

        if (project.getOperateCode() == 2) {
            project.setStatus(ProjectStatus.DaiQueRen.getCode());
            audProjectMapper.updateProjectStatus(project);

            String title = "新的项目信息待审核";
            String content = "项目：“" + project.getProjectname() + "”的信息待审核。";
            content = content + "<a href=\"/Project/ToConfirmProjectList.aspx?" + "kid=" + project.getProjectid() + "&f=todo\" target=\"_self\" >查看</a> ";

            String perms = "project:toconfirm:list";

            SysUserMenu userMenu = new SysUserMenu();
            userMenu.setPerms(perms);

            List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

            Set<Long> useridSet = new HashSet<>();
            for(SysUserMenu um : userMenuList) {
                useridSet.add(um.getUserId());
            }

            for (Long userid : useridSet) {
                AudMessage message = new AudMessage();
                message.setMessagetime(DateUtils.dateTimeNow());
                message.setMessagetitle(title);
                message.setMessagecontent(content);
                message.setTouserid(userid.intValue());
                message.setRelatedsheettype("项目");
                message.setRelatedsheetid(project.getProjectid());

                messageMapper.insertAudMessage(message);
            }

//            //发送消息
//            string title = "新的项目信息待审核";
//            string content = "项目：“" + txt_ProjectName.Text + "”的信息待审核。"
//                    + "<a href=\"/Project/ToConfirmProjectList.aspx?"
//                    + "kid=" + lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//            ProjectCommonFunc.SendMessage(title, content, "项目", lab_ProjectID.Text, ConstantParameter.MID_ProjectConfirm);
//            public static void SendMessage(string title, string content ,string type ,string kid ,string moduleID)
//            {
//                //发送通知消息(发送给具有“项目信息审核”功能的人员)
//                IPermissionManager iperm = PermissionManager.GetInstance();
//                DataSet ds = iperm.GetUserListToModule(moduleID);
//
//                //循环
//                for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
//                {
//                    AudMessage am = new AudMessage();
//                    am.MessageTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//                    am.MessageTitle = title;
//                    am.MessageContent = content;
//                    //查询消息要发送给哪个人员
//                    am.ToUserID = int.Parse(ds.Tables[0].Rows[i]["UserID"].ToString());
//                    am.RelatedSheetType = type;
//                    am.RelatedSheetID = int.Parse(kid);
//                    IAudMessageManager iamm = AudMessageManager.GetInstance();
//                    iamm.AddNew(am);
//                }
//
//                public DataSet GetUserListToModule(string moduleID)
//                {
//                    string sql = "";
//                    try
//                    {
//                        sql = " select distinct(userID) from UM_UserInRole "
//                                + " where roleID in "
//                                + " ( select RoleID from um_permission where moduleid=" + moduleID
//                                + " and permissiontype=1 ) ";
//                        DataSet ds = DBAccess.DB.ExecuteDataSet(CommandType.Text, sql);
//                        return ds;
//                    }
//                    catch (Exception exp)
//                    {
//                        LogRecorder.WriteLog("获取能够访问某个功能模块的所有用户时发生错误，执行的sql为" + sql, exp.Message);
//                        return null;
//                    }
//                }

        }

        return rows;
    }

    @Transactional
    public Integer removeProject(AudProject project) {
        log.debug("与绩效模块挂钩，后面实现。");

        project.setStatus(ProjectStatus.YiShanChu.getCode());
        Integer rows = audProjectMapper.updateProjectStatus(project);
                   /*
        *                 if (status == Entity.Enums.ProjectStatus.YiShanChu)   //如果是删除项目
                {
                    //删掉对应的绩效计分记录
                    ITeamPerformanceManager iteamper = TeamPerformanceManager.GetInstance();
                    iteamper.DeleteRecordByAchieve(ConstantParameter.CHString_IndicatorProject, projectID);
                }
        * */

        return  rows;
    }

    @Transactional
    public Integer xinjianzhongProject(AudProject project) {

        project.setStatus(ProjectStatus.XinJianZhong.getCode());
        Integer rows = audProjectMapper.updateProjectStatus(project);

        return  rows;
    }

    @Transactional
    public Integer confirmProject(AudProject project) {
        log.debug("与绩效模块挂钩，后面实现。");
        Integer rows = 0;

        if (project.getStatus() != ProjectStatus.DaiQueRen.getCode()) {
            return  rows;
        }

        if (project.getConfirmResult() == 1) {
            AudProject record = new AudProject();
            record.setProjectid(project.getProjectid());
            record.setSubjectcode(project.getSubjectcode());
            record.setStatus(ProjectStatus.ZaiYan.getCode());
            rows = audProjectMapper.updateProjectStatus(record);

            //发送消息
//            string title = "新的项目信息待审核";
//            string content = "项目：“" + txt_ProjectName.Text + "”的信息待审核。"
//                    + "<a href=\"/Project/ToConfirmProjectList.aspx?"
//                    + "kid=" + lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//            ProjectCommonFunc.SendMessage(title, content, "项目", lab_ProjectID.Text, ConstantParameter.MID_ProjectConfirm);
            //发送通知消息(发送给具有“项目信息审核”功能的人员)
//            IPermissionManager iperm = PermissionManager.GetInstance();
//            DataSet ds = iperm.GetUserListToModule(moduleID);
//
//            //循环
//            for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
//            {
//                AudMessage am = new AudMessage();
//                am.MessageTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//                am.MessageTitle = title;
//                am.MessageContent = content;
//                //查询消息要发送给哪个人员
//                am.ToUserID = int.Parse(ds.Tables[0].Rows[i]["UserID"].ToString());
//                am.RelatedSheetType = type;
//                am.RelatedSheetID = int.Parse(kid);
//                IAudMessageManager iamm = AudMessageManager.GetInstance();
//                iamm.AddNew(am);
//            }


        }
        else if (project.getConfirmResult() == 2) {
            AudProject record = new AudProject();
            record.setProjectid(project.getProjectid());
            record.setStatus(ProjectStatus.BuTongGuo.getCode());
            rows =  audProjectMapper.updateProjectStatus(record);

            Integer status = 2;      //表示不通过
            AudApply apply = new AudApply();
            apply.setApplytype("项目确认申请");
            apply.setRelatedid(project.getProjectid());
            apply.setApplyuserid(project.getCreateuserid());
            apply.setApplytime(DateUtils.dateTimeNow());
            apply.setApplystatus(status);
            apply.setAuditopinion(project.getConfirmNote());
            apply.setAudittime(DateUtils.dateTimeNow());
            apply.setAudituserid(project.getConfirmUserid());
            applyMapper.insertAudApply(apply);

        }

//

//        if (rbtl_Result.SelectedValue == "1")  //表示通过
//        {
//            //状态设置成在研
//            ipm.SetStatus(Request["kid"].ToString(), Entity.Enums.ProjectStatus.ZaiYan);
//
//            //修改经费编号
//            if (txt_SubjectCode.Text.Trim() != "")
//            {
//                ipm.UpdateSubjectCode(Request["kid"], txt_SubjectCode.Text.Trim());
//            }
//
//
//            //增加团队的评分记录 待修改
//            ITeamPerformanceManager iteamper = TeamPerformanceManager.GetInstance();
//            iteamper.AddRecord_FromProject(Request["kid"]);
//
//
//        }
//        else   //数据库里写入申请不通过的记录
//        {
//            ipm.SetStatus(Request["kid"].ToString(), Entity.Enums.ProjectStatus.BuTongGuo);
//
//            //生成确认的申请
//            IApplyManager iappM = ApplyManager.GetInstance();
//            Apply apply = new Apply();
//            apply.ApplyType = "项目确认申请";
//            apply.RelatedID = int.Parse(ProjectDetail1.lab_ProjectID.Text);
//            apply.ApplyUserID = int.Parse(ProjectDetail1.lab_CreateUserID.Text);
//            apply.ApplyTime = "2016-01-01 00:00:00";
//            string newApplyID = iappM.AddNew(apply);
//            //申请不通过
//            iappM.AuditApply(newApplyID, false, Session["CurrentUserID"].ToString(),
//                    txt_Opinion.Text, DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
//
//        }
//
//        //消除"审核人"待办事项
//        IAudMessageManager iaudms = AudMessageManager.GetInstance();
//        IPermissionManager iperm = PermissionManager.GetInstance();
//        DataSet ds = iperm.GetUserListToModule(ConstantParameter.MID_ProjectConfirm);
//        //循环
//        for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
//        {
//            iaudms.SetProcessed(ds.Tables[0].Rows[i]["UserID"].ToString(), "项目", Request["kid"].ToString());
//        }
//        ds.Dispose();
//
//        //发送通知 待修改
//        string title = "";
//        string content = "";
//        if (rbtl_Result.SelectedValue == "1")  //通过
//        {
//            title = "您提交的项目审核通过";
//            content = "项目：“" + ProjectDetail1.lab_ProjectName.Text + "”的信息已经审核通过。"
//                    + "<a href=\"/Project/ProjectList.aspx?"
//                    + "kid=" + ProjectDetail1.lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//        }
//        else
//        {
//            title = "您提交的项目审核不通过";
//            content = "项目：“" + ProjectDetail1.lab_ProjectName.Text + "”的信息审核不通过。"
//                    + "<a href=\"/Project/ProjectList.aspx?"
//                    + "kid=" + ProjectDetail1.lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//        }
//        AudMessage am = new AudMessage();
//        am.MessageTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//        am.MessageTitle = title;
//        am.MessageContent = content;
//        //发送给提交人
//        am.ToUserID = int.Parse(ProjectDetail1.lab_CreateUserID.Text);
//        am.RelatedSheetType = "项目";
//        am.RelatedSheetID = int.Parse(ProjectDetail1.lab_ProjectID.Text);
//        iaudms.AddNew(am);
//
//
//        Response.Write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"); //是为了保持页面样式
//        Response.Write("<script>alert('操作成功！');</script>"); //提示错误
//        Response.Write("<script>document.location = 'ToConfirmProjectList.aspx';</script>");

        return  rows;
    }

    @Transactional
    public Integer acceptanceconfirm(AudProject project) {

        Integer rows = 0;

        if (project.getStatus() != ProjectStatus.JieTiDaiQueRen.getCode()) {
            return  rows;
        }

        if (project.getConfirmResult() == 1) {
            AudProject record = new AudProject();
            record.setProjectid(project.getProjectid());
            record.setSubjectcode(project.getSubjectcode());
            record.setStatus(ProjectStatus.YiJieTi.getCode());
            rows = audProjectMapper.updateProjectStatus(record);

            if (project.getIfacceptancefull() == false)  //表示材料还不齐全
                {
                record.setIfacceptancefull(project.getIfacceptancefull());
                audProjectMapper.updateIfAcceptanceFull(record);
            }

        }
        else if (project.getConfirmResult() == 2) {
            AudProject record = new AudProject();
            record.setProjectid(project.getProjectid());
            record.setStatus(ProjectStatus.JietiBuTongGuo.getCode());
            rows =  audProjectMapper.updateProjectStatus(record);

            Integer status = 2;      //表示不通过
            AudApply apply = new AudApply();
            apply.setApplytype("项目结题确认申请");
            apply.setRelatedid(project.getProjectid());
            apply.setApplyuserid(project.getCreateuserid());
            apply.setApplytime(DateUtils.dateTimeNow());
            apply.setApplystatus(status);
            apply.setAuditopinion(project.getConfirmNote());
            apply.setAudittime(DateUtils.dateTimeNow());
            apply.setAudituserid(project.getConfirmUserid());
            applyMapper.insertAudApply(apply);

        }

//

//        if (rbtl_Result.SelectedValue == "1")  //表示通过
//        {
//            //状态设置成在研
//            ipm.SetStatus(Request["kid"].ToString(), Entity.Enums.ProjectStatus.ZaiYan);
//
//            //修改经费编号
//            if (txt_SubjectCode.Text.Trim() != "")
//            {
//                ipm.UpdateSubjectCode(Request["kid"], txt_SubjectCode.Text.Trim());
//            }
//
//
//            //增加团队的评分记录 待修改
//            ITeamPerformanceManager iteamper = TeamPerformanceManager.GetInstance();
//            iteamper.AddRecord_FromProject(Request["kid"]);
//
//
//        }
//        else   //数据库里写入申请不通过的记录
//        {
//            ipm.SetStatus(Request["kid"].ToString(), Entity.Enums.ProjectStatus.BuTongGuo);
//
//            //生成确认的申请
//            IApplyManager iappM = ApplyManager.GetInstance();
//            Apply apply = new Apply();
//            apply.ApplyType = "项目确认申请";
//            apply.RelatedID = int.Parse(ProjectDetail1.lab_ProjectID.Text);
//            apply.ApplyUserID = int.Parse(ProjectDetail1.lab_CreateUserID.Text);
//            apply.ApplyTime = "2016-01-01 00:00:00";
//            string newApplyID = iappM.AddNew(apply);
//            //申请不通过
//            iappM.AuditApply(newApplyID, false, Session["CurrentUserID"].ToString(),
//                    txt_Opinion.Text, DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
//
//        }
//
//        //消除"审核人"待办事项
//        IAudMessageManager iaudms = AudMessageManager.GetInstance();
//        IPermissionManager iperm = PermissionManager.GetInstance();
//        DataSet ds = iperm.GetUserListToModule(ConstantParameter.MID_ProjectConfirm);
//        //循环
//        for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
//        {
//            iaudms.SetProcessed(ds.Tables[0].Rows[i]["UserID"].ToString(), "项目", Request["kid"].ToString());
//        }
//        ds.Dispose();
//
//        //发送通知 待修改
//        string title = "";
//        string content = "";
//        if (rbtl_Result.SelectedValue == "1")  //通过
//        {
//            title = "您提交的项目审核通过";
//            content = "项目：“" + ProjectDetail1.lab_ProjectName.Text + "”的信息已经审核通过。"
//                    + "<a href=\"/Project/ProjectList.aspx?"
//                    + "kid=" + ProjectDetail1.lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//        }
//        else
//        {
//            title = "您提交的项目审核不通过";
//            content = "项目：“" + ProjectDetail1.lab_ProjectName.Text + "”的信息审核不通过。"
//                    + "<a href=\"/Project/ProjectList.aspx?"
//                    + "kid=" + ProjectDetail1.lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//        }
//        AudMessage am = new AudMessage();
//        am.MessageTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//        am.MessageTitle = title;
//        am.MessageContent = content;
//        //发送给提交人
//        am.ToUserID = int.Parse(ProjectDetail1.lab_CreateUserID.Text);
//        am.RelatedSheetType = "项目";
//        am.RelatedSheetID = int.Parse(ProjectDetail1.lab_ProjectID.Text);
//        iaudms.AddNew(am);
//
//
//        Response.Write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"); //是为了保持页面样式
//        Response.Write("<script>alert('操作成功！');</script>"); //提示错误
//        Response.Write("<script>document.location = 'ToConfirmProjectList.aspx';</script>");

        return  rows;
    }

    public AudApply selectApplyByTypeAndRelatedID(Integer projectId, Integer status) {

        AudApply apply = new AudApply();
        if (status == ProjectStatus.BuTongGuo.getCode()) {
            apply.setApplytype("项目确认申请");
        }
        else {
            apply.setApplytype("项目结题确认申请");
        }
        apply.setRelatedid(projectId);
        return applyMapper.selectApplyByTypeAndRelatedID(apply);
    }


}
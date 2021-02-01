package com.ruoyi.project.service;

import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.domain.*;
import com.ruoyi.project.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    public List<AudProject> selectAudProjectList(AudProject project) {

        List<AudProject> projectList = audProjectMapper.selectAudProjectList(project);

        for (AudProject prj : projectList) {
            prj.setProjectDateRange(prj.getProjectbegindate() + "至" + prj.getProjectenddate());

        }

        return projectList;
    }


    public List<AudProject> selectAudProjectNewList(AudProject project) {

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


        List<AudProject> projectList = audProjectMapper.selectAudProjectList(project);

        for (AudProject prj : projectList) {

            List<Integer> list = new ArrayList<>();
            list.add(ProjectStatus.DaiQueRen.getCode());
            list.add(ProjectStatus.JieTiDaiQueRen.getCode());
            list.add(ProjectStatus.XinJianZhong.getCode());

            if (list.contains(prj.getStatus())) {
                prj.setProjectColor(ProjectColor.Red.getCode());
            }

            list = new ArrayList<>();
            list.add(ProjectStatus.BuTongGuo.getCode());
            list.add(ProjectStatus.JietiBuTongGuo.getCode());

            if (list.contains(prj.getStatus())) {
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

        return projectList;
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
}
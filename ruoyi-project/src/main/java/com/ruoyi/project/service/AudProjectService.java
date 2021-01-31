package com.ruoyi.project.service;

import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.PmUplevelproject;
import com.ruoyi.project.mapper.*;
import com.ruoyi.project.domain.PmProjectjoinorganization;

import com.ruoyi.project.domain.PmProjectmember;
import com.ruoyi.project.domain.AudProjectdoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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


    public List<AudProject> selectAudProjectZaiYanList(AudProject project) {

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
            }
            catch (Exception e) {

            }

            long days = (DateUtils.getNowDate().getTime() - ended.getTime())/(1000 * 24 * 60 * 60);

            if (days < 900) {
                prj.setProjectDateRange(prj.getProjectbegindate() + "至" + prj.getProjectenddate() + "(剩余 " + String.valueOf(days) + "天)");
                prj.setProjectColor(ProjectColor.Red.getCode());
            }
            else {
                prj.setProjectDateRange(prj.getProjectbegindate() + "至" + prj.getProjectenddate());
            }

        }

        return projectList;
    }


    public AudProject selectProjectById(Integer projectId)
    {

        return audProjectMapper.selectProjectById(projectId);
    }

    public Integer queryIfDuplicate(AudProject project) {
        return audProjectMapper.queryIfDuplicate(project);
    }


    public void updateProjectDetail(AudProject project) {

        Integer projectId = project.getProjectid();

        if (project.getJointype() != null) {
            if (project.getJointype() == 1) {
                List<PmProjectjoinorganization> joinList  = project.getProjectJoinOrganizationList();
                List<Integer> ids = new ArrayList<>();
                for (PmProjectjoinorganization s : joinList) {
                    s.setProjectid(projectId.toString());

                    if (s.getJoid() != null) {
                        ids.add(s.getJoid());
                    }
                }

                PmProjectjoinorganization  join = new PmProjectjoinorganization();
                join.setProjectid(projectId.toString());
                List<PmProjectjoinorganization> joinList2 = joinorganizationMapper.selectProjectjoinorganizationList(join);

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
                    }
                    else {
                        joinorganizationMapper.insertProjectjoinorganization(s);
                    }
                }
            }
            else if (project.getJointype() == 2) {

                PmUplevelproject uplevelProject = project.getUplevelproject();

                if (uplevelProject == null) {
                    PmUplevelproject uplevelProject2 = new PmUplevelproject();
                    uplevelProject2.setProjectid(project.getProjectid());
                    uplevelprojectMapper.deleteUplevelProject(uplevelProject2);
                }
                else {
                    uplevelProject.setProjectid(projectId);

                    PmUplevelproject uplevelproject2 = uplevelprojectMapper.selectUplevelProjectByProjectid(projectId);

                    if (uplevelproject2 != null) {
                        if (uplevelProject.getInfoid() == uplevelproject2.getInfoid()) {
                            uplevelprojectMapper.updateUplevelProject(uplevelProject);
                        }
                        else {
                            uplevelprojectMapper.deleteUplevelProject(uplevelproject2);
                            uplevelprojectMapper.insertUplevelProject(uplevelProject);
                        }
                    }
                    else {
                        uplevelprojectMapper.insertUplevelProject(uplevelProject);
                    }
                }
            }
        }

        projectmemberMapper.deleteProjectmemberByProjectId(projectId);

        log.debug("project.getProjectmemberList is " + project.getProjectmemberList().toString());
        for (Integer s: project.getProjectmemberList()) {
            PmProjectmember projectmember = new PmProjectmember();
            projectmember.setProjectid(projectId);
            projectmember.setUserid(s);
            projectmemberMapper.insertProjectmember(projectmember);
        }

        List<AudProjectdoc> doclist = project.getProjectdocList();

        for(AudProjectdoc s: doclist) {
            s.setProjectid(projectId);
        }

        log.debug("doclist is " + doclist.toString());

        projectdocMapper.selectProjectdocList(new AudProjectdoc());
    }


    @Transactional
    public Integer insertProject(AudProject project) {
        log.debug("insertProject is below.");

        Integer rows = audProjectMapper.insertProject(project);

        Integer projectId = rows > 0 ? project.getProjectid() : rows;

        log.debug("insertProject rows is " + rows.toString() + " projectid is " + project.getProjectid().toString());

        if (projectId > 0 ) {

            log.debug("projectJoinOrganizationList is " + project.getProjectJoinOrganizationList());
            log.debug("uplevelproject is " + project.getUplevelproject());
            log.debug("projectmemberList is " + project.getProjectmemberList());
            log.debug("projectdocList is " + project.getProjectdocList());

            this.updateProjectDetail(project);

        }

        return projectId;
    }

    @Transactional
    public Integer updateProject(AudProject project) {

        Integer rows = audProjectMapper.updateProject(project);

        this.updateProjectDetail(project);

        return rows;
    }

}
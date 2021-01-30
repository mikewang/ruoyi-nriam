package com.ruoyi.project.service;

import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.mapper.AudProjectMapper;
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

    @Transactional
    public Integer insertProject(AudProject record) {
        log.debug("insertProject is below.");

        Integer rows = audProjectMapper.insertProject(record);

        log.debug("useGeneratedKeys projectid  is " + rows.toString());

        return rows;
    }

    @Transactional
    public Integer updateProject(AudProject record) {

        return audProjectMapper.updateProject(record);
    }

}
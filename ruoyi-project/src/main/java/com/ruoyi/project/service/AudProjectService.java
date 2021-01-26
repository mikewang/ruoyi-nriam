package com.ruoyi.project.service;

import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.mapper.AudProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudProjectService {
    private static final Logger log = LoggerFactory.getLogger(AudProjectService.class);

    @Resource
    private AudProjectMapper audProjectMapper;

    public List<AudProject> selectAudProjectList(AudProject project) {

        List<AudProject> projectList = audProjectMapper.selectAudProjectList(project);

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

        return projectList;
    }

}
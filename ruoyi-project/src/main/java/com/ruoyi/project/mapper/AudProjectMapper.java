package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.AudProject;


import java.util.List;

public interface AudProjectMapper {

    List<AudProject> selectAudProjectList(AudProject project);

    AudProject selectProjectById(Integer projectid);

    int queryIfDuplicate(AudProject project);

    int insertProject(AudProject record);

    int updateProject(AudProject record);


    List<AudProject> selectAll();


}
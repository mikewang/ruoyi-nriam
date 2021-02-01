package com.ruoyi.project.mapper;


import com.ruoyi.project.domain.PmProjectmember;
import java.util.List;

public interface PmProjectmemberMapper {

    int insert(PmProjectmember record);

    List<PmProjectmember> selectProjectmemberByProjectid(Integer projectid);

    int deleteProjectmemberByProjectId(Integer projectid);

    int insertProjectmember(PmProjectmember record);

    int mergerProjectmember(List<PmProjectmember> records);

    int sourceMergerProjectmember(List<PmProjectmember> records);

}
package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.PmProjectmember;
import java.util.List;

public interface PmProjectmemberMapper {

    int insert(PmProjectmember record);

    List<PmProjectmember> selectProjectmemberByProjectid(Integer projectid);
}
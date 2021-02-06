package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.PmProjectacceptance;
import java.util.List;

public interface PmProjectacceptanceMapper {

    PmProjectacceptance selectProjectacceptanceById(Integer projectid);
    int insertProjectacceptance(PmProjectacceptance record);
    int deleteProjectacceptanceById(Integer projectid);
    int updateProjectacceptance(PmProjectacceptance record);

}
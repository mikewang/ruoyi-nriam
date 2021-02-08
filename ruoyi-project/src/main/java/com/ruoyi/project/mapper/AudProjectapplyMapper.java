package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.AudProjectapply;
import java.util.List;

public interface AudProjectapplyMapper {

    List<AudProjectapply> selectProjectapplyList(AudProjectapply projectapply);

    AudProjectapply selectProjectapplyById(Integer projectapplyid);

    int deleteByPrimaryKey(Integer projectapplyid);

    int insert(AudProjectapply record);

    int updateByPrimaryKey(AudProjectapply record);
}
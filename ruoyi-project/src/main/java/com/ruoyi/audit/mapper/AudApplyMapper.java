package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudApply;
import java.util.List;

public interface AudApplyMapper {

    int insertAudApply(AudApply record);

    AudApply selectAudApplyById(Integer applyid);

    int deleteAudApplyById(Integer applyid);

    AudApply selectApplyByTypeAndRelatedID(AudApply record);

    List<AudApply> selectAll();

    int updateByPrimaryKey(AudApply record);
}
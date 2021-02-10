package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudApplyAchieve;

import java.util.HashMap;
import java.util.List;

public interface AudApplyMapper {

    int insertAudApply(AudApply record);

    AudApply selectAudApplyById(Integer applyid);

    int deleteAudApplyById(Integer applyid);

    AudApply selectApplyByTypeAndRelatedID(AudApply record);

    List<AudApply> selectAll();

    int updateByPrimaryKey(AudApply record);

    List<AudApplyAchieve> selectAchieveToConfirmList(HashMap map);

}
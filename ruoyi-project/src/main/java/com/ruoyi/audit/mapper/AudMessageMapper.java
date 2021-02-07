package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudMessage;

import java.util.List;

public interface AudMessageMapper {

    List<AudMessage> selectMessageByToUserId(Integer touserId);

    int insertAudMessage(AudMessage record);
    int updateIfProcessedById(AudMessage record);

    AudMessage selectByPrimaryKey(Integer messageid);

    int updateByPrimaryKey(AudMessage record);

    int updateProcessedByIds(List ids);
}

package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudMessage;

import java.util.List;

public interface AudMessageMapper {

    List<AudMessage> selectByUserId(Long userId);

    int insertAudMessage(AudMessage record);

    AudMessage selectByPrimaryKey(Integer messageid);

    int updateByPrimaryKey(AudMessage record);

    int updateProcessedByIds(List ids);
}

package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudMessage;

import java.util.List;

public interface AudMessageMapper {

    List<AudMessage> selectByUserId(Integer userId);

    int deleteByPrimaryKey(Integer messageid);

    int insert(AudMessage record);

    AudMessage selectByPrimaryKey(Integer messageid);


    List<AudMessage> selectAll();

    int updateByPrimaryKey(AudMessage record);
}
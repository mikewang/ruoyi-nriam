package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudMessage;

import java.util.List;

public interface AudMessageMapper {

    List<AudMessage> selectByUserId(Integer userId);
}

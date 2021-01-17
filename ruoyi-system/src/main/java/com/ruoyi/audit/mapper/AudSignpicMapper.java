package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudSignpic;

import java.util.List;

public interface AudSignpicMapper {

    List<AudSignpic> selectSignpicList(AudSignpic signpic);

    AudSignpic selectSignpicById(Integer id);

    int updateSignpicById(AudSignpic record);

}

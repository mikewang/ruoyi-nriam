package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudSignpic;

import java.util.List;

public interface AudSignpicMapper {

    List<AudSignpic> selectSignpicList(AudSignpic signpic);

    AudSignpic selectSignpicById(Integer id);

    int mergeSignpic(AudSignpic record);

    int updateSignpic(AudSignpic record);

    int insertSignpic(AudSignpic record);

    int removeSignpicByUserIds(List ids);

}

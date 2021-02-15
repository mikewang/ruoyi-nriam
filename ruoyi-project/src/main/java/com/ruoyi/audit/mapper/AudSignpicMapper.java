package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudSignpic;

import java.util.List;

public interface AudSignpicMapper {

    List<AudSignpic> selectSignpicList(AudSignpic signpic);

    AudSignpic selectSignpicById(Integer id);

    AudSignpic selectSignpicByUserId(Integer userId);

    int mergeSignpic(AudSignpic record);

    int updateSignpic(AudSignpic record);

    int insertSignpic(AudSignpic record);

    int removeSignpicByUserIds(List ids);

}

package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.AudProjectdoc;

import java.util.List;

public interface AudProjectdocMapper {

    int insert(AudProjectdoc record);

    List<AudProjectdoc> selectProjectdocList(AudProjectdoc record);

    int deleteProjectdoc(AudProjectdoc record);

    int insertProjectdoc(AudProjectdoc record);

}
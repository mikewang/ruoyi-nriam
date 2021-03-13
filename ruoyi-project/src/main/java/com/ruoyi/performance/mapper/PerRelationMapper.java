package com.ruoyi.performance.mapper;

import com.ruoyi.performance.domain.PerRelation;
import java.util.List;

public interface PerRelationMapper {

    List<PerRelation> selectPerRelation(PerRelation record);

    int insertPerRelation(PerRelation record);

    int updatePerRelation(PerRelation record);

    List<PerRelation> selectAll();
}
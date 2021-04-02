package com.ruoyi.project.mapper;

import com.ruoyi.common.core.domain.model.BasDoc;

import java.util.List;

public interface BasDocMapper {
    int insertBasDoc(BasDoc record);
    int updateBasDoc(BasDoc record);
    int updateBasDocAttachToType(BasDoc record);
    int deleteBasDocById(Integer id);
    int deleteBasDocByIds(List ids);
    List<BasDoc> selectBasDocListByIds(List ids);
    List<BasDoc> selectBasDocList(BasDoc record);
    BasDoc selectBasDocById(Integer id);
//    int updateBasDocAttachTo(BasDoc record);
    List<BasDoc> selectAll();
}
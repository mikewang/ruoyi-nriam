package com.ruoyi.logis.mapper;

import com.ruoyi.logis.domain.BasDoc;
import org.apache.ibatis.annotations.Options;

import java.util.List;

public interface BasDocMapper {

    BasDoc selectByPrimaryKey(Integer docid);
    List<BasDoc> selectByIds(List<Integer> ids);

    List<BasDoc> selectAll();

    int insert(BasDoc record);

    int updateByPrimaryKey(BasDoc record);

    int deleteByPrimaryKey(Integer docid);
}
package com.ruoyi.logis.mapper;

import com.ruoyi.logis.domain.BasDoc;
import java.util.List;

public interface BasDocMapper {

    BasDoc selectByPrimaryKey(Integer docid);

    List<BasDoc> selectAll();

    int insert(BasDoc record);

    int updateByPrimaryKey(BasDoc record);

    int deleteByPrimaryKey(Integer docid);
}
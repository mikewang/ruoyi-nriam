package com.ruoyi.logis.mapper;

import com.ruoyi.logis.domain.LogisContract;
import java.util.List;

public interface LogisContractMapper {
    int insert(LogisContract record);
    List<LogisContract> selectAll();
}
package com.ruoyi.logis.mapper;

import com.ruoyi.logis.domain.LogisContract;
import java.util.List;

public interface LogisContractMapper {

    public List<LogisContract> selectLogisContractList(LogisContract record);
    public LogisContract selectLogisContractById(Long contractId);

    public int insertLogisContract(LogisContract record);
    public int updateLogisContract(LogisContract record);
}
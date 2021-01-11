package com.ruoyi.logis.service;

import com.ruoyi.common.annotation.DataScope;

import com.ruoyi.logis.domain.LogisContract;
import com.ruoyi.logis.mapper.LogisContractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogisContractService {
    private static final Logger log = LoggerFactory.getLogger(LogisContractService.class);

    @Resource
    private LogisContractMapper logisContractMapper;

    @DataScope(deptAlias = "d", userAlias = "u")
    public List<LogisContract> selectLogisContractList(LogisContract logisContract) {
        return logisContractMapper.selectLogisContractList(logisContract);
    }

    public LogisContract selectLogisContractById(Long contractId) {
        return logisContractMapper.selectLogisContractById(contractId);
    }

    @Transactional
    public int insertLogisContract(LogisContract logisContract) {
        // 新增信息
        int rows = logisContractMapper.insertLogisContract(logisContract);
//        // 新增用户岗位关联
//        insertUserPost(user);
//        // 新增用户与角色管理
//        insertUserRole(user);
        return rows;
    }

    @Transactional
    public int updateLogisContract(LogisContract logisContract) {
        // 新增用户信息
        int rows = logisContractMapper.updateLogisContract(logisContract);
//        // 新增用户岗位关联
//        insertUserPost(user);
//        // 新增用户与角色管理
//        insertUserRole(user);
        return rows;
    }


}

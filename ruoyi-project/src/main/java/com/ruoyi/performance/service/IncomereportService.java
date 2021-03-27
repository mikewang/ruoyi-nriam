package com.ruoyi.performance.service;

import com.ruoyi.common.constant.CHString;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.mapper.AudProjectMapper;
import com.ruoyi.project.mapper.PmTeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncomereportService {

    private static final Logger log = LoggerFactory.getLogger(IncomereportService.class);

    @Resource
    PerIncomeMapper incomeMapper;

    @Resource
    PmTeamMapper teamMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;


    @Resource
    PerRelationMapper relationMapper;


    public List<PerIncome> selectPerIncome(PerIncome record) {

        List<PerIncome> list = incomeMapper.selectPerIncome(record);

        return list;
    }

    

    @Transactional
    public Integer insertPerIncome(PerIncome record) {

        Integer  result = incomeMapper.insertPerIncome(record);

        return result;
    }

    @Transactional
    public Integer updatePerIncome(PerIncome record) {
        Integer  result = incomeMapper.updatePerIncome(record);

        return result;

    }

    @Transactional
    public Integer deletePerIncomeById(Integer incomeid) {
        Integer  result = incomeMapper.updatePerIncomeDeletedById(incomeid);
        return result;
    }

    @Transactional
    public Integer confirmPerIncomeByIds(List<Integer> incomeids) {

        Integer incomeid = incomeids.get(0);

        Integer  result = incomeMapper.updatePerIncomeConfirmById(incomeid);

        return result;

    }

}

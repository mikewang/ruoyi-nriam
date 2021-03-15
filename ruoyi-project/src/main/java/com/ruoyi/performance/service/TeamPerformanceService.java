package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchAppraisal;
import com.ruoyi.achieve.mapper.AchAppraisalMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatorappraisal;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorappraisalMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TeamPerformanceService {

    private static final Logger log = LoggerFactory.getLogger(TeamPerformanceService.class);

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    public List<PerTeamperformance> selectPerTeamperformance(PerTeamperformance record) {

        List<PerTeamperformance> list = teamperformanceMapper.selectPerTeamperformance(record);

        return list;
    }



}

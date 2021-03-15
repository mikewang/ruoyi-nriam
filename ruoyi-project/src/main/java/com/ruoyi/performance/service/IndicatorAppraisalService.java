package com.ruoyi.performance.service;

import com.ruoyi.performance.domain.PerIndicatorappraisal;
import com.ruoyi.performance.mapper.PerIndicatorappraisalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class IndicatorAppraisalService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorAppraisalService.class);

    @Resource
    PerIndicatorappraisalMapper indicatorappraisalMapper;

    public PerIndicatorappraisal selectPerIndicatorappraisalById(Integer fundid) {

        PerIndicatorappraisal fund = indicatorappraisalMapper.selectPerIndicatorappraisalById(fundid);

        return fund;
    }


    @Transactional
    public Integer updatePerIndicatorappraisal(PerIndicatorappraisal fund) {
        Integer result = 1;

        result = indicatorappraisalMapper.updatePerIndicatorappraisal(fund);

        return result;
    }

}

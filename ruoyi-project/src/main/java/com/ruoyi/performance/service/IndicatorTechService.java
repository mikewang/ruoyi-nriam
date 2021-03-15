package com.ruoyi.performance.service;

import com.ruoyi.performance.domain.PerIndicatortech;
import com.ruoyi.performance.mapper.PerIndicatortechMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class IndicatorTechService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorTechService.class);

    @Resource
    PerIndicatortechMapper indicatortechMapper;

    public PerIndicatortech selectPerIndicatortechById(Integer fundid) {

        PerIndicatortech fund = indicatortechMapper.selectPerIndicatortechById(fundid);

        return fund;
    }


    @Transactional
    public Integer updatePerIndicatortech(PerIndicatortech fund) {
        Integer result = 1;

        result = indicatortechMapper.updatePerIndicatortech(fund);

        return result;
    }

}

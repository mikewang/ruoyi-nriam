package com.ruoyi.performance.service;

import com.ruoyi.performance.domain.PerIndicator;
import com.ruoyi.performance.domain.PerIndicatorfund;
import com.ruoyi.performance.domain.PerIndicatorproject;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.mapper.PerIndicatorMapper;
import com.ruoyi.performance.mapper.PerIndicatorfundMapper;
import com.ruoyi.performance.mapper.PerIndicatorprojectMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorFundService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorFundService.class);

    @Resource
    PerIndicatorfundMapper indicatorfundMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerIndicatorMapper indicatorMapper;

    public PerIndicatorfund selectPerIndicatorfundById(Integer fundid) {

        PerIndicatorfund fund = indicatorfundMapper.selectPerIndicatorfundById(fundid);

        return fund;
    }


    @Transactional
    public Integer updatePerIndicatorfund(PerIndicatorfund fund) {
        Integer result = 1;

        result = indicatorfundMapper.updatePerIndicatorfund(fund);

        //   //计算今年到账经费  待修改 （原代码 注释掉其中的关系。）

        return result;
    }

}

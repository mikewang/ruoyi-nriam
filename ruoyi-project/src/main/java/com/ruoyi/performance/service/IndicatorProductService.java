package com.ruoyi.performance.service;

import com.ruoyi.performance.domain.PerIndicatorproduct;
import com.ruoyi.performance.mapper.PerIndicatorproductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class IndicatorProductService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorProductService.class);

    @Resource
    PerIndicatorproductMapper indicatorproductMapper;

    public PerIndicatorproduct selectPerIndicatorproductById(Integer fundid) {

        PerIndicatorproduct fund = indicatorproductMapper.selectPerIndicatorproductById(fundid);

        return fund;
    }


    @Transactional
    public Integer updatePerIndicatorproduct(PerIndicatorproduct fund) {
        Integer result = 1;

        result = indicatorproductMapper.updatePerIndicatorproduct(fund);

        return result;
    }

}

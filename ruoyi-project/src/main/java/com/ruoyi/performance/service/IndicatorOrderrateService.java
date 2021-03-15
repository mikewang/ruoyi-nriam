package com.ruoyi.performance.service;

import com.ruoyi.performance.domain.PerIndicatororderrate;
import com.ruoyi.performance.mapper.PerIndicatororderrateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorOrderrateService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorOrderrateService.class);

    @Resource
    PerIndicatororderrateMapper PerIndicatororderrateMapper;


    public List<PerIndicatororderrate> selectPerIndicatororderrate(PerIndicatororderrate standard) {

        List<PerIndicatororderrate> list = PerIndicatororderrateMapper.selectPerIndicatororderrate(standard);

        return list;
    }

    
    @Transactional
    public Integer updatePerIndicatororderrate(PerIndicatororderrate standard) {
        Integer result = 1;

        result = PerIndicatororderrateMapper.updatePerIndicatororderrate(standard);

        return result;
    }

}

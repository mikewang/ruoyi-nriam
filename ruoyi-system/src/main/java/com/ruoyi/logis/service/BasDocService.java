package com.ruoyi.logis.service;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.logis.domain.BasDoc;
import com.ruoyi.logis.mapper.BasDocMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BasDocService {
    private static final Logger log = LoggerFactory.getLogger(BasDocService.class);

    @Resource
    private BasDocMapper basDocMapper;

    public List<BasDoc> selectBasDocListByIds(List<String> ids) {

        return null;
//        String [] docIds = ids.split(",");
//
//        List<String> items= Arrays.asList(docIds);
//
//        return basDocMapper.selectByIds(items);
    }

}

package com.ruoyi.project.service;

import com.ruoyi.project.domain.AudProjectdoc;
import com.ruoyi.project.mapper.AudProjectdocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudProjectdocService {
    private static final Logger log = LoggerFactory.getLogger(AudProjectdocService.class);

    @Resource
    private AudProjectdocMapper mapper;


    public List<AudProjectdoc> selectProjectdocList(AudProjectdoc record)
    {
        return mapper.selectProjectdocList(record);
    }
}
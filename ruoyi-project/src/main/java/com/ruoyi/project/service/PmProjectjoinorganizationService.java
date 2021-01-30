package com.ruoyi.project.service;

import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.domain.PmProjectjoinorganization;
import com.ruoyi.project.mapper.PmProjectjoinorganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PmProjectjoinorganizationService {
    private static final Logger log = LoggerFactory.getLogger(PmProjectjoinorganizationService.class);

    @Resource
    private PmProjectjoinorganizationMapper mapper;

    public List<PmProjectjoinorganization> selectProjectjoinorganizationList(PmProjectjoinorganization query) {
        log.debug("PmProjectjoinorganization projectid is " + query.getProjectid());
        List<PmProjectjoinorganization> orgList = mapper.selectProjectjoinorganizationList(query);

        return orgList;
    }

}
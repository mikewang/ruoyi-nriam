package com.ruoyi.project.service;

import com.ruoyi.project.domain.AudProjectapply;
import com.ruoyi.project.mapper.AudProjectapplyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudProjectapplyService {
    private static final Logger log = LoggerFactory.getLogger(AudProjectapplyService.class);

    @Resource
    private AudProjectapplyMapper audProjectapplyMapper;

    public List<AudProjectapply> selectProjectapplyList(AudProjectapply projectapply) {

        List<AudProjectapply> projectList = audProjectapplyMapper.selectProjectapplyList(projectapply);

        return projectList;
    }

}
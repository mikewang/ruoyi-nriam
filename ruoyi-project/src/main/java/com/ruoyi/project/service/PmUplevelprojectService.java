package com.ruoyi.project.service;

import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.PmUplevelproject;
import com.ruoyi.project.mapper.PmUplevelprojectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PmUplevelprojectService {
    private static final Logger log = LoggerFactory.getLogger(PmUplevelprojectService.class);

    @Resource
    private PmUplevelprojectMapper mapper;

    public List<PmUplevelproject> selectUplevelprojectList(PmUplevelproject query) {
        log.debug("PmUplevelproject projectid is " + query.getProjectid());
        List<PmUplevelproject> orgList = mapper.selectUplevelprojectList(query);

        return orgList;
    }

    public PmUplevelproject selectUplevelProjectByProjectid(Integer projectId)
    {
        return mapper.selectUplevelProjectByProjectid(projectId);
    }
}
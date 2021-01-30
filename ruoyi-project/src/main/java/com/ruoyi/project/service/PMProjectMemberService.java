package com.ruoyi.project.service;

import com.ruoyi.project.domain.PmProjectmember;
import com.ruoyi.project.mapper.PmProjectmemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PMProjectMemberService {
    private static final Logger log = LoggerFactory.getLogger(PMProjectMemberService.class);

    @Resource
    private PmProjectmemberMapper mapper;


    public List<PmProjectmember> selectProjectmemberByProjectid(Integer projectId)
    {
        return mapper.selectProjectmemberByProjectid(projectId);
    }
}
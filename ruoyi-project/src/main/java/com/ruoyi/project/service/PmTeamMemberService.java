package com.ruoyi.project.service;

import com.ruoyi.project.domain.PmTeamMember;
import com.ruoyi.project.mapper.PmTeamMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PmTeamMemberService {
    private static final Logger log = LoggerFactory.getLogger(PmTeamMemberService.class);

    @Resource
    private PmTeamMemberMapper pmTeamMemberMapper;

    public List<PmTeamMember> selectTeamMemberList(PmTeamMember member) {

        List<PmTeamMember> memberList = pmTeamMemberMapper.selectTeamMemberList(member);

        return memberList;
    }


}
package com.ruoyi.project.service;

import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.mapper.PmTeamMapper;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PmTeamServiceTest {
    @Resource
    private PmTeamService pmTeamService;

    @org.junit.jupiter.api.Test
    void selectAll() {
        PmTeam team = new PmTeam();

        System.out.println(team.toString());

        List<PmTeam> teamList = pmTeamService.selectTeamList(team);

        System.out.println(teamList.toString());

    }
}
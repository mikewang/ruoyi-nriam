package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.PmTeam;
import java.util.List;

public interface PmTeamMapper {

    List<PmTeam> selectTeamList(PmTeam team);
    List<PmTeam> selectAll();

    PmTeam selectTeamById(Integer teamid);
    int insertTeam(PmTeam record);
    int updateTeam(PmTeam record);
    int deleteTeamById(Integer teamid);
    int deleteTeamByIds(List<Integer> teamids);

}
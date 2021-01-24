package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.domain.PmTeamMember;
import java.util.List;

public interface PmTeamMemberMapper {

    List<PmTeamMember> selectTeamMemberList(PmTeamMember record);

    int insertTeamMember(PmTeamMember record);
    int updateTeamMember(PmTeam record);
    int deleteTeamMemberById(Integer userid);
    int deleteTeamMembersByTeamid(Integer teamid);


}
package com.ruoyi.project.service;

import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.domain.PmTeamMember;
import com.ruoyi.project.mapper.PmTeamMapper;
import com.ruoyi.project.mapper.PmTeamMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PmTeamService {
    private static final Logger log = LoggerFactory.getLogger(PmTeamService.class);

    @Resource
    private PmTeamMapper pmTeamMapper;
    @Resource
    private PmTeamMemberMapper pmTeamMemberMapper;

    public List<PmTeam> selectTeamList(PmTeam team) {
        List<PmTeam> teamList = pmTeamMapper.selectTeamList(team);

        for (PmTeam t : teamList) {

            // 设置 返回的团队成员名称字符串
            PmTeamMember member = new PmTeamMember();
            member.setTeamid(t.getTeamid());
            List<PmTeamMember> memberList = pmTeamMemberMapper.selectTeamMemberList(member);
            List<String> members = new ArrayList();
            for (PmTeamMember s : memberList) {
                members.add(s.getRealName());
            }
            String memberStr = String.join(",",members);
            log.debug(memberStr);
            t.setMembers(memberStr);

            // 设置成员树形列表。
            List<HashMap> roleMemberList = new ArrayList();


            for (PmTeamMember s : memberList) {
                HashMap user = new HashMap();
                user.put("userid", s.getUserid());
                user.put("realName", s.getRealName());

                Boolean roleMemberX = true;
                for ( HashMap roleMem : roleMemberList ) {
                    if (roleMem.get("teamrole").equals(s.getTeamrole())) {
                        List<HashMap> userList = (List<HashMap>) roleMem.get("userList");
                        userList.add(user);
                        roleMemberX = false;
                        break;
                    }
                }
                if (roleMemberX) {
                    roleMemberX = false;
                    HashMap roleMem = new HashMap();
                    roleMem.put("teamrole", s.getTeamrole());
                    roleMem.put("teamroleName", s.getTeamroleName());

                    List<HashMap> userList = new ArrayList();
                    userList.add(user);
                    roleMem.put("userList", userList);

                    roleMemberList.add(roleMem);
                }

            }

            log.debug(roleMemberList.toString());

            t.setMemberList(roleMemberList);

        }

        return teamList;
    }


    public List<PmTeam> selectAll() {
        List<PmTeam> teamList = pmTeamMapper.selectAll();

        return teamList;
    }

    @Transactional
    public int addTeam(PmTeam team) {

        int rows = pmTeamMapper.insertTeam(team);
        int teamid = team.getTeamid();

        for (List<Integer> itemList : team.getCheckedIdList()){
            int index = team.getCheckedIdList().indexOf(itemList);
            HashMap<String, Object> memberGroup = team.getMemberList().get(index);

            for (Integer userid : itemList) {
                PmTeamMember member = new PmTeamMember();
                member.setTeamid(teamid);
                member.setTeamrole((Integer) memberGroup.get("teamrole"));
                member.setUserid(userid);
                rows = pmTeamMemberMapper.insertTeamMember(member);
            }
        }

        return rows;
    }

    @Transactional
    public int updateTeam(PmTeam team) {

        int rows = pmTeamMapper.updateTeam(team);
        int teamid = team.getTeamid();
        rows = pmTeamMemberMapper.deleteTeamMembersByTeamid(teamid);

        for (List<Integer> itemList : team.getCheckedIdList()){
            int index = team.getCheckedIdList().indexOf(itemList);
            HashMap<String, Object> memberGroup = team.getMemberList().get(index);

            for (Integer userid : itemList) {
                PmTeamMember member = new PmTeamMember();
                member.setTeamid(teamid);

                member.setTeamrole((Integer) memberGroup.get("teamrole"));
                member.setUserid(userid);
                rows = pmTeamMemberMapper.insertTeamMember(member);
            }
        }

        return rows;
    }
}
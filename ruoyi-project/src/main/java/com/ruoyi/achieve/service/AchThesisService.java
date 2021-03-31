package com.ruoyi.achieve.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchThesis;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchThesisMapper;
import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.constant.MID;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.AchieveType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AchThesisService {
    private static final Logger log = LoggerFactory.getLogger(AchThesisService.class);

    @Resource
    private AchThesisMapper thesisMapper;

    @Resource
    private AchAuthorMapper authorMapper;

    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private PerTeamperformanceMapper teamperformanceMapper;


    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;

    private String ACHIEVETYPE = AchieveType.THESIS.getInfo();

    public List<AchThesis> selectAchThesisList(AchThesis thesis) {

        List<AchThesis> thesisList = thesisMapper.selectAchThesis(thesis);

        for (AchThesis p : thesisList) {
            Integer thesisid = p.getThesisid();
            AchAuthor query = new AchAuthor();
            query.setAchievetype(ACHIEVETYPE);
            query.setRelatedid(thesisid);

            List<AchAuthor> achAuthorList = authorMapper.selectAchAuthorList(query);
            List<String> personnames = new ArrayList<>();
            for (AchAuthor a : achAuthorList) {
                personnames.add(a.getPersonname());
            }
            String authors = StringUtils.joinWith(",", personnames);

            p.setAuthorList(achAuthorList);
            p.setAuthors(authors);

            if (p.getThesislevel().equals("-1")) {
                p.setThesislevel("");
            }


            //状态文字设置颜色
            if (p.getStatus() == AchieveStatus.DaiQueRen.getCode()) {
                p.setStatusColor(1);
            }
            else if (p.getStatus() == AchieveStatus.BuTongGuo.getCode()) {
                p.setStatusColor(-1);
            }
            else {
                p.setStatusColor(0);
            }

        }
        log.debug("AchThesis list is request.");
        return thesisList;
    }

    public AchThesis selectAchThesisById(Integer thesisid) {

        AchThesis p =  thesisMapper.selectAchThesisById(thesisid);
        if (p == null) {
            return null;
        }

        AchAuthor query = new AchAuthor();
        log.debug("ACHIEVETYPE is " + ACHIEVETYPE + " thesisid is " + thesisid.toString());
        query.setAchievetype(ACHIEVETYPE);
        query.setRelatedid(thesisid);

        List<AchAuthor> achAuthorList = authorMapper.selectAchAuthorList(query);
        log.debug("achAuthorList is " + achAuthorList.toString());

        p.setAuthorList(achAuthorList);
        List<String> personnames = new ArrayList<>();
        for (AchAuthor a : achAuthorList) {
            personnames.add(a.getPersonname());
        }
        String authors = StringUtils.joinWith(",", personnames);

        p.setAuthors(authors);

        return  p;
    }


    public Integer queryIfDuplicate(AchThesis thesis) {
        return thesisMapper.queryIfDuplicate(thesis);
    }



    public Integer updateThesisDetail(AchThesis thesis) {

        Integer thesisid = thesis.getThesisid();

        //保存对应的作者
        List<AchAuthor> authors = thesis.getAuthorList();
        AchAuthor query = new AchAuthor();
        query.setAchievetype(ACHIEVETYPE);
        query.setRelatedid(thesis.getThesisid());
        log.debug("deleteAchAuthor is " + query.toString());
        authorMapper.deleteAchAuthor(query);
        Integer i = 1;
        for(AchAuthor author : authors) {
            author.setRelatedid(thesisid);
            author.setAchievetype(ACHIEVETYPE);
            author.setOrdernumber(i);
            author.setIfreporter(false);
            authorMapper.insertAchAuthor(author);
            i = i + 1;
        }
        return 1;
    }

    // 有"成果审核"权限的人
    public Set<Long> getConfirmUserIdList(){

        SysUserMenu userMenu = new SysUserMenu();
        userMenu.setRoleId(Long.valueOf(MID.AchieveConfirm));

        List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

        Set<Long> useridSet = new HashSet<>();
        for (SysUserMenu um : userMenuList) {
            useridSet.add(um.getUserId());
        }
        return useridSet;
    }



    public void sendApplyAndMsg(AchThesis thesis) {

        String title = "新的科技成果待审核";
        String content = "学术论文：“" + thesis.getThesisname()  + "”的信息待审核。"
            + "<a href=\"/Achieve/ToConfirmList.aspx?at=" + ACHIEVETYPE
            + "&kid=" + thesis.getThesisid() + "&f=todo\" target=\"_self\" >查看</a> ";

        AudApply apply = new AudApply();
        apply.setApplytype(ACHIEVETYPE);
        apply.setRelatedid(thesis.getThesisid());
        apply.setApplyuserid(thesis.getCreateuserid());
        apply.setApplytime(DateUtils.dateTimeNow());
        apply.setApplyreason("");
        apply.setApplystatus(0);
        applyMapper.insertAudApply(apply);

        //发送通知消息(发送给具有“科技成果审核”功能的人员)

        Set<Long> useridSet = getConfirmUserIdList();

        log.debug("send message " + ACHIEVETYPE + " userid is " + useridSet.toString());

        for (Long userid : useridSet) {
            //查询消息要发送给哪个人员
            //发送给项目的负责人
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            message.setTouserid(userid.intValue());
            message.setRelatedsheettype(ACHIEVETYPE);
            message.setRelatedsheetid(thesis.getThesisid());
            messageMapper.insertAudMessage(message);
        }

    }

    public Integer insertAchThesis(AchThesis thesis) {
        thesis.setCreatetime(DateUtils.dateTimeNow());
        thesis.setThesisid(-1);
        Integer rows = thesisMapper.insertAchThesis(thesis);
        updateThesisDetail(thesis);
        //发申请和通知
        sendApplyAndMsg(thesis);
        return  rows;
    }

    public Integer updateAchThesis(AchThesis thesis) {

        AchThesis undoThesis = thesisMapper.selectAchThesisById(thesis.getThesisid());

        Integer rows = thesisMapper.updateAchThesis(thesis);
        updateThesisDetail(thesis);

        if (undoThesis.getStatus() == AchieveStatus.BuTongGuo.getCode()) {
            //发申请和通知
            sendApplyAndMsg(thesis);
        }

        return  rows;
    }

    public Integer updateStatusAchThesis(AchThesis thesis) {
        return thesisMapper.updateStatusAchThesis(thesis);
    }



    public Integer confirmAchThesis(AchThesis thesis) {

        AchThesis record = new AchThesis();
        record.setThesisid(thesis.getThesisid());
        if (thesis.getConfirmResult() == 1) {
            record.setStatus(AchieveStatus.ZhengChang.getCode());

            //审核通过时，团队绩效加分, 后续开发，等绩效模块到位。

            PerTeamperformance p = new PerTeamperformance();

            teamperformanceMapper.insertPerTeamperformance(p);

//            if (Confirm1.rbtl_Result.SelectedValue == "1")  //通过
//            {
//                ITeamPerformanceManager iteamper = TeamPerformanceManager.GetInstance();
//                iteamper.AddRecord_FromThesis(Request["kid"]);
//            }
        }
        else if (thesis.getConfirmResult() == 2) {
            record.setStatus(AchieveStatus.BuTongGuo.getCode());
        }
        else {return 0;}

        thesisMapper.updateStatusAchThesis(record);

        AudApply apply = new AudApply();
        apply.setApplyid(thesis.getApplyid());
        apply.setAudituserid(thesis.getConfirmUserid());
        apply.setAudittime(DateUtils.dateTimeNow());
        apply.setAuditopinion(thesis.getConfirmNote());
        apply.setApplystatus(thesis.getConfirmResult());

        applyMapper.updateAuditAudApply(apply);


        //消除"审核人"待办事项
        Set<Long> useridSet = getConfirmUserIdList();

        for (Long userid : useridSet) {
            AudMessage message = new AudMessage();
            message.setProcessedtime(DateUtils.dateTimeNow());
            message.setTouserid(userid.intValue());
            message.setRelatedsheettype(ACHIEVETYPE);
            message.setRelatedsheetid(thesis.getThesisid());
            messageMapper.updateIfProcessedById(message);
        }

        String title = "";
        String content = "";
        if (thesis.getConfirmResult() == 1) {
            title = "您提交的科技成果审核通过";
            content = "学术论文：“" + thesis.getThesisname()  + "”的信息已经审核通过。"
                    + "<a href=\"/Achieve/ThesisList.aspx?"
                    + "kid=" + thesis.getThesisid() + "&f=todo\" target=\"_self\" >查看</a> ";
        }
        else if (thesis.getConfirmResult() == 2) {
            title = "您提交的科技成果审核不通过";
            content = "学术论文：“" + thesis.getThesisname() + "”的信息审核不通过。"
                    + "<a href=\"/Achieve/ThesisList.aspx?"
                    + "kid=" + thesis.getThesisid() + "&f=todo\" target=\"_self\" >查看</a> ";
        }

        //发送通知
        //发送给提交人
        AudMessage message = new AudMessage();
        message.setMessagetime(DateUtils.dateTimeNow());
        message.setMessagetitle(title);
        message.setMessagecontent(content);
        message.setTouserid(thesis.getCreateuserid());
        message.setRelatedsheettype(ACHIEVETYPE);
        message.setRelatedsheetid(thesis.getThesisid());
        messageMapper.insertAudMessage(message);

        return  1;
    }

    public AudApply selectApplyByTypeAndRelatedID(Integer thesisid, Integer status) {

        AudApply apply = new AudApply();

        if (status == AchieveStatus.BuTongGuo.getCode()) {
            apply.setApplytype(AchieveType.PATENT.getInfo());
        }
        else {
            return  null;
        }
        apply.setRelatedid(thesisid);
        return applyMapper.selectApplyByTypeAndRelatedID(apply);
    }

    public Integer removeAchThesis(Integer thesisid) {

        AchThesis record = new AchThesis();
        record.setThesisid(thesisid);
        record.setStatus(AchieveStatus.YiShanChu.getCode());
        Integer rows = thesisMapper.updateStatusAchThesis(record);
        //如果该成果对应的有待审核的“申请”和消息，则清除掉

        AudApply apply = new AudApply();

        apply.setApplytype(AchieveType.THESIS.getInfo());
        apply.setRelatedid(thesisid);
        apply.setApplystatus(0);
        AudApply apply2 = applyMapper.selectApplyByTypeAndRelatedID(apply);

        if (apply2 != null) {
            applyMapper.deleteAudApplyById(apply2.getApplyid());
            AudMessage message = new AudMessage();
            message.setRelatedsheettype(AchieveType.THESIS.getInfo());
            message.setRelatedsheetid(thesisid);
            messageMapper.updateIfProcessedAudMessageBySheetAndId(message);
        }

        //删掉对应的绩效计分记录。



        return  rows;
    }

}
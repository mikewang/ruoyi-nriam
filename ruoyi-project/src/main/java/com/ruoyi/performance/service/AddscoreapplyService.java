package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.*;
import com.ruoyi.achieve.mapper.*;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.constant.CHString;
import com.ruoyi.common.constant.MID;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.mapper.AudProjectMapper;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AddscoreapplyService {

    private static final Logger log = LoggerFactory.getLogger(AddscoreapplyService.class);

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    @Resource
    PerScoreschangelogMapper scoreschangelogMapper;

    @Resource
    PerAddscoreapplyMapper addscoreapplyMapper;

    @Resource
    BasDocMapper basDocMapper;

    @Resource
    private SysUserMenuMapper userMenuMapper;


    @Resource
    private AudMessageMapper messageMapper;



    public List<PerAddscoreapply> selectAddscoreapply(PerAddscoreapply record) {

        List<PerAddscoreapply> list = addscoreapplyMapper.selectPerAddscoreapply(record);

        return list;
    }

    public PerAddscoreapply selectAddscoreapplyById(Integer applyid) {

        PerAddscoreapply addscoreapply = addscoreapplyMapper.selectPerAddscoreapplyById(applyid);

        return addscoreapply;
    }


    private Integer sendAddscoreapplyConfirmMsg(PerAddscoreapply record) {
        Integer result = 0;
        //给相关的负责人发信息

        SysUserMenu userMenu = new SysUserMenu();
        userMenu.setMenuId(Long.valueOf(MID.ScoreApplyAudit));

        List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

        Set<Long> useridSet = new HashSet<>();
        for (SysUserMenu um : userMenuList) {
            useridSet.add(um.getUserId());
        }


        for (Long userid : useridSet) {

            String title =  "新的绩效考核加分申请待审核";
            String content =  "团队：“" + record.getTeamidlinktext() + "”的绩效考核加分申请待审核。"
                    + "<a href=\"/Performance/ApplyList_ToConfirm.aspx?"
                    + "kid=" + record.getApplyid() + "&f=todo\" target=\"_self\" >查看</a> ";
            //查询消息要发送给哪个人员
            AudMessage message = new AudMessage();
            message.setTouserid(Integer.valueOf(userid.toString()));
            message.setRelatedsheettype("加分申请");
            message.setRelatedsheetid(record.getApplyid());
            messageMapper.insertAudMessage(message);
            result = 1;
        }

        return  result;
    }

    private Integer updateAddscoreapplyBasDocAttachToType(PerAddscoreapply record) {

        Integer result = 0;
        for (BasDoc file : record.getFileList()) {
            BasDoc doc = new BasDoc();
            doc.setDocid(file.getDocid());

            doc.setAttachtotype("加分申请");
            doc.setRelatedid(record.getApplyid());
            doc.setDoctype("证明材料");

            basDocMapper.updateBasDocAttachToType(doc);
        }

        return result;
    }

    @Transactional
    public Integer insertAddscoreapply(PerAddscoreapply record) {

        Integer  result = addscoreapplyMapper.insertPerAddscoreapply(record);

        updateAddscoreapplyBasDocAttachToType(record);

        //给相关的负责人发信息
        sendAddscoreapplyConfirmMsg(record);

        return result;
    }

    @Transactional
    public Integer updateAddscoreapply(PerAddscoreapply record) {

        if (record.getApplystatus().equals("审核不通过")) {
            record.setApplystatus("待审核");

            //给相关的负责人发信息
            sendAddscoreapplyConfirmMsg(record);
        }

        Integer  result = addscoreapplyMapper.updatePerAddscoreapply(record);

        // 删除掉原来的已有未出现的证明材料。
        BasDoc doc = new BasDoc();
        doc.setAttachtotype("加分申请");
        doc.setRelatedid(record.getApplyid());
        doc.setDoctype("证明材料");

        for (BasDoc file : basDocMapper.selectBasDocList(doc)) {
            Integer docid = file.getDocid();
            for (BasDoc file2 : record.getFileList()) {
                if (docid == file.getDocid()) {
                    docid = -1;
                    break;
                }
            }
            if (docid != -1) {
                BasDoc doc2 = new BasDoc();
                doc2.setDocid(docid);

                doc2.setAttachtotype("加分申请");
                doc2.setRelatedid(null);
                doc2.setDoctype("证明材料");

                basDocMapper.updateBasDocAttachToType(doc2);
            }
        }

        updateAddscoreapplyBasDocAttachToType(record);

        return result;
    }


    @Transactional
    public Integer updateAddscoreapplyDeletedById(PerAddscoreapply record) {

        Integer  result = addscoreapplyMapper.updatePerAddscoreapplyDeletedById(record.getApplyid());

        return result;
    }

    @Transactional
    public Integer updateAddscoreapplyConfirm(PerAddscoreapply record) {

        Integer  result = addscoreapplyMapper.updatePerAddscoreapplyConfirm(record);


        if (record.getAuditResult().equals("1")) {
            //生成团队绩效记录
            PerTeamperformance teamperformance = new PerTeamperformance();

            teamperformance.setTeamid(record.getTeamid());
            teamperformance.setPerformanceyear(record.getYear());
            teamperformance.setIndicatortype("加分申请");
            teamperformance.setIndicatorid(-1);
            teamperformance.setAchieveid(record.getApplyid());

            String description = record.getLevel3name();
            teamperformance.setDescription(description);
            teamperformance.setPoints(record.getApplyscores());
            teamperformance.setLevel1id(record.getLevel1id());
            teamperformance.setLevel1name(record.getLevel1name());
            teamperformance.setLevel2id(record.getLevel2id());
            teamperformance.setLevel2name(record.getLevel2name());
            result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);


            //如果设置的分数与申请分数不同，则还要记录日志
            if (record.getLastApplyscores() != record.getApplyscores()) {
                String des = "申请分数为" + record.getLastApplyscores() + "，审核分数为" + record.getApplyscores();

                PerScoreschangelog scoreschangelog = new PerScoreschangelog();
                scoreschangelog.setDescription(des);
                scoreschangelog.setPerformanceid(teamperformance.getPerformanceid());
                scoreschangelog.setOldscores(record.getLastApplyscores());
                scoreschangelog.setNewscores(record.getApplyscores());
                scoreschangelog.setReason(record.getAuditopinion());
                scoreschangelog.setUserid(record.getAudituserid());
                scoreschangelog.setTime(DateUtils.dateTimeNow());

                scoreschangelogMapper.insertPerScoreschangelog(scoreschangelog);

            }

            //消除"审核人"待办事项

            SysUserMenu userMenu = new SysUserMenu();
            userMenu.setMenuId(Long.valueOf(MID.ScoreApplyAudit));

            List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

            Set<Long> useridSet = new HashSet<>();
            for (SysUserMenu um : userMenuList) {
                useridSet.add(um.getUserId());
            }

            for (Long userid : useridSet) {
                AudMessage message = new AudMessage();
                message.setProcessedtime(DateUtils.dateTimeNow());
                message.setTouserid(userid.intValue());
                message.setRelatedsheettype("加分申请");
                message.setRelatedsheetid(record.getApplyid());
                messageMapper.updateIfProcessedById(message);
            }

            //发送消息给提交人
            String title = "";
            String content = "";
            if (record.getAuditResult().equals("1"))  //通过
            {
                title = "您提交的加分申请审核通过";
                content = "关于“" + record.getLevel3name() + "”加分申请的信息已经审核通过。"
                        + "<a href=\"/Performance/AddScoreApplyList.aspx?"
                        + "kid=" + record.getApplyid() + "&f=todo\" target=\"_self\" >查看</a> ";
            }
            else
            {
                title = "您提交的加分申请审核不通过";
                content = "关于“" + record.getLevel3name() + "”的加分申请的信息审核不通过。"
                        + "<a href=\"/Performance/AddScoreApplyList.aspx?"
                        + "kid=" + record.getApplyid()+ "&f=todo\" target=\"_self\" >查看</a> ";
            }

            AudMessage message = new AudMessage();
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            message.setMessagetime(DateUtils.dateTimeNow());
            
            message.setTouserid(record.getApplyuserid());
            message.setRelatedsheettype("加分申请");
            message.setRelatedsheetid(record.getApplyid());
            messageMapper.insertAudMessage(message);
            
        }


        return result;
    }


}

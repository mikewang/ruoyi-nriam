package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.*;
import com.ruoyi.achieve.mapper.*;
import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.constant.CHString;
import com.ruoyi.common.constant.MID;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SMSSender.Juhe;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.AudProjectdoc;
import com.ruoyi.project.mapper.AudProjectMapper;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TeamPerformanceService {

    private static final Logger log = LoggerFactory.getLogger(TeamPerformanceService.class);

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    @Resource
    PerIndicatorMapper indicatorMapper;

    @Resource
    PerConfirmqequestMapper confirmqequestMapper;

    @Resource
    AudProjectMapper projectMapper;
    @Resource
    AchSoftwareMapper softwareMapper;

    @Resource
    AchProductMapper productMapper;

    @Resource
    AchAppraisalMapper appraisalMapper;
    @Resource
    AchPrizeMapper prizeMapper;
    @Resource
    AchPatentMapper patentMapper;

    @Resource
    AchThesisMapper thesisMapper;

    @Resource
    AchArticleMapper articleMapper;

    @Resource
    AchStandardMapper standardMapper;
    @Resource
    AchTechMapper techMapper;

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


    public List<PerTeamperformance> selectPerTeamperformance(PerTeamperformance record) {

        List<PerTeamperformance> list = teamperformanceMapper.selectPerTeamperformance(record);

        for (PerTeamperformance row : list) {

            if (row.getIndicatortype().equals(CHString.IndicatorProject)) {
                AudProject project = projectMapper.selectProjectById(row.getAchieveid());

                row.setIndicatortypeDetail(project.getProjectname());
            }
            if (row.getIndicatortype().equals(CHString.Software)) {

                AchSoftware software = softwareMapper.selectAchSoftwareById(row.getAchieveid());

                row.setIndicatortypeDetail(software.getSoftwarename());
            }

            if (row.getIndicatortype().equals(CHString.Product)) {

                AchProduct product = productMapper.selectAchProductById(row.getAchieveid());

                row.setIndicatortypeDetail(product.getProductname());
            }

            if (row.getIndicatortype().equals(CHString.Appraisal)) {

                AchAppraisal appraisal = appraisalMapper.selectAchAppraisalById(row.getAchieveid());

                row.setIndicatortypeDetail(appraisal.getAppraisalname());
            }

            if (row.getIndicatortype().equals(CHString.Prize)) {

                AchPrize prize = prizeMapper.selectAchPrizeById(row.getAchieveid());

                row.setIndicatortypeDetail(prize.getPrizename());
            }

            if (row.getIndicatortype().equals(CHString.Patent)) {

                Integer patentId = row.getAchieveid();

                AchPatent patent = patentMapper.selectAchPatentById(patentId);

                row.setIndicatortypeDetail(patent.getPatentname());
            }

            if (row.getIndicatortype().equals(CHString.Thesis)) {

                Integer patentId = row.getAchieveid();

                AchThesis patent = thesisMapper.selectAchThesisById(patentId);

                row.setIndicatortypeDetail(patent.getThesisname());
            }

            if (row.getIndicatortype().equals(CHString.Article)) {

                Integer patentId = row.getAchieveid();

                AchArticle patent = articleMapper.selectAchArticleById(patentId);

                row.setIndicatortypeDetail(patent.getArticlename());
            }

            if (row.getIndicatortype().equals(CHString.Standard)) {

                Integer patentId = row.getAchieveid();

                AchStandard patent = standardMapper.selectAchStandardById(patentId);

                row.setIndicatortypeDetail(patent.getStandardname());
            }

            if (row.getIndicatortype().equals(CHString.Tech)) {

                Integer patentId = row.getAchieveid();

                AchTech patent = techMapper.selectAchTechById(patentId);

                row.setIndicatortypeDetail(patent.getTechname());
            }

            List<String> sets = new ArrayList<>();
            sets.add(CHString.IndicatorFundNational);
            sets.add(CHString.IndicatorFundOther);
            sets.add(CHString.IncomeDevelop);
            sets.add(CHString.IncomeTransfer);

            if (sets.contains(row.getIndicatortype())) {
                row.setIndicatortypeDetail("详情");
            }

            if (row.getIndicatortype().equals("加分申请")) {
                row.setIndicatortypeDetail("申请详情");
            }
        }

        return list;
    }


    public Integer selectPerTeamperformancePointsSum(PerTeamperformance record) {

        Integer pointssum = teamperformanceMapper.selectPerTeamperformancePointsSum(record);

        return pointssum;
    }

    public PerConfirmqequest selectPerConfirmRequestByTeamYear(PerConfirmqequest record) {

        List<PerConfirmqequest> list = confirmqequestMapper.selectPerConfirmRequest(record);

        PerConfirmqequest quest = new PerConfirmqequest();

        if (list.size() > 0) {
            quest = list.get(0);
        }

        return quest;
    }

    @Transactional
    public Integer insertPerConfirmRequest(PerConfirmqequest record) {
        Integer result = 1;

        result = confirmqequestMapper.insertPerConfirmqequest(record);


        return result;
    }


    @Transactional
    public Integer updatePerTeamperformancePoint(PerTeamperformance record, PerScoreschangelog scoreschangelog) {
        Integer result = 1;
        //修改分数

        result = teamperformanceMapper.updatePerTeamperformancePoint(record);
        ////记录日志

        scoreschangelogMapper.insertPerScoreschangelog(scoreschangelog);

        return result;
    }

    @Transactional
    public Integer updatePerTeamperformance(PerTeamperformance record) {
        Integer result = 1;

        result = teamperformanceMapper.updatePerTeamperformance(record);

        return result;
    }

    public Integer updatePerTeamperformanceStatus(PerTeamperformance record) {
        Integer result = 1;

        result = teamperformanceMapper.updatePerTeamperformanceStatus(record);

        return result;
    }


    public List<PerScoreschangelog> selectPerScoreschangelog(PerScoreschangelog scoreschangelog) {

        List<PerScoreschangelog> list = scoreschangelogMapper.selectPerScoreschangelog(scoreschangelog);

        return list;
    }


    @Transactional
    public Integer updatePerTeamperformanceDeletedById(PerTeamperformance record) {
        Integer result = 1;
        //修改分数

        result = teamperformanceMapper.updatePerTeamperformanceDeletedById(record);


        return result;
    }


    public class IndicatorTree {
        private String type;
        private List<PerIndicator> childList;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<PerIndicator> getChildList() {
            return childList;
        }

        public void setChildList(List<PerIndicator> childList) {
            this.childList = childList;
        }

        @Override
        public String toString() {
            return "IndicatorTree{" +
                    "type='" + type + '\'' +
                    ", childList=" + childList +
                    '}';
        }
    }


    // 考核指标 树
    public List<IndicatorTree> selectPerIndictorTree() {

        List<PerIndicator> list = indicatorMapper.selectPerIndicatorTree();

        List<IndicatorTree> trees = new ArrayList<>();

        List<PerIndicator> list1 = new ArrayList<>();
        for (PerIndicator perIndicator : list) {
            if (perIndicator.getParentid() == null) {
                list1.add(perIndicator);
            }
        }

        log.debug("list1 is " + list1.toString());

        list1.forEach(indicator -> {

            IndicatorTree tempTree = new IndicatorTree();
//            log.debug("tempTree is " + tempTree.toString());

            for (IndicatorTree tree : trees) {
                if (tree.getType().equals(indicator.getIndicatortype())) {
                    tempTree = tree;
                }
            }

            if (tempTree.getType() == null) {
                IndicatorTree kk = new IndicatorTree();
                List<PerIndicator> childList = new ArrayList<>();
                childList.add(indicator);
                kk.setChildList(childList);
                kk.setType(indicator.getIndicatortype());
                trees.add(kk);
            }
            else {
                tempTree.getChildList().add(indicator);
            }
        });


        List<PerIndicator> list2 = new ArrayList<>();
        for (PerIndicator perIndicator : list) {
            if (perIndicator.getIndicatorlevel() == 2) {
                list2.add(perIndicator);
            }
        }

        list2.forEach(indicator2 -> {

            for (IndicatorTree tree : trees) {
                tree.getChildList().forEach(indicator1 -> {
                    if (indicator1.getIndicatorid().equals(indicator2.getParentid())) {

                        if (indicator1.getChildList() == null) {
                            List<PerIndicator> ll = new ArrayList<>();
                            ll.add(indicator2);
                            indicator1.setChildList(ll);
                        }
                        else {
                            indicator1.getChildList().add(indicator2);
                        }

                    }

                });
            }
        });

        for (IndicatorTree tree : trees) {

            tree.getChildList().sort((o1, o2) -> o1.getIndicatorname().compareTo(o2.getIndicatorname()));

        }

        List<PerIndicator> list3 = new ArrayList<>();
        for (PerIndicator perIndicator : list) {
            if (perIndicator.getIndicatorlevel() == 3) {
                list3.add(perIndicator);
            }
        }

        list3.forEach(indicator3 -> {
            for (IndicatorTree tree : trees) {
                tree.getChildList().forEach(indicator1 -> {
                    indicator1.getChildList().forEach(indicator2 -> {
                        if (indicator2.getIndicatorid().equals(indicator3.getParentid())) {

                            if (indicator2.getChildList() == null) {
                                List<PerIndicator> ll = new ArrayList<>();
                                ll.add(indicator3);
                                indicator2.setChildList(ll);
                                log.debug("indicator3 is " + indicator3.toString());
                            }
                            else {
                                indicator2.getChildList().add(indicator3);
                            }

                        }
                    });
                });
            }
        });
        return trees;
    }


    @Transactional
    public Integer insertPerIndicator(PerIndicator record) {

        Integer  result = indicatorMapper.insertPerIndicator(record);

        return result;
    }

    @Transactional
    public Integer updatePerIndicator(PerIndicator record) {

        Integer  result = indicatorMapper.updatePerIndicator(record);


        return result;
    }


    @Transactional
    public Integer updatePerIndicatorDeletedById(PerIndicator record) {

        Integer  result = indicatorMapper.updatePerIndicatorDeleted(record);


        return result;
    }
//
//    public List<PerAddscoreapply> selectAddscoreapply(PerAddscoreapply record) {
//
//        List<PerAddscoreapply> list = addscoreapplyMapper.selectPerAddscoreapply(record);
//
//        return list;
//    }
//
//    public PerAddscoreapply selectAddscoreapplyById(Integer applyid) {
//
//        PerAddscoreapply addscoreapply = addscoreapplyMapper.selectPerAddscoreapplyById(applyid);
//
//        return addscoreapply;
//    }
//
//
//    private Integer sendAddscoreapplyConfirmMsg(PerAddscoreapply record) {
//        Integer result = 0;
//        //给相关的负责人发信息
//
//        SysUserMenu userMenu = new SysUserMenu();
//        userMenu.setMenuId(Long.valueOf(MID.ScoreApplyAudit));
//
//        List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);
//
//        Set<Long> useridSet = new HashSet<>();
//        for (SysUserMenu um : userMenuList) {
//            useridSet.add(um.getUserId());
//        }
//
//
//        for (Long userid : useridSet) {
//
//            String title =  "新的绩效考核加分申请待审核";
//            String content =  "团队：“" + record.getTeamidlinktext() + "”的绩效考核加分申请待审核。"
//                    + "<a href=\"/Performance/ApplyList_ToConfirm.aspx?"
//                    + "kid=" + record.getApplyid() + "&f=todo\" target=\"_self\" >查看</a> ";
//            //查询消息要发送给哪个人员
//            AudMessage message = new AudMessage();
//            message.setTouserid(Integer.valueOf(userid.toString()));
//            message.setRelatedsheettype("加分申请");
//            message.setRelatedsheetid(record.getApplyid());
//            messageMapper.insertAudMessage(message);
//            result = 1;
//        }
//
//        return  result;
//    }
//
//    private Integer updateAddscoreapplyBasDocAttachToType(PerAddscoreapply record) {
//
//        Integer result = 0;
//        for (BasDoc file : record.getFileList()) {
//            BasDoc doc = new BasDoc();
//            doc.setDocid(file.getDocid());
//
//            doc.setAttachtotype("加分申请");
//            doc.setRelatedid(record.getApplyid());
//            doc.setDoctype("证明材料");
//
//            basDocMapper.updateBasDocAttachToType(doc);
//        }
//
//        return result;
//    }
//
//    @Transactional
//    public Integer insertAddscoreapply(PerAddscoreapply record) {
//
//        Integer  result = addscoreapplyMapper.insertPerAddscoreapply(record);
//
//        updateAddscoreapplyBasDocAttachToType(record);
//
//        //给相关的负责人发信息
//        sendAddscoreapplyConfirmMsg(record);
//
//        return result;
//    }
//
//    @Transactional
//    public Integer updateAddscoreapply(PerAddscoreapply record) {
//
//        if (record.getApplystatus().equals("审核不通过")) {
//            record.setApplystatus("待审核");
//
//            //给相关的负责人发信息
//            sendAddscoreapplyConfirmMsg(record);
//        }
//
//        Integer  result = addscoreapplyMapper.updatePerAddscoreapply(record);
//
//        // 删除掉原来的已有未出现的证明材料。
//        BasDoc doc = new BasDoc();
//        doc.setAttachtotype("加分申请");
//        doc.setRelatedid(record.getApplyid());
//        doc.setDoctype("证明材料");
//
//        for (BasDoc file : basDocMapper.selectBasDocList(doc)) {
//            Integer docid = file.getDocid();
//            for (BasDoc file2 : record.getFileList()) {
//                if (docid == file.getDocid()) {
//                    docid = -1;
//                    break;
//                }
//            }
//            if (docid != -1) {
//                BasDoc doc2 = new BasDoc();
//                doc2.setDocid(docid);
//
//                doc2.setAttachtotype("加分申请");
//                doc2.setRelatedid(null);
//                doc2.setDoctype("证明材料");
//
//                basDocMapper.updateBasDocAttachToType(doc2);
//            }
//        }
//
//        updateAddscoreapplyBasDocAttachToType(record);
//
//        return result;
//    }
//
//
//    @Transactional
//    public Integer updateAddscoreapplyDeletedById(PerAddscoreapply record) {
//
//        Integer  result = addscoreapplyMapper.updatePerAddscoreapplyDeletedById(record.getApplyid());
//
//        return result;
//    }
//
//    @Transactional
//    public Integer updateAddscoreapplyConfirm(PerAddscoreapply record) {
//
//        Integer  result = addscoreapplyMapper.updatePerAddscoreapplyConfirm(record);
//
//
//        if (record.getAuditResult().equals("1")) {
//            //生成团队绩效记录
//            PerTeamperformance teamperformance = new PerTeamperformance();
//
//            teamperformance.setTeamid(record.getTeamid());
//            teamperformance.setPerformanceyear(record.getYear());
//            teamperformance.setIndicatortype("加分申请");
//            teamperformance.setIndicatorid(-1);
//            teamperformance.setAchieveid(record.getApplyid());
//
//            String description = record.getLevel3name();
//            teamperformance.setDescription(description);
//            teamperformance.setPoints(record.getApplyscores());
//            teamperformance.setLevel1id(record.getLevel1id());
//            teamperformance.setLevel1name(record.getLevel1name());
//            teamperformance.setLevel2id(record.getLevel2id());
//            teamperformance.setLevel2name(record.getLevel2name());
//            result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);
//
//
//            //如果设置的分数与申请分数不同，则还要记录日志
//            if (record.getLastApplyscores() != record.getApplyscores()) {
//                String des = "申请分数为" + record.getLastApplyscores() + "，审核分数为" + record.getApplyscores();
//
//                PerScoreschangelog scoreschangelog = new PerScoreschangelog();
//                scoreschangelog.setDescription(des);
//                scoreschangelog.setPerformanceid(teamperformance.getPerformanceid());
//                scoreschangelog.setOldscores(record.getLastApplyscores());
//                scoreschangelog.setNewscores(record.getApplyscores());
//                scoreschangelog.setReason(record.getAuditopinion());
//                scoreschangelog.setUserid(record.getAudituserid());
//                scoreschangelog.setTime(DateUtils.dateTimeNow());
//
//                scoreschangelogMapper.insertPerScoreschangelog(scoreschangelog);
//
//            }
//
//            //消除"审核人"待办事项
//
//
//            SysUserMenu userMenu = new SysUserMenu();
//            userMenu.setMenuId(Long.valueOf(MID.ScoreApplyAudit));
//
//            List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);
//
//            Set<Long> useridSet = new HashSet<>();
//            for (SysUserMenu um : userMenuList) {
//                useridSet.add(um.getUserId());
//            }
//
//            for (Long userid : useridSet) {
//                AudMessage message = new AudMessage();
//                message.setProcessedtime(DateUtils.dateTimeNow());
//                message.setTouserid(userid.intValue());
//                message.setRelatedsheettype("加分申请");
//                message.setRelatedsheetid(record.getApplyid());
//                messageMapper.updateIfProcessedById(message);
//            }
//
//            //发送消息给提交人
//            String title = "";
//            String content = "";
//            if (record.getAuditResult().equals("1"))  //通过
//            {
//                title = "您提交的加分申请审核通过";
//                content = "关于“" + record.getLevel3name() + "”加分申请的信息已经审核通过。"
//                        + "<a href=\"/Performance/AddScoreApplyList.aspx?"
//                        + "kid=" + record.getApplyid() + "&f=todo\" target=\"_self\" >查看</a> ";
//            }
//            else
//            {
//                title = "您提交的加分申请审核不通过";
//                content = "关于“" + record.getLevel3name() + "”的加分申请的信息审核不通过。"
//                        + "<a href=\"/Performance/AddScoreApplyList.aspx?"
//                        + "kid=" + record.getApplyid()+ "&f=todo\" target=\"_self\" >查看</a> ";
//            }
//
//            AudMessage message = new AudMessage();
//
//            message.setTouserid(record.getApplyuserid());
//            message.setRelatedsheettype("加分申请");
//            message.setRelatedsheetid(record.getApplyid());
//            messageMapper.insertAudMessage(message);
//
//        }
//
//
//        return result;
//    }


}

package com.ruoyi.achieve.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchArticle;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchArticleMapper;
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
import com.ruoyi.performance.service.TeamPerformanceService;
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
public class AchArticleService {
    private static final Logger log = LoggerFactory.getLogger(AchArticleService.class);

    @Resource
    private AchArticleMapper articleMapper;

    @Resource
    private AchAuthorMapper authorMapper;

    @Resource
    private BasDocMapper basDocMapper;
    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private TeamPerformanceService teamperformanceService;

    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;

    private String ACHIEVETYPE = AchieveType.ARTICLE.getInfo();

    public List<AchArticle> selectAchArticleList(AchArticle article) {

        List<AchArticle> articleList = articleMapper.selectAchArticle(article);

        for (AchArticle p : articleList) {
            Integer articleid = p.getArticleid();
            AchAuthor query = new AchAuthor();
            query.setAchievetype(ACHIEVETYPE);
            query.setRelatedid(articleid);

            List<AchAuthor> achAuthorList = authorMapper.selectAchAuthorList(query);
            List<String> personnames = new ArrayList<>();
            for (AchAuthor a : achAuthorList) {
                personnames.add(a.getPersonname());
            }
            String authors = StringUtils.joinWith(",", personnames);

            p.setAuthorList(achAuthorList);
            p.setAuthors(authors);

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
        log.debug("AchArticle list is request.");
        return articleList;
    }

    public AchArticle selectAchArticleById(Integer articleid) {

        AchArticle p =  articleMapper.selectAchArticleById(articleid);
        if (p == null) {
            return null;
        }

        AchAuthor query = new AchAuthor();
        log.debug("ACHIEVETYPE is " + ACHIEVETYPE + " articleid is " + articleid.toString());
        query.setAchievetype(ACHIEVETYPE);
        query.setRelatedid(articleid);

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


    public Integer queryIfDuplicate(AchArticle article) {
        return articleMapper.queryIfDuplicate(article);
    }




    public Integer updateArticleDetail(AchArticle article) {

        Integer articleid = article.getArticleid();

        //保存对应的作者
        List<AchAuthor> authors = article.getAuthorList();
        log.debug("authors is " + authors.toString());

        AchAuthor query = new AchAuthor();
        query.setAchievetype(ACHIEVETYPE);
        query.setRelatedid(article.getArticleid());
        log.debug("deleteAchAuthor is " + query.toString());
        authorMapper.deleteAchAuthor(query);
        Integer i = 1;
        for(AchAuthor author : authors) {
            author.setRelatedid(articleid);
            author.setAchievetype(ACHIEVETYPE);
            author.setOrdernumber(i);

            if (author.getIfreporter() == null) {
                author.setIfreporter(false);
            }

            authorMapper.insertAchAuthor(author);
            i = i + 1;
        }

        //保存对应的文档
        // 同步 文件。 使用merge方式。
        BasDoc recordDoc = new BasDoc();
        recordDoc.setRelatedid(articleid);

        List<BasDoc> existedDocList = basDocMapper.selectBasDocList(recordDoc);

        for (BasDoc doc : existedDocList) {
            doc.setDoctype(null);
            doc.setRelatedid(null);
            doc.setAttachtotype(null);
            basDocMapper.updateBasDocAttachToType(doc);
        }

        List<BasDoc> doclist = article.getDocList();
        log.debug("article.getDocList is " + doclist.toString());

        for (BasDoc s : doclist) {
            s.setAttachtotype(ACHIEVETYPE);
            s.setRelatedid(articleid);
            log.debug("updateBasDocAttachToType is " + s.toString());
            basDocMapper.updateBasDocAttachToType(s);
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

    public void sendApplyAndMsg(AchArticle article) {

        String title = "新的科技成果待审核";
        String content = "著作：“" + article.getArticlename()  + "”的信息待审核。"
                + "<a href=\"/Achieve/ToConfirmList.aspx?at=" + ACHIEVETYPE
                + "&kid=" + article.getArticleid() + "&f=todo\" target=\"_self\" >查看</a> ";

        AudApply apply = new AudApply();
        apply.setApplytype(ACHIEVETYPE);
        apply.setRelatedid(article.getArticleid());
        apply.setApplyuserid(article.getCreateuserid());
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
            message.setRelatedsheetid(article.getArticleid());
            messageMapper.insertAudMessage(message);
        }

    }



    @Transactional
    public Integer insertAchArticle(AchArticle article) {
        article.setCreatetime(DateUtils.dateTimeNow());
        article.setArticleid(-1);
        Integer rows = articleMapper.insertAchArticle(article);
        updateArticleDetail(article);
        //发申请和通知
        sendApplyAndMsg(article);
        return  rows;
    }

    @Transactional
    public Integer updateAchArticle(AchArticle article) {

        AchArticle undoArticle = articleMapper.selectAchArticleById(article.getArticleid());

        Integer rows = articleMapper.updateAchArticle(article);
        this.updateArticleDetail(article);

        if (undoArticle.getStatus() == AchieveStatus.BuTongGuo.getCode()) {
            //发申请和通知
            sendApplyAndMsg(article);
        }

        return  rows;
    }


    @Transactional
    public Integer confirmAchArticle(AchArticle article) {

        AchArticle record = new AchArticle();
        record.setArticleid(article.getArticleid());
        if (article.getConfirmResult() == 1) {
            record.setStatus(AchieveStatus.ZhengChang.getCode());

            //审核通过时，团队绩效加分
            teamperformanceService.addTeamperformancesForArticle(article);

        }
        else if (article.getConfirmResult() == 2) {
            record.setStatus(AchieveStatus.BuTongGuo.getCode());
        }
        else {return 0;}

        log.debug("updateAchArticleStatus is " + record.toString());

        articleMapper.updateAchArticleStatus(record);

        AudApply apply = new AudApply();
        apply.setApplyid(article.getApplyid());
        apply.setAudituserid(article.getConfirmUserid());
        apply.setAudittime(DateUtils.dateTimeNow());
        apply.setAuditopinion(article.getConfirmNote());
        apply.setApplystatus(article.getConfirmResult());

        applyMapper.updateAudApply(apply);


        //消除"审核人"待办事项
        Set<Long> useridSet = getConfirmUserIdList();
        for (Long userid : useridSet) {
            AudMessage message = new AudMessage();
            message.setProcessedtime(DateUtils.dateTimeNow());
            message.setTouserid(userid.intValue());
            message.setRelatedsheettype(ACHIEVETYPE);
            message.setRelatedsheetid(article.getArticleid());
            messageMapper.updateIfProcessedById(message);
        }

        String title = "";
        String content = "";
        if (article.getConfirmResult() == 1) {
            title = "您提交的科技成果审核通过";
            content = "著作：“" + article.getArticlename() + "”的信息已经审核通过。"
                    + "<a href=\"/Achieve/ArticleList.aspx?"
                    + "kid=" + article.getArticleid() + "&f=todo\" target=\"_self\" >查看</a> ";
        }
        else if (article.getConfirmResult() == 2) {
            title = "您提交的科技成果审核不通过";
            content = "著作：“" + article.getArticlename()  + "”的信息审核不通过。"
                    + "<a href=\"/Achieve/ArticleList.aspx?"
                    + "kid=" + article.getArticleid() + "&f=todo\" target=\"_self\" >查看</a> ";
        }

        //发送通知
        //发送给提交人
        AudMessage message = new AudMessage();
        message.setMessagetime(DateUtils.dateTimeNow());
        message.setMessagetitle(title);
        message.setMessagecontent(content);
        message.setTouserid(article.getCreateuserid());
        message.setRelatedsheettype(ACHIEVETYPE);
        message.setRelatedsheetid(article.getArticleid());
        messageMapper.insertAudMessage(message);

        return  1;
    }

    public AudApply selectApplyByTypeAndRelatedID(Integer articleid, Integer status) {

        AudApply apply = new AudApply();

        if (status == AchieveStatus.BuTongGuo.getCode()) {
            apply.setApplytype(AchieveType.PATENT.getInfo());
        }
        else {
            return  null;
        }
        apply.setRelatedid(articleid);
        return applyMapper.selectApplyByTypeAndRelatedID(apply);
    }


    @Transactional
    public Integer removeAchArticle(Integer articleid) {

        AchArticle record = new AchArticle();
        record.setArticleid(articleid);
        record.setStatus(AchieveStatus.YiShanChu.getCode());
        Integer rows = articleMapper.updateAchArticleStatus(record);
        //如果该成果对应的有待审核的“申请”和消息，则清除掉

        AudApply apply = new AudApply();

        apply.setApplytype(AchieveType.PATENT.getInfo());
        apply.setRelatedid(articleid);
        apply.setApplystatus(0);
        AudApply apply2 = applyMapper.selectApplyByTypeAndRelatedID(apply);

        if (apply2 != null) {
            applyMapper.deleteAudApplyById(apply2.getApplyid());
            AudMessage message = new AudMessage();
            message.setRelatedsheettype(AchieveType.PATENT.getInfo());
            message.setRelatedsheetid(articleid);
            messageMapper.updateIfProcessedAudMessageBySheetAndId(message);
        }

        //删掉对应的绩效计分记录
        teamperformanceService.deletePerTeamperformances(ACHIEVETYPE, articleid);

        return  rows;
    }
        

}
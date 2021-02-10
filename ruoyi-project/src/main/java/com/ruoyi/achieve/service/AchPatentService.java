package com.ruoyi.achieve.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchPatentMapper;
import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.AudProjectdoc;
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
public class AchPatentService {
    private static final Logger log = LoggerFactory.getLogger(AchPatentService.class);

    @Resource
    private AchPatentMapper patentMapper;

    @Resource
    private AchAuthorMapper authorMapper;

    @Resource
    private BasDocMapper basDocMapper;

    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;

    private String achieveType = "专利";

    public List<AchPatent> selectAchPatentList(AchPatent patent) {
        List<AchPatent> patentList = patentMapper.selectAchPatentList(patent);

        for (AchPatent p : patentList) {
            Integer patentid = p.getPatentid();
            AchAuthor query = new AchAuthor();
            query.setAchievetype(achieveType);
            query.setRelatedid(patentid);

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
        log.debug("AchPatent list is request.");
        return patentList;
    }

    public AchPatent selectAchPatentById(Integer patentid) {

        AchPatent p =  patentMapper.selectAchPatentById(patentid);

        AchAuthor query = new AchAuthor();
        query.setAchievetype(achieveType);
        query.setRelatedid(patentid);

        List<AchAuthor> achAuthorList = authorMapper.selectAchAuthorList(query);
        List<String> personnames = new ArrayList<>();
        for (AchAuthor a : achAuthorList) {
            personnames.add(a.getPersonname());
        }
        String authors = StringUtils.joinWith(",", personnames);

        p.setAuthorList(achAuthorList);
        p.setAuthors(authors);

        return  p;
    }

    public Integer queryIfDuplicate(AchPatent patent) {
        return patentMapper.queryIfDuplicate(patent);
    }



    public Integer updatePatentDetail(AchPatent patent) {

        Integer patentid = patent.getPatentid();

        //保存对应的作者
        List<AchAuthor> authors = patent.getAuthorList();
        AchAuthor query = new AchAuthor();
        query.setAchievetype(achieveType);
        query.setRelatedid(patent.getPatentid());
        log.debug("deleteAchAuthor is " + query.toString());
        authorMapper.deleteAchAuthor(query);
        Integer i = 1;
        for(AchAuthor author : authors) {
            author.setRelatedid(patentid);
            author.setAchievetype(achieveType);
            author.setOrdernumber(i);
            author.setIfreporter(false);
            authorMapper.insertAchAuthor(author);
            i = i + 1;
        }

        //保存对应的文档
        // 同步 文件。 使用merge方式。
        List<BasDoc> doclist = patent.getDocList();
        log.debug("patent.getDocList is " + doclist.toString());

        for (BasDoc s : doclist) {
            s.setAttachtotype(achieveType);
            s.setRelatedid(patentid);
            log.debug("updateBasDocAttachTo is " + s.toString());
            basDocMapper.updateBasDocAttachTo(s);
        }

        return 1;
    }

//    /////生成确认申请，并发送消息通知相关人员
//    string title = "新的科技成果待审核";
//    string content = "专利：“" + txt_PatentName.Text + "”的信息待审核。"
//            + "<a href=\"/Achieve/ToConfirmList.aspx?at=" + Server.UrlEncode(ConstantParameter.CHString_Patent)
//            + "&kid=" + kid + "&f=todo\" target=\"_self\" >查看</a> ";
//
//            AchieveCommonFunc.SendApplyAndMsg(title, content, ConstantParameter.CHString_Patent,
//    kid, Session["CurrentUserID"].ToString());
//提交审核申请
//Apply apply = new Apply();
//    apply.ApplyType = achieveType;
//    apply.RelatedID = int.Parse(relatedID);
//    apply.ApplyUserID = int.Parse(currentUserID);
//    apply.ApplyTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//    IApplyManager iaplym = ApplyManager.GetInstance();
//            iaplym.AddNew(apply);
//
//    //发送通知消息(发送给具有“科技成果审核”功能的人员)
//    IPermissionManager iperm = PermissionManager.GetInstance();
//    DataSet ds = iperm.GetUserListToModule(ConstantParameter.MID_AchieveConfirm);
//
//    //循环
//            for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
//    {
//        AudMessage am = new AudMessage();
//        am.MessageTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//        am.MessageTitle = title;
//        am.MessageContent = content;
//        //查询消息要发送给哪个人员
//        //发送给项目的负责人
//        am.ToUserID = int.Parse(ds.Tables[0].Rows[i]["UserID"].ToString());
//        am.RelatedSheetType = achieveType;
//        am.RelatedSheetID = int.Parse(relatedID);
//        IAudMessageManager iamm = AudMessageManager.GetInstance();
//        iamm.AddNew(am);
//    }
//            ds.Dispose();

    public void sendApplyAndMsg(AchPatent patent) {

        String title = "新的科技成果待审核";
        String content = "专利：“" + patent.getPatentname()  + "”的信息待审核。"
            + "<a href=\"/Achieve/ToConfirmList.aspx?at=" + achieveType
            + "&kid=" + patent.getPatentid() + "&f=todo\" target=\"_self\" >查看</a> ";

        AudApply apply = new AudApply();
        apply.setApplytype(achieveType);
        apply.setRelatedid(patent.getPatentid());
        apply.setApplyuserid(patent.getCreateuserid());
        apply.setApplytime(DateUtils.dateTimeNow());
        apply.setApplyreason("");
        apply.setApplystatus(0);
        applyMapper.insertAudApply(apply);

    //发送通知消息(发送给具有“科技成果审核”功能的人员)
        String perms = "achieve:toconfirm:list";

        SysUserMenu userMenu = new SysUserMenu();
        userMenu.setPerms(perms);

        List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

        Set<Long> useridSet = new HashSet<>();
        for (SysUserMenu um : userMenuList) {
            useridSet.add(um.getUserId());
        }
        log.debug("send message " + achieveType + " userid is " + useridSet.toString());

        for (Long userid : useridSet) {
                    //查询消息要发送给哪个人员
        //发送给项目的负责人
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            message.setTouserid(userid.intValue());
            message.setRelatedsheettype(achieveType);
            message.setRelatedsheetid(patent.getPatentid());
            messageMapper.insertAudMessage(message);
        }

    }

    public Integer insertAchPatent(AchPatent patent) {
        patent.setCreatetime(DateUtils.dateTimeNow());
        patent.setProjectid(-1);
        Integer rows = patentMapper.insertAchPatent(patent);
        updatePatentDetail(patent);
        //发申请和通知
        sendApplyAndMsg(patent);
        return  rows;
    }

    public Integer updateAchPatent(AchPatent patent) {

        AchPatent undoPatent = patentMapper.selectAchPatentById(patent.getPatentid());

        Integer rows = patentMapper.updateAchPatent(patent);
        updatePatentDetail(patent);

        if (undoPatent.getStatus() == AchieveStatus.BuTongGuo.getCode()) {
            //发申请和通知
            sendApplyAndMsg(patent);
        }

        return  rows;
    }

    public Integer updateStatusAchPatent(AchPatent patent) {
        return patentMapper.updateStatusAchPatent(patent);
    }

    public Integer confirmAchPatent(AchPatent patent) {

        AchPatent record = new AchPatent();
        record.setPatentid(patent.getPatentid());
        if (patent.getConfirmResult() == 1) {
            record.setStatus(AchieveStatus.ZhengChang.getCode());

            //审核通过时，团队绩效加分, 后续开发，等绩效模块到位。
//            if (Confirm1.rbtl_Result.SelectedValue == "1")  //通过
//            {
//                ITeamPerformanceManager iteamper = TeamPerformanceManager.GetInstance();
//                iteamper.AddRecord_FromPatent(Request["kid"]);
//            }
        }
        else if (patent.getConfirmResult() == 2) {
            record.setStatus(AchieveStatus.BuTongGuo.getCode());
        }
        else {return 0;}

        patentMapper.updateStatusAchPatent(record);



        return  1;
    }

}
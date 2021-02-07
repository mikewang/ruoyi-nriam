package com.ruoyi.project.service;

import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.AudProjectdoc;
import com.ruoyi.project.domain.PmProjectacceptance;
import com.ruoyi.project.mapper.AudProjectMapper;
import com.ruoyi.project.mapper.AudProjectdocMapper;
import com.ruoyi.project.mapper.PmProjectacceptanceMapper;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PmProjectacceptanceService {
    private static final Logger log = LoggerFactory.getLogger(PmProjectacceptanceService.class);

    @Resource
    private PmProjectacceptanceMapper mapper;


    @Resource
    private AudProjectdocMapper projectdocMapper;

    @Resource
    private BasDocMapper basDocMapper;

    @Resource
    private AudProjectMapper audProjectMapper;

    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;

    public PmProjectacceptance selectProjectacceptanceById(Integer projectId) {

        return mapper.selectProjectacceptanceById(projectId);
    }

    private void sendMessage(String projectname, Integer projectid, String relatedsheettype) {
        String title =  "项目验收信息待审核";
        String content = "项目：“" + projectname+ "”的验收信息待审核。"
                + "<a href=\"/Project/ToAcceptanceConfirmList.aspx?"
                + "kid=" + projectid.toString() + "&f=todo\" target=\"_self\" >查看</a> ";

        // 下面写操作代码。

        String perms = "project:toacceptanceconfirm:list";

        SysUserMenu userMenu = new SysUserMenu();
        userMenu.setPerms(perms);

        List<SysUserMenu> userMenuList = userMenuMapper.selectUserMenuList(userMenu);

        Set<Long> useridSet = new HashSet<>();
        for (SysUserMenu um : userMenuList) {
            useridSet.add(um.getUserId());
        }
        log.debug("send message " + relatedsheettype + " userid is " + useridSet.toString());

        for (Long userid : useridSet) {
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            message.setTouserid(userid.intValue());
            message.setRelatedsheettype(relatedsheettype);
            message.setRelatedsheetid(projectid);
            messageMapper.insertAudMessage(message);
        }

    }

    public int mergeProjectacceptance(PmProjectacceptance record) {

        // 同步 项目文件。 使用merge方式。
        List<AudProjectdoc> doclist = record.getProjectdocList();

        for (AudProjectdoc s : doclist) {
            s.setProjectid(record.getProjectid());
            s.setIfdeleted(false);
        }

        log.debug("doclist is " + doclist.toString());

        if (doclist.size() > 0) {
            Integer rows = projectdocMapper.mergeProjectdoc(doclist);
            rows = projectdocMapper.sourceMergeProjectdoc(doclist);
        } else {
            AudProjectdoc query = new AudProjectdoc();
            query.setProjectid(record.getProjectid());
            Integer rows = projectdocMapper.deleteProjectdoc(query);
        }

        Set<String> s1 = new HashSet<>();
        s1.add("验收申请书");
        s1.add("验收证书");
        s1.add("技术总结");
        s1.add("工作总结");
        s1.add("查新报告");
        s1.add("审计报告");
        s1.add("检测报告");
        s1.add("其它附件");
        s1.add("用户证明");
        s1.add("成果照片视频");

        for (AudProjectdoc s : doclist) {
           // log.debug("docid = " + s.getDocid().toString() + " getDoctype is " + s.getDoctype());
            if (s1.contains(s.getDoctype())) {
                BasDoc doc = new BasDoc();
                doc.setDocid(s.getDocid());
                doc.setAttachtotype("验收信息");
                doc.setDoctype(s.getDoctype());
                doc.setRelatedid(s.getProjectid());
                basDocMapper.updateBasDocAttachTo(doc);
            }
        }

        if (record.getStatus() == ProjectStatus.YiJieTi.getCode()) {
            AudProject project = new AudProject();
            project.setProjectid(record.getProjectid());
            project.setIfacceptancefull(true);
            audProjectMapper.updateIfAcceptanceFull(project);
        }
        else {
            AudProject project = new AudProject();
            project.setProjectid(record.getProjectid());
            project.setStatus(ProjectStatus.JieTiDaiQueRen.getCode());
            audProjectMapper.updateProjectStatus(project);
        }

        mapper.deleteProjectacceptanceById(record.getProjectid());

        Integer rows = mapper.insertProjectacceptance(record);

        if (record.getStatus() != ProjectStatus.YiJieTi.getCode()) {
            //发送通知
            //发送消息
            sendMessage(record.getProjectname(), record.getProjectid(), "项目验收");

//        string title = "项目验收信息待审核";
//        string content = "项目：“" + lab_ProjectName.Text + "”的验收信息待审核。"
//                + "<a href=\"/Project/ToAcceptanceConfirmList.aspx?"
//                + "kid=" + lab_ProjectID.Text + "&f=todo\" target=\"_self\" >查看</a> ";
//        ProjectCommonFunc.SendMessage(title, content, "项目验收", lab_ProjectID.Text, ConstantParameter.MID_ProjectAcceptanceConfirm);
        }
        return rows;
    }

    public int insertProjectacceptance(PmProjectacceptance record) {

        // 同步 项目文件。 使用merge方式。
        List<AudProjectdoc> doclist = record.getProjectdocList();

        for (AudProjectdoc s : doclist) {
            s.setProjectid(record.getProjectid());
            s.setIfdeleted(false);
        }

        log.debug("doclist is " + doclist.toString());

        if (doclist.size() > 0) {
            Integer rows = projectdocMapper.mergeProjectdoc(doclist);
            rows = projectdocMapper.sourceMergeProjectdoc(doclist);
        } else {
            AudProjectdoc query = new AudProjectdoc();
            query.setProjectid(record.getProjectid());
            Integer rows = projectdocMapper.deleteProjectdoc(query);
        }

        AudProject project = new AudProject();
        project.setProjectid(record.getProjectid());
        project.setStatus(ProjectStatus.JieTiDaiQueRen.getCode());

        audProjectMapper.updateProjectStatus(project);

        return mapper.insertProjectacceptance(record);
    }

    public int updateProjectacceptance(PmProjectacceptance record) {

        // 同步 项目文件。 使用merge方式。
        List<AudProjectdoc> doclist = record.getProjectdocList();

        for (AudProjectdoc s : doclist) {
            s.setProjectid(record.getProjectid());
            s.setIfdeleted(false);
        }

        log.debug("doclist is " + doclist.toString());

        if (doclist.size() > 0) {
            Integer rows = projectdocMapper.mergeProjectdoc(doclist);
            rows = projectdocMapper.sourceMergeProjectdoc(doclist);
        } else {
            AudProjectdoc query = new AudProjectdoc();
            query.setProjectid(record.getProjectid());
            Integer rows = projectdocMapper.deleteProjectdoc(query);
        }
        return mapper.updateProjectacceptance(record);
    }

}
package com.ruoyi.fourtech.service;


import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.FourtechStatus;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.ConvertUpMoney;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SMSSender.Juhe;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.contract.domain.AudContract;
import com.ruoyi.contract.domain.AudContractdoc;
import com.ruoyi.contract.domain.AudContractpay;
import com.ruoyi.contract.mapper.AudContractdocMapper;
import com.ruoyi.fourtech.domain.AudFourtech;
import com.ruoyi.fourtech.mapper.AudFourtechMapper;
import com.ruoyi.project.domain.DocFile;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.sheet.domain.*;
import com.ruoyi.sheet.mapper.*;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AudFourtechService {

    private static final Logger log = LoggerFactory.getLogger(AudFourtechService.class);

    @Resource
    AudFourtechMapper audFourtechMapper;

    @Resource
    AudSheetauditrecordMapper audSheetauditrecordMapper;

    @Resource
    private AudContractdocMapper contractdocMapper;


    @Resource
    AudBudgetpayMapper audBudgetpayMapper;

//    @Resource
//    AudFourtechauditrecordMapper audFourtechauditrecordMapper;


    @Resource
    SrmSupplierinfoMapper supplierinfoMapper;

    @Resource
    BasUsedmaxserialnumberMapper usedmaxserialnumberMapper;

    @Resource
    private AppClientinfoMapper clientinfoMapper;

    @Resource
    private AudMessageMapper messageMapper;

    @Resource
    private SysDeptMapper deptMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysDictDataMapper dictDataMapper;

    @Resource
    private BasDocMapper basDocMapper;


    public List<AudFourtech> selectFourtechTijiaoren(AudFourtech fourtech) {

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechTijiaoren(fourtech);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudFourtech> selectFourtechTijiaorenByUserid(Integer uid) {

        List<AudFourtech> list = audFourtechMapper.selectFourtechTijiaorenByUserid(uid);

        log.debug("request sheetList list is " + list.toString());

        return list;
    }

    public AudFourtech selectFourtechById(Integer sheetid) {

        AudFourtech sheet = audFourtechMapper.selectFourtechById(sheetid);

        return sheet;
    }


    @Transactional
    public Integer addFourtechTijiaoren(AudFourtech contract) {
        Integer result = 1;

        result = audFourtechMapper.insertFourtech(contract);

        return result;
    }


    @Transactional
    public Integer updateFourtechTijiaoren(AudFourtech contract) {
        Integer result = 1;

        result = audFourtechMapper.updateFourtech(contract);

        return result;
    }


    @Transactional
    public Integer mergeFourtechDoc(Integer contractid, BasDoc doc) {

        Integer result = basDocMapper.insertBasDoc(doc);

        log.debug("mergeContractDoc docid is " + doc.getDocid().toString());

        return doc.getDocid();
    }

    public List<BasDoc> selectFourtechDocList(BasDoc sheet) {

        List<BasDoc> sheetList = basDocMapper.selectBasDocList(sheet);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }


    // 用于 审批流程。
    @Transactional
    public Integer updateFourtechStatus(AudFourtech contract, AudSheetauditrecord record ) {
        Integer result = 0;

        // 获取 短信服务器的密钥信息
        String url = "";
        String key = "";

        List<SysDictData> dictList =  dictDataMapper.selectDictDataByType("短信服务商");
        for (SysDictData dict : dictList) {
            if (dict.getDictLabel().equals("key")) {
                key = dict.getDictValue();
            }
            if (dict.getDictLabel().equals("url")) {
                url = dict.getDictValue();
            }
        }


        if (contract.getSheetstatus().equals(SheetStatus.ChuShen.getCode()) == false) {
            //消除经办人以前的待办事项“针对同一个类型的同一张单，因为要生成新的待办事项，所以讲旧的设为已读”
            // iamm.SetProcessed(lab_SheetUserID.Text, "合同", lab_ContractID.Text);
            AudMessage query = new AudMessage();
            query.setProcessedtime(DateUtils.dateTimeNow());
            query.setTouserid(contract.getSheetuserid());
            query.setRelatedsheettype("四技合同");
            query.setRelatedsheetid(contract.getFourtechid());
            messageMapper.updateIfProcessedAudMessageBySheetAndId(query);

            //消除本人的待办事项
            //  iamm.SetProcessed(Session["CurrentUserID"].ToString(), "合同", lab_ContractID.Text);
            query = new AudMessage();
            query.setProcessedtime(DateUtils.dateTimeNow());
            if (record != null) {
                query.setTouserid(record.getAudituserid());
            }
            query.setRelatedsheettype("四技合同");
            query.setRelatedsheetid(contract.getFourtechid());
            messageMapper.updateIfProcessedAudMessageBySheetAndId(query);
        }

        // 开始修改状态。
        if (contract.getSheetstatus() == SheetStatus.YiZuoFei.getCode()) {
//            audSheetMapper.updateAudSheetStatus(contract);

            return 1;
        }
        else if (contract.getSheetstatus() == SheetStatus.NoPass.getCode()) {
            result = audFourtechMapper.updateFourtechStatus(contract);

            //给相应人员发提醒消息
            String title = "新的四技合同审批不通过";
            String content = "您提交的项目：" + contract.getFourtechname() + " 的四技合同审批不通过。"
                    + "<a href=\"/FourTech/FourTechList_tijiaoren.aspx?f=todo&kid=" + contract.getFourtechid() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);

            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getSheetuserid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            //发送短信 发送给经办人
            String msg = "#type#=四技合同&#name#=" + contract.getFourtechname()  + "&#result#=不通过";
            String jinbanren = contract.getSheetuserid().toString();
            // 暂时 关闭，调试中。
            Juhe.sendSMS_Audited_ToSheetUser("13776614820", msg, url, key);

            //发送短信 发送给当前审核人
            String danwei =  contract.getCoperationunit();

            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=不通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Juhe.sendSMS_Audited("13776614820", msg, url, key);
        }
        else if (contract.getSheetstatus().equals(SheetStatus.ChuShen.getCode())) {
            result = audFourtechMapper.updateFourtechStatus(contract);

            //给相应人员发提醒消息
            //发送通知消息(发送给具有“产业处初审”功能的人员)
//            IPermissionManager iperm = PermissionManager.GetInstance();
//            DataSet ds = iperm.GetUserListToModule(ConstantParameter.MID_FourTech_ChanYeChuShen);

            //给相关处的负责人发信息
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(56L);

            for (SysUserRole u : userRoleList) {
                log.debug("userrole is " + u.toString());
                Integer userid = u.getUserId().intValue();
                String title =  "新的四技合同待您审批";
                String content = "项目：" + contract.getFourtechname() + "的四技合同待审批。"
                        + "<a href=\"/FourTech/FourTechList_ToAudit.aspx?t=2&f=todo&kid=" + contract.getFourtechid() + "\" target=\"_self\" >查看</a> ";   //t=2表示产业处初审 //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给分管处的负责人
                AudMessage message = new AudMessage();
                message.setTouserid(userid);
                message.setRelatedsheettype("四技合同");
                message.setRelatedsheetid(contract.getFourtechid());
                messageMapper.insertAudMessage(message);

                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
                for (AppClientinfo client : clientList) {
                    String clientId = client.getClientid();
                    String msgTitle = "新的四技合同待您审批";
                    String msgBody = "项目：" + contract.getFourtechname() + "的四技合同待审批。";
                    // 暂时 关闭，调试中。
                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
                }

                //发送短信
                String msg = "#type#=四技合同&#name#=" + contract.getFourtechname();
                // 暂时 关闭，调试中。
                // userid ..
                Integer currentAudituserid =  contract.getSheetuserid();

                String currentAudituseridTel = "13776614820";

                Juhe.sendSMS_Audited(currentAudituseridTel, msg, url, key);

            }
        }
        else  if (contract.getSheetstatus().equals(SheetStatus.XiangMuShenPi.getCode())) {

            result = audFourtechMapper.updateFourtechStatus(contract);

            //给相应人员发提醒消息
            //产业处初审通过，给经办人发信息
            String title = "新的四技合同审批中";
            String content ="您提交的项目：" + contract.getFourtechname() + "的四技合同由产业处初审通过。等待项目负责人审批。"
                    + "<a href=\"/FourTech/FourTechList_tijiaoren.aspx?f=todo&kid=" + contract.getFourtechid() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getSheetuserid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            //给项目负责人发信息
            title = "新的四技合同待您审批";
            content ="项目：" + contract.getFourtechname() + "的四技合同待您审批。"
                    + "<a href=\"/FourTech/FourTechList_ToAudit.aspx?t=3&f=todo&kid=" +  contract.getFourtechid() + "\" target=\"_self\" >查看</a> ";   //t=4表示项目负责人审批
            message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            message.setTouserid(contract.getManagerid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            // 发送推送

            Integer userid = message.getTouserid();
            List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
            for (AppClientinfo client : clientList) {
                String clientId = client.getClientid();
                String msgTitle = "新的四技合同待您审批";
                String msgBody = "项目：" + contract.getFourtechname() + " 的四技合同待您审批。";
                // 暂时 关闭，调试中。
                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
            }

            //发送短信 给项目负责人  。
            String msg = "#type#=四技合同&#name#=" + contract.getFourtechname();
            // 暂时 关闭，调试中。
            Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getCoperationunit();

            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Integer currentAudituserid =  record.getAudituserid();

            String currentAudituseridTel = "13776614820";

            Juhe.sendSMS_Audited(currentAudituseridTel, msg, url, key);

        }
        else  if (contract.getSheetstatus().equals(SheetStatus.BuMenShenPi.getCode())) {

            result = audFourtechMapper.updateFourtechStatus(contract);

            //给相应人员发提醒消息
            //项目负责人 审批通过，给经办人发信息
            String title = "新的四技合同审批中";
            String content = "您提交的项目：" + contract.getFourtechname() + "的四技合同由项目负责人审批通过。等待部门负责人审批。"
                    + "<a href=\"/FourTech/FourTechList_tijiaoren.aspx?f=todo&kid=" +  contract.getFourtechid()  + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getSheetuserid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            //给部门负责人发信息

            title = "新的四技合同待您审批";
            content ="项目：" + contract.getFourtechname() + "的四技合同待您审批。"
                    + "<a href=\"/FourTech/FourTechList_ToAudit.aspx?t=4&f=todo&kid=" + contract.getFourtechid()  + "\" target=\"_self\" >查看</a> ";   //t=4表示项目负责人审批
            message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给项目所属的部门的负责人
            Integer managerId = 0;
            SysDept dept = deptMapper.selectDeptById(contract.getOrganizationid().longValue());
            managerId = dept.getManagerId().intValue();
            message.setTouserid(managerId);
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            // 发送推送

            Integer userid = message.getTouserid();
            List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
            for (AppClientinfo client : clientList) {
                String clientId = client.getClientid();
                String msgTitle = "新的四技合同待您审批";
                String msgBody = "项目：" + contract.getFourtechname() + " 的四技合同待您审批。";
                // 暂时 关闭，调试中。
                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
            }

            //发送短信 给部门负责人  。
            String msg = "#type#=四技合同&#name#=" + contract.getFourtechname();
            // 暂时 关闭，调试中。
            Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getCoperationunit();

            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Integer currentAudituserid =  record.getAudituserid();

            String currentAudituseridTel = "13776614820";

            Juhe.sendSMS_Audited(currentAudituseridTel, msg, url, key);

        }
        else  if (contract.getSheetstatus().equals(SheetStatus.ChuShenPi.getCode())) {

            result = audFourtechMapper.updateFourtechStatus(contract);

            //给相应人员发提醒消息
            //部门负责人审批通过，给经办人发信息
            String title = "新的四技合同审批中";
            String content = "您提交的项目：" + contract.getFourtechname() + "的四技合同由部门负责人审批通过。等待分管处审批。"
                    + "<a href=\"/FourTech/FourTechList_tijiaoren.aspx?f=todo&kid=" + contract.getFourtechid()  + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getSheetuserid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            ////给产业处审核的相关人员发信息
            //DataSet roleds = irolem.GetUsersOfARole(ConstantParameter.Role_ChengGuoChuManager);
            //发送通知消息(发送给具有“产业处审核”功能的人员)

            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(59L);

            for (SysUserRole u : userRoleList) {
                log.debug("userrole is " + u.toString());
                Integer userid = u.getUserId().intValue();
                title =  "新的四技合同待您审批";
                content = "项目：" +  contract.getFourtechname()  + "的四技合同待您审批。"
                        + "<a href=\"/FourTech/FourTechList_ToAudit.aspx?t=5&f=todo&kid=" + contract.getFourtechid() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给分管处的负责人
                message.setTouserid(userid);
                message.setRelatedsheettype("四技合同");
                message.setRelatedsheetid(contract.getFourtechid());
                messageMapper.insertAudMessage(message);

                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
                for (AppClientinfo client : clientList) {
                    String clientId = client.getClientid();
                    String msgTitle = "新的四技合同待您审批";
                    String msgBody = "项目：" + contract.getFourtechname() + " 的四技合同待您审批。";
                    // 暂时 关闭，调试中。
                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
                }

                //发送短信
                String msg = "#type#=四技合同&#name#=" + contract.getFourtechname();
                // 暂时 关闭，调试中。
                // userid ..
                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

            }
             //发送 发送短信  给当前审核人

            String danwei = contract.getCoperationunit();
            String  msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();
            Integer currentAudituserid =  record.getAudituserid();
            String currentAudituseridTel = "13776614820";
            Juhe.sendSMS_Audited(currentAudituseridTel, msg, url, key);


        }
        else  if (contract.getSheetstatus().equals(SheetStatus.SuoZhangShenPi.getCode())) {

            result = audFourtechMapper.updateFourtechStatus(contract);

            //给相应人员发提醒消息
            //分管处审批通过，给经办人发信息
            String title = "新的四技合同审批中";
            String content = "您提交的项目：" + contract.getFourtechname()+ "的四技合同由分管处审批通过。等待所长审批。"
                    + "<a href=\"/FourTech/FourTechList_tijiaoren.aspx?f=todo&kid=" +  contract.getFourtechid()  + "\" target=\"_self\" >查看</a> ";

            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getSheetuserid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            //给所长发信息
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(12L);

            for (SysUserRole u : userRoleList) {
                log.debug("userrole is " + u.toString());
                Integer userid = u.getUserId().intValue();
                title =  "新的四技合同待您审批";
                content = "项目：" +  contract.getFourtechname()  + "的四技合同待您审批。"
                        + "<a href=\"/FourTech/FourTechList_ToAudit.aspx?t=7&f=todo&kid=" + contract.getFourtechid() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给分管处的负责人
                message.setTouserid(userid);
                message.setRelatedsheettype("四技合同");
                message.setRelatedsheetid(contract.getFourtechid());
                messageMapper.insertAudMessage(message);

                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
                for (AppClientinfo client : clientList) {
                    String clientId = client.getClientid();
                    String msgTitle = "新的四技合同待您审批";
                    String msgBody = "项目：" + contract.getFourtechname() + " 的四技合同待您审批。";
                    // 暂时 关闭，调试中。
                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
                }

                //发送短信
                String msg = "#type#=四技合同&#name#=" + contract.getFourtechname();
                // 暂时 关闭，调试中。
                // userid ..
                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
            }

            //发送 发送短信  给当前审核人

            String danwei = contract.getCoperationunit();
            String  msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();
            Integer currentAudituserid =  record.getAudituserid();
            String currentAudituseridTel = "13776614820";
            Juhe.sendSMS_Audited(currentAudituseridTel, msg, url, key);


        }
        else  if (contract.getSheetstatus().equals(SheetStatus.ShenPiWanCheng.getCode())) {
            //如果是审批完成，则还要加上“审核通过”时间
            contract.setPasstime(DateUtils.dateTimeNow());
           result = audFourtechMapper.updateFourtechStatus(contract);

            //合同审批通过后，要改写合同正文文档，加上签名，加上二维码
            //  ProcessDoc(); 待开发。

            //给相应人员发提醒消息
            //所长审批通过，给经办人发信息
            String title = "新的四技合同审批完成";
            String content = "您提交的项目：" + contract.getFourtechname()+ "的四技合同由由所长审批通过。"
                    + "<a href=\"/FourTech/FourTechList_tijiaoren.aspx?f=todo&kid=" +  contract.getFourtechid()  + "\" target=\"_self\" >查看</a> ";

            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getSheetuserid());
            message.setRelatedsheettype("四技合同");
            message.setRelatedsheetid(contract.getFourtechid());
            messageMapper.insertAudMessage(message);

            //发送短信 发送给当前审核人
            String danwei = contract.getCoperationunit();
            String  msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Juhe.sendSMS_Audited("13776614820", msg, url, key);

            //发送短信，给经办人
            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过";
            //
            // 经办人 contract.getSheetuserid().toString();

            Juhe.sendSMS_Audited_ToSheetUser("13776614820", msg, url, key);

        }


        //记录审核结论
        if (record != null ) {
            audSheetauditrecordMapper.insertAudSheetauditrecord(record);
        }

        return result;
    }

    public List<AudFourtech> selectFourtechChushen(AudFourtech sheet) {

        sheet.setSheetstatus(SheetStatus.ChuShen.getCode());

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechChushen(sheet);

        log.debug("request selectFourtechChushen list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudFourtech> selectFourtechXiangmu(AudFourtech sheet) {

        sheet.setSheetstatus(SheetStatus.XiangMuShenPi.getCode());

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechXiangmu(sheet);

        log.debug("request selectFourtechXiangmu list is " + sheetList.toString());

        return sheetList;
    }


    public List<AudFourtech> selectFourtechBumen(AudFourtech sheet) {

        sheet.setSheetstatus(SheetStatus.BuMenShenPi.getCode());

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechBumen(sheet);

        log.debug("request selectFourtechBumen list is " + sheetList.toString());

        return sheetList;
    }


    public List<AudFourtech> selectFourtechChu(AudFourtech sheet) {

        sheet.setSheetstatus(SheetStatus.ChuShenPi.getCode());

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechChu(sheet);

        log.debug("request selectFourtechChu list is " + sheetList.toString());

        return sheetList;
    }
    public List<AudFourtech> selectFourtechSuozhang(AudFourtech sheet) {

        sheet.setSheetstatus(SheetStatus.SuoZhangShenPi.getCode());

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechSuozhang(sheet);

        log.debug("request selectFourtechSuozhang list is " + sheetList.toString());

        return sheetList;
    }

//
//
//    public List<AudBudgetpay> selectFourtechBudgetPayById(Integer sheetid) {
//
//        List<AudBudgetpay> payList = audBudgetpayMapper.selectBudgetPayOfFourtechByFourtechid(sheetid);
//
//        log.debug("request payList list is " + payList.toString());
//
//        return payList;
//    }
//
//    public List<AudBudgetpay> selectBudgetPayRecord(AudBudgetpayRecordQuery query) {
//
//        log.debug("selectBudgetPayRecord  query is " + query.toString());
//
//        List<AudBudgetpay> payList = audBudgetpayMapper.selectBudgetPayRecord(query);
//
////        log.debug("request selectBudgetPayRecord  list is " + payList.toString());
//
//        return payList;
//    }
//
//    public List<AudFourtechauditrecord> selectFourtechauditRecord(AudFourtechauditrecord query) {
//
//        log.debug("selectBudgetPayRecord  query is " + query.toString());
//
//        List<AudFourtechauditrecord> payList = audFourtechauditrecordMapper.selectFourtechauditRecord(query);
//
//        log.debug("request selectBudgetPayRecord  list is " + payList.toString());
//
//        return payList;
//    }
//
//
//    @Transactional
//    public Integer addFourtechTijiaoren(AudFourtech sheet) {
//
//        // 获取 短信服务器的密钥信息
//        String url = "";
//        String key = "";
//
//        List<SysDictData> dictList =  dictDataMapper.selectDictDataByType("短信服务商");
//        for (SysDictData dict : dictList) {
//            if (dict.getDictLabel().equals("key")) {
//                key = dict.getDictValue();
//            }
//            if (dict.getDictLabel().equals("url")) {
//                url = dict.getDictValue();
//            }
//
//        }
//
//        if (sheet.getFourtechcode() == null || sheet.getFourtechcode().isEmpty()) {
////        if (ifCopy == false)   //说明是新单，单号要新生成。否则，就用旧单号
////        {
////            //在保存之前，确保号是最新的
////            //获取最新的流水号
////            ISerialNumberManager isnm = SerialNumberManager.GetInstance();
////            int newSerialNumber = isnm.GetNewSerialNumber("拨付单号");
////
////            //（单号规则：BFD+8位年月日+3位流水号）  待定
////            curCode = "BFD" + DateTime.Now.ToString("yyyyMMdd") + newSerialNumber.ToString("000");
////            newEntity.FourtechCode = curCode;
////        }
//
//            BasUsedmaxserialnumber query = new BasUsedmaxserialnumber();
//            query.setModulename("拨付单号");
//            query.setSomedate(DateUtils.getDate());
//            Integer serialnumber = 1;
//            BasUsedmaxserialnumber record = usedmaxserialnumberMapper.selectBasUsedmaxserialnumber(query);
//            if (record == null) {
//                query.setUsedmaxserialnumber(serialnumber);
//                usedmaxserialnumberMapper.insertBasUsedmaxserialnumber(query);
//            } else {
//                serialnumber = record.getUsedmaxserialnumber() + 1;
//                query.setUsedmaxserialnumber(serialnumber);
//                usedmaxserialnumberMapper.updateBasUsedmaxserialnumber(query);
//            }
//
//            String sheetCode = "BFD" + DateUtils.dateTime() + String.format("%03d", serialnumber);
//
//            log.debug("sheetCode is " + sheetCode);
//
//            sheet.setFourtechcode(sheetCode);
//        }
//
//        BigDecimal  hejiZong = new BigDecimal(0);
//        BigDecimal hejiXiaoji  = new BigDecimal(0);
//        BigDecimal  hejiYiqian = new BigDecimal(0);
//        BigDecimal  hejiBennian = new BigDecimal(0);
//        BigDecimal  hejiBenci = new BigDecimal(0);
//
//        for(AudBudgetpay s : sheet.getBudgetpayList()) {
//            hejiZong = hejiZong.add(s.getZong());
//            hejiXiaoji = hejiXiaoji.add(s.getXiaoji());
//            hejiYiqian = hejiYiqian.add(s.getYiqian());
//            hejiBennian = hejiBennian.add(s.getBennian());
//            hejiBenci = hejiBenci.add(s.getBenci());
//        }
//        sheet.setHejiZong(hejiZong);
//        sheet.setHejiXiaoji(hejiXiaoji);
//        sheet.setHejiYiqian(hejiYiqian);
//        sheet.setHejiBennian(hejiBennian);
//        sheet.setHejiBenci(hejiBenci);
//        sheet.setDaxie(ConvertUpMoney.toChinese(hejiBenci.toString()));
//        sheet.setReferenceid("");
//
//        Integer result = audFourtechMapper.insertAudFourtech(sheet);
//
//        log.debug("sheet id is " + sheet.getFourtechid());
//
//        for(AudBudgetpay s : sheet.getBudgetpayList()) {
//            log.debug("AudBudgetpay is " + s.toString());
//            s.setFourtechid(sheet.getFourtechid());
//            s.setProjectid(sheet.getProjectid());
//            result = audBudgetpayMapper.insertAudBudgetpay(s);
//        }
//
//
//        //给相应人员发提醒消息
//        String title = "新的拨付单待审批";
//        String content = "拨付单：" + sheet.getFourtechcode() + "待审批。"
//                + "<a href=\"/Audit/FourtechList_ToAudit.aspx?t=3&f=todo&type=bf&kid=" + sheet.getFourtechid() + "\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
//        //发送给项目的负责人
//        AudMessage message = new AudMessage();
//        message.setMessagetime(DateUtils.dateTimeNow());
//        message.setMessagetitle(title);
//        message.setMessagecontent(content);
//        message.setTouserid(sheet.getProjectmanagerid());
//        message.setRelatedsheettype("拨付单");
//        message.setRelatedsheetid(sheet.getFourtechid());
//        messageMapper.insertAudMessage(message);
//
////        api.MsgManager.SendToAUser(am.ToUserID.ToString(), am.MessageTitle,
////                "新的拨付单：" + code + "待审批。");
////
////        //发送短信
////        CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
////                "#type#=拨付单&#name#=" + code);
//
//        // 发送推送
//        Integer userid = sheet.getProjectmanagerid();
//        List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//        for (AppClientinfo client : clientList) {
//            String clientId = client.getClientid();
//            String msgTitle = "新的拨付单待审批";
//            String msgBody = "新的拨付单：" + sheet.getFourtechcode() + "待审批。";
//            // 暂时 关闭，调试中。
//            PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//        }
//
//        //发送短信
//        String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode();
//        // 暂时 关闭，调试中。
//        Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
//
//        return result;
//    }
//
//
//    public List<AudFourtech> selectFourtechXiangmuByUserid(Integer uid) {
//
//        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechXiangmuByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudFourtech> selectFourtechBuMenByUserid(Integer uid) {
//
//        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechBuMenByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudFourtech> selectFourtechChuByUserid(Integer uid) {
//
//
//        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechChuByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudFourtech> selectFourtechFenguansuoByUserid(Integer uid) {
//
//
//        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechFenguansuoByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudFourtech> selectFourtechSuozhangByUserid(Integer uid) {
//
//
//        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechSuozhangByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    @Transactional
//    public Integer updateFourtechAuditStatus(AudFourtech sheet, AudFourtechauditrecord record ) {
//        Integer result = 0;
//
//        // 获取 短信服务器的密钥信息
//        String url = "";
//        String key = "";
//
//        List<SysDictData> dictList =  dictDataMapper.selectDictDataByType("短信服务商");
//        for (SysDictData dict : dictList) {
//            if (dict.getDictLabel().equals("key")) {
//                key = dict.getDictValue();
//            }
//            if (dict.getDictLabel().equals("url")) {
//                url = dict.getDictValue();
//            }
//
//        }
//
//
//
//        //消除经办人以前的待办事项“针对同一个类型的同一张单，因为要生成新的待办事项，所以讲旧的设为已读”
//        // iamm.SetProcessed(lab_FourtechUserID.Text, "拨付单", lab_FourtechID.Text);
//        AudMessage query = new AudMessage();
//        query.setProcessedtime(DateUtils.dateTimeNow());
//        query.setTouserid(sheet.getFourtechuserid());
//        query.setRelatedsheettype("拨付单");
//        query.setRelatedsheetid(sheet.getFourtechid());
//        messageMapper.updateIfProcessedAudMessageByFourtechAndId(query);
//
//        //消除本人的待办事项
//        // iamm.SetProcessed(Session["CurrentUserID"].ToString(), "拨付单", lab_FourtechID.Text);
//        query = new AudMessage();
//        query.setProcessedtime(DateUtils.dateTimeNow());
//        if (record != null) {
//            query.setTouserid(record.getAudituserid());
//        }
//        query.setRelatedsheettype("拨付单");
//        query.setRelatedsheetid(sheet.getFourtechid());
//        messageMapper.updateIfProcessedAudMessageByFourtechAndId(query);
//
//        if (sheet.getFourtechstatus() == FourtechStatus.YiZuoFei.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//            return 1;
//        }
//        else if (sheet.getFourtechstatus() == FourtechStatus.NoPass.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//            //给相应人员发提醒消息
//            String title = "拨付单审批不通过";
//            String content =  "您提交的拨付单："
//                    + sheet.getFourtechcode() + "审批不通过。"
//                    + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getFourtechuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//            // 这个逻辑放到 controller 里实现，这是异步操作。
////            //发送短信
////            CommonFunc.SendSMS_Audited_ToFourtechUser(am.ToUserID.ToString(),        //发送给经办人
////                    "#type#=拨付单&#name#=" + lab_FourtechCode.Text + "&#result#=不通过");
////            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(),        //发送给当前审核人
////                    "#type#=拨付单&#name#=" + lab_FourtechCode.Text + "&#result#=不通过"
////                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);
//
//            //发送短信
//            String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=不通过";
//            // 暂时 关闭，调试中。
//            Juhe.sendSMS_Audited_ToFourtechUser("13776614820", msg, url, key);
//
//            String danwei = "";
//            if (sheet.getBudgetpayList().size() > 1) {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
//            }
//            else {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
//            }
//            msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=不通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//        }
//        else if (sheet.getFourtechstatus() == FourtechStatus.BuMenShenPi.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//
//            //给相应人员发提醒消息
//            //项目负责人审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content =  "您提交的拨付单："
//                    + sheet.getFourtechcode() + "由项目负责人审批通过。等待部门负责人审批。"
//                    + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" +  sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getFourtechuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//
//            //给部门负责人发信息
//            title =  "拨付单待您审批";
//            content =  "拨付单："
//                    +  sheet.getFourtechcode() + "待您审批。"
//                    + "<a href=\"/Audit/FourtechList_ToAudit.aspx?t=4&f=todo&type=bf&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";   //t=4表示部门负责人审批
//            //查询消息要发送给哪个人员
//            //发送给项目所属的部门的负责人
//            Integer managerId = 0;
//            SysDept dept = deptMapper.selectDeptById(sheet.getOrganizationid().longValue());
//            managerId = dept.getManagerId().intValue();
//            message.setTouserid(managerId);
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//
//            // 这个逻辑放到 controller 里实现，这是异步操作。
////            SendAppMsg(am.ToUserID.ToString());  api.MsgManager.SendToAUser(userID, "拨付单待您审批", "拨付单：" + lab_FourtechCode.Text + "待您审批。");
////
////            //发送短信
////            CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
////                    "#type#=拨付单&#name#=" + lab_FourtechCode.Text);
////
////            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(),        //发送给当前审核人
////                    "#type#=拨付单&#name#=" + lab_FourtechCode.Text + "&#result#=通过"
////                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);
//
//            // 发送推送
//
//            Integer userid = message.getTouserid();
//            List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//            for (AppClientinfo client : clientList) {
//                String clientId = client.getClientid();
//                String msgTitle = "拨付单待您审批";
//                String msgBody = "拨付单：" + sheet.getFourtechcode() + "待您审批。";
//                // 暂时 关闭，调试中。
//                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//            }
//
//            //发送短信
//            String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode();
//            // 暂时 关闭，调试中。
//            Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
//
//            //发送给当前审核人
//            String danwei = "";
//            if (sheet.getBudgetpayList().size() > 1) {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
//            }
//            else  if (sheet.getBudgetpayList().size() == 1) {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
//            }
//            msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//
//        }
//        else if (sheet.getFourtechstatus() == FourtechStatus.ChuShenPi.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//
//            //给相应人员发提醒消息
//            //部门负责人审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content = "您提交的拨付单："
//                    +  sheet.getFourtechcode()  + "由部门负责人审批通过。等待分管处审批。"
//                    + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getFourtechuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//
//            //给相关处的负责人发信息
//            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(10L);
//
//            for (SysUserRole u : userRoleList) {
//                log.debug("userrole is " + u.toString());
//                Integer userid = u.getUserId().intValue();
//                title =  "拨付单待您审批";
//                content = "拨付单："
//                        + sheet.getFourtechcode() + "待您审批。"
//                        + "<a href=\"/Audit/FourtechList_ToAudit.aspx?t=5&f=todo&type=bf&kid=" +  sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
//                //查询消息要发送给哪个人员
//                //发送给分管处的负责人
//                message.setTouserid(userid);
//                message.setRelatedsheettype("拨付单");
//                message.setRelatedsheetid(sheet.getFourtechid());
//                messageMapper.insertAudMessage(message);
//
//                // 这个逻辑放到 controller 里实现，这是异步操作。
//                //                SendAppMsg(am.ToUserID.ToString());
//                //
//                //                //发送短信
//                //                CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                //                        "#type#=拨付单&#name#=" + lab_FourtechCode.Text);
//
//                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//                for (AppClientinfo client : clientList) {
//                    String clientId = client.getClientid();
//                    String msgTitle = "拨付单待您审批";
//                    String msgBody = "拨付单：" + sheet.getFourtechcode() + "待您审批。";
//                    // 暂时 关闭，调试中。
//                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//                }
//                //发送短信
//                String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode();
//                // 暂时 关闭，调试中。
//                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
//
//            }
//
//            //发送给当前审核人
//            String danwei = "";
//            if (sheet.getBudgetpayList().size() > 1) {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
//            }
//            else {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
//            }
//            String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//        }
//        else if (sheet.getFourtechstatus() == FourtechStatus.FenGuanSuoShenPi.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//
//            //给相应人员发提醒消息
//            //相关处的负责人审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content = "您提交的拨付单："
//                    +  sheet.getFourtechcode()  + "由分管处审批通过。等待分管所长审批。"
//                    + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getFourtechuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//
//            //给分管所长发信息
//            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(11L);
//            for (SysUserRole u : userRoleList) {
//                Integer userid = u.getUserId().intValue();
//                title =  "拨付单待您审批";
//                content = "拨付单："
//                        + sheet.getFourtechcode() + "待您审批。"
//                        + "<a href=\"/Audit/FourtechList_ToAudit.aspx?t=6&f=todo&type=bf&kid=" +  sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
//                //查询消息要发送给哪个人员
//                //发送给分管所长
//                message.setTouserid(userid);
//                message.setRelatedsheettype("拨付单");
//                message.setRelatedsheetid(sheet.getFourtechid());
//                messageMapper.insertAudMessage(message);
//
//                //发送app推送
//                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//                for (AppClientinfo client : clientList) {
//                    String clientId = client.getClientid();
//                    String msgTitle = "拨付单待您审批";
//                    String msgBody = "拨付单：" + sheet.getFourtechcode() + "待您审批。";
//                    // 暂时 关闭，调试中。
//                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//                }
//                //发送短信
//                String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode();
//                // 暂时 关闭，调试中。
//                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
//
//            }
//
//            //发送给当前审核人，分管所长 审批之后 C#中 不发短信通知了。
////            String danwei = "";
////            if (sheet.getBudgetpayList().size() > 1) {
////                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
////            }
////            else {
////                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
////            }
////            String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
////            Juhe.sendSMS_Audited("13776614820", msg);
//
//        }
//        else if (sheet.getFourtechstatus() == FourtechStatus.SuoZhangShenPi.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//            //给相应人员发提醒消息
//            //分管所长审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content = "您提交的拨付单："
//                    +  sheet.getFourtechcode()  + "由分管所长审批通过。等待所长审批。"
//                    + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getFourtechuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//
//            //给所长发信息
//            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(12L);
//            for (SysUserRole u : userRoleList) {
//                Integer userid = u.getUserId().intValue();
//                title =  "拨付单待您审批";
//                content = "拨付单："
//                        + sheet.getFourtechcode() + "待您审批。"
//                        + "<a href=\"/Audit/FourtechList_ToAudit.aspx?t=7&f=todo&type=bf&kid=" +  sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
//                //查询消息要发送给哪个人员
//                //发送给 所长
//                message.setTouserid(userid);
//                message.setRelatedsheettype("拨付单");
//                message.setRelatedsheetid(sheet.getFourtechid());
//                messageMapper.insertAudMessage(message);
//
//                //发送app推送
//                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//                for (AppClientinfo client : clientList) {
//                    String clientId = client.getClientid();
//                    String msgTitle = "拨付单待您审批";
//                    String msgBody = "拨付单：" + sheet.getFourtechcode() + "待您审批。";
//                    // 暂时 关闭，调试中。
//                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//                }
//                //发送短信
//                String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode();
//                // 暂时 关闭，调试中。
//                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
//
//            }
//
//            //发送给当前审核人，分管所长 审批之后 C#中 不发短信通知了。
////            String danwei = "";
////            if (sheet.getBudgetpayList().size() > 1) {
////                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
////            }
////            else {
////                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
////            }
////            String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
////            Juhe.sendSMS_Audited("13776614820", msg);
//
//        }
//        else if (sheet.getFourtechstatus() == FourtechStatus.ShenPiWanCheng.getCode()) {
//            audFourtechMapper.updateAudFourtechStatus(sheet);
//
//            //如果是完成状态，还要在明细记录里写入审核时间。表示这个付款明细已经执行了
//            AudBudgetpay pay = new AudBudgetpay();
//            pay.setFourtechid(sheet.getFourtechid());
//            pay.setAudittime(DateUtils.dateTimeNow());
//            audBudgetpayMapper.updateAudBudgetpayAduitTime(pay);
//
//            //给相应人员发提醒消息
//            //分管所长审批通过，给经办人发信息
//            String title = "拨付单审批完成";
//            String content = "";
//            log.debug("sheet audit is " + sheet.toString());
//            if (record.getAudittype().equals("6")) {
//                content = "您提交的拨付单："
//                        +  sheet.getFourtechcode()  + "由分管所长审批通过。"
//                        + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//
//            }
//            else if (record.getAudittype().equals("7")) {
//                content = "您提交的拨付单："
//                        +  sheet.getFourtechcode()  + "由所长审批通过。"
//                        + "<a href=\"/Audit/FourtechList_tijiaoren.aspx?f=todo&kid=" + sheet.getFourtechid().toString() + "\" target=\"_self\" >查看</a> ";
//
//            }
//
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getFourtechuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getFourtechid());
//            messageMapper.insertAudMessage(message);
//
//            //发送短信，给当前审核人
//            String danwei = "";
//            if (sheet.getBudgetpayList().size() > 1) {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
//            }
//            else {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
//            }
//            String msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//            //发送短信，给经办人
//            msg = "#type#=拨付单&#name#=" + sheet.getFourtechcode() + "&#result#=通过";
//            // 暂时 关闭，调试中。
//            Juhe.sendSMS_Audited_ToFourtechUser("13776614820", msg, url, key);
//
////            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(), 、、发送短信，给当前审核人
////                    "#type#=拨付单&#name#=" + lab_FourtechCode.Text + "&#result#=通过"
////                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);
////
////            CommonFunc.SendSMS_Audited_ToFourtechUser(am.ToUserID.ToString(),        //发送给经办人
////                    "#type#=拨付单&#name#=" + lab_FourtechCode.Text + "&#result#=通过");
//
//
//            //对应的协作单位审批通过 ，在协作单位开发完成后补充。
////            for (int s = 0; s < dpl_SupplierIDList.Items.Count; s++)
////            {
////                isupm.AuditPass(dpl_SupplierIDList.Items[s].Text);
////            }
//
//        }
//
//        //记录审核结论
//        audFourtechauditrecordMapper.insertAudFourtechauditrecord(record);
//
//        return result;
//    }
//
//
//    @Transactional
//    public Integer addSupplierinfo(SrmSupplierinfo supplier) {
//
//        Integer result = supplierinfoMapper.insertSupplierInfo(supplier);
//
//        BasDoc doc = new BasDoc();
//        doc.setDocid(supplier.getOrgImgId());
//        doc.setRelatedid(supplier.getSupplierid());
//        doc.setDoctype("营业执照");
//        doc.setAttachtotype("协作单位");
//        basDocMapper.updateBasDocAttachTo(doc);
//        log.debug("supplier is " + supplier.toString());
//
//        if (supplier.getDocList() != null) {
//            for (DocFile map : supplier.getDocList()) {
//                log.debug("getDocList map is " + map.toString());
//                Integer docid = map.getUrl();
//                doc = new BasDoc();
//                doc.setDocid(docid);
//                doc.setRelatedid(supplier.getSupplierid());
//                doc.setDoctype("其它附件");
//                doc.setAttachtotype("协作单位");
//                basDocMapper.updateBasDocAttachTo(doc);
//            }
//        }
//
//        return result;
//    }
//
//    @Transactional
//    public Integer updateSupplierinfo(SrmSupplierinfo supplier) {
//        log.debug("supplier is " + supplier.toString());
//
//        Integer result = supplierinfoMapper.updateSupplierInfo(supplier);
//
//        BasDoc doc = new BasDoc();
//        doc.setRelatedid(supplier.getSupplierid());
//        doc.setAttachtotype("协作单位");
//
//        Set<Integer> sets = new HashSet<>();
//        for( DocFile file : supplier.getDocList()){
//            sets.add(file.getUrl());
//        }
//
//        sets.add(supplier.getOrgImgId());
//
//
//        Set<Integer> sets2 = new HashSet<>();
//        for (BasDoc record : basDocMapper.selectBasDocList(doc))
//        {
//            if (sets.contains(record.getDocid()) == false)
//            {
//                sets2.add(record.getDocid());
//            }
//        }
//
//        List<Integer> ids = new ArrayList<Integer>();
//        for(Integer id : sets2) {
//            ids.add(id);
//        }
//
//        log.debug("deleteBasDocByIds is " + ids.toString());
//        if (ids.size() > 0) {
//            result =  basDocMapper.deleteBasDocByIds(ids);
//        }
//
//        doc.setDoctype("营业执照");
//        doc.setDocid(supplier.getOrgImgId());
//
//        result =  basDocMapper.updateBasDocAttachTo(doc);
//
//        if (supplier.getDocList() != null && supplier.getDocList().size() > 0) {
//            for (DocFile map : supplier.getDocList()) {
//                Integer docid = (Integer) map.getUrl();
//                doc = new BasDoc();
//                doc.setDocid(docid);
//                doc.setRelatedid(supplier.getSupplierid());
//                doc.setDoctype("其它附件");
//                doc.setAttachtotype("协作单位");
//                result = basDocMapper.updateBasDocAttachTo(doc);
//            }
//        }
//
//        return result;
//    }
//
//    @Transactional
//    public Integer deleteSupplierinfo(List<Integer> ids) {
//        log.debug("supplier ids is " + ids.toString());
//
//        Integer result = supplierinfoMapper.deleteSupplierInfoByIds(ids);
//
//        return result;
//    }
//
//    public List<SrmSupplierinfo> selectSupplierInfoList(SrmSupplierinfo query) {
//
//        log.debug("selectSupplierInfoList  query is " + query.toString());
//
//        List<SrmSupplierinfo> list = supplierinfoMapper.selectSupplierInfoList(query);
//
//        //   log.debug("request selectSupplierInfoList  list is " + list.toString());
//
//        return list;
//    }
//
//    public SrmSupplierinfo selectSupplierInfoById(Integer supplierid) {
//
//        SrmSupplierinfo s = supplierinfoMapper.selectSupplierInfoById(supplierid);
//
//        return s;
//    }
//
//    public SrmSupplierinfo selectSupplierInfoByOrganizationcode(String organizationcode) {
//
//        SrmSupplierinfo s = supplierinfoMapper.selectSupplierInfoByOrganizationcode(organizationcode);
//
//        return s;
//    }


}

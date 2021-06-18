package com.ruoyi.fourtech.service;


import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.domain.AudSheetauditrecord;
import com.ruoyi.audit.mapper.*;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SMSSender.Juhe;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.contract.mapper.AudContractdocMapper;
import com.ruoyi.fourtech.domain.AudFourtech;
import com.ruoyi.fourtech.mapper.AudFourtechMapper;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    SrmSupplierMapper supplierinfoMapper;

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

    @Resource
    private SysUserMapper userMapper;

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
            SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getSheetuserid()));

            Juhe.sendSMS_Audited_ToSheetUser(touser.getPhonenumber(), msg, url, key);

            //发送短信 发送给当前审核人
            String danwei =  contract.getCoperationunit();

            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=不通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;

            touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);
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

                SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getSheetuserid()));

                Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

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

            SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getManagerid()));

            Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getCoperationunit();

            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;

            touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

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

            SysUser touser = userMapper.selectUserById(Long.valueOf(managerId));

            Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getCoperationunit();

            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;
            touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

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

                SysUser touser = userMapper.selectUserById(Long.valueOf(userid));

                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            }
             //发送 发送短信  给当前审核人

            String danwei = contract.getCoperationunit();
            String  msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;

            SysUser touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);


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
                SysUser touser = userMapper.selectUserById(Long.valueOf(userid));
                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);
            }

            //发送 发送短信  给当前审核人

            String danwei = contract.getCoperationunit();
            String  msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过" + "&#money#=" + contract.getFourtechmoney().toString() + "&#danwei#=" + danwei;

            SysUser touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));
            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);


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
            SysUser touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));
            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

            //发送短信，给经办人
            msg = "#type#=四技合同&#name#=" + contract.getFourtechname() + "&#result#=通过";
            //
            // 经办人 contract.getSheetuserid().toString();
            touser = userMapper.selectUserById(Long.valueOf(contract.getSheetuserid()));
            Juhe.sendSMS_Audited_ToSheetUser(touser.getPhonenumber(), msg, url, key);

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




    public List<AudFourtech> selectFourtechAuditedByUserid(Integer uid) {

        List<AudFourtech> list = audFourtechMapper.selectFourtechAuditedByUserid(uid);

        log.debug("request  sheetList list audited by is " + list.toString());

        return list;
    }

    public List<AudFourtech> selectFourtechShenPiWanCheng(AudFourtech sheet) {

        sheet.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());

        List<AudFourtech> sheetList = audFourtechMapper.selectFourtechShenPiWanCheng(sheet);

        log.debug("request selectFourtechShenPiWanCheng list is " + sheetList.toString());

        return sheetList;
    }

    // 用于 审批流程。
    @Transactional
    public Integer updateFourtechCode(AudFourtech contract) {
        Integer result = 0;

        result = audFourtechMapper.updateFourtechCode(contract);


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


        //发送通知短信给经办人

        String msg = "#name#=" + contract.getFourtechname() + "&#num#=" + contract.getFourtechcode().toString() ;

        SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getSheetuserid()));

        Juhe.sendSMS_CodeFourTech(touser.getPhonenumber(), msg, url, key);

        return result;
    }


    public List<AudFourtech> queryFourtech(AudFourtech fourtech) {

        List<AudFourtech> sheetList = audFourtechMapper.queryFourtech(fourtech);

        log.debug("queryFourtech sheetList is " + sheetList.toString());

        return sheetList;
    }

}

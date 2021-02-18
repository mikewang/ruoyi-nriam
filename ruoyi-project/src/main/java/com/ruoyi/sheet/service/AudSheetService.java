package com.ruoyi.sheet.service;


import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.ConvertUpMoney;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SMSSender.Juhe;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.sheet.domain.*;
import com.ruoyi.sheet.mapper.*;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import org.apache.poi.hpsf.Decimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AudSheetService {

    private static final Logger log = LoggerFactory.getLogger(AudApplyService.class);

    @Resource
    AudSheetMapper audSheetMapper;

    @Resource
    AudBudgetpayMapper audBudgetpayMapper;

    @Resource
    AudSheetauditrecordMapper audSheetauditrecordMapper;


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



    public List<AudSheet> selectSheetTijiaorenByUserid(Long userId) {
        Integer uid = userId.intValue();
        uid = 175;
        List<AudSheet> sheetList = audSheetMapper.selectSheetTijiaorenByUserid(uid);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }

    public AudSheet selectSheetTijiaorenById(Integer sheetid) {

        AudSheet sheet = audSheetMapper.selectSheetById(sheetid);

        return sheet;
    }


    public List<AudBudgetpay> selectSheetBudgetPayById(Integer sheetid) {

        List<AudBudgetpay> payList = audBudgetpayMapper.selectBudgetPayOfSheetBySheetid(sheetid);

        log.debug("request payList list is " + payList.toString());

        return payList;
    }

    public List<AudBudgetpay> selectBudgetPayRecord(AudBudgetpayRecordQuery query) {

        log.debug("selectBudgetPayRecord  query is " + query.toString());

        List<AudBudgetpay> payList = audBudgetpayMapper.selectBudgetPayRecord(query);

//        log.debug("request selectBudgetPayRecord  list is " + payList.toString());

        return payList;
    }

    public List<AudSheetauditrecord> selectSheetauditRecord(AudSheetauditrecord query) {

        log.debug("selectBudgetPayRecord  query is " + query.toString());

        List<AudSheetauditrecord> payList = audSheetauditrecordMapper.selectSheetauditRecord(query);

        log.debug("request selectBudgetPayRecord  list is " + payList.toString());

        return payList;
    }

    public List<SrmSupplierinfo> selectSupplierInfoList(SrmSupplierinfo query) {

        log.debug("selectSupplierInfoList  query is " + query.toString());

        List<SrmSupplierinfo> list = supplierinfoMapper.selectSupplierInfoList(query);

        //   log.debug("request selectSupplierInfoList  list is " + list.toString());

        return list;
    }


    @Transactional
    public Integer addSheetTijiaoren(AudSheet sheet) {

        if (sheet.getSheetcode() == null || sheet.getSheetcode().isEmpty()) {
//        if (ifCopy == false)   //说明是新单，单号要新生成。否则，就用旧单号
//        {
//            //在保存之前，确保号是最新的
//            //获取最新的流水号
//            ISerialNumberManager isnm = SerialNumberManager.GetInstance();
//            int newSerialNumber = isnm.GetNewSerialNumber("拨付单号");
//
//            //（单号规则：BFD+8位年月日+3位流水号）  待定
//            curCode = "BFD" + DateTime.Now.ToString("yyyyMMdd") + newSerialNumber.ToString("000");
//            newEntity.SheetCode = curCode;
//        }

            BasUsedmaxserialnumber query = new BasUsedmaxserialnumber();
            query.setModulename("拨付单号");
            query.setSomedate(DateUtils.getDate());
            Integer serialnumber = 1;
            BasUsedmaxserialnumber record = usedmaxserialnumberMapper.selectBasUsedmaxserialnumber(query);
            if (record == null) {
                query.setUsedmaxserialnumber(serialnumber);
                usedmaxserialnumberMapper.insertBasUsedmaxserialnumber(query);
            } else {
                serialnumber = record.getUsedmaxserialnumber() + 1;
                query.setUsedmaxserialnumber(serialnumber);
                usedmaxserialnumberMapper.updateBasUsedmaxserialnumber(query);
            }

            String sheetCode = "BFD" + DateUtils.dateTime() + String.format("%03d", serialnumber);

            log.debug("sheetCode is " + sheetCode);

            sheet.setSheetcode(sheetCode);
        }

        BigDecimal  hejiZong = new BigDecimal(0);
        BigDecimal hejiXiaoji  = new BigDecimal(0);
        BigDecimal  hejiYiqian = new BigDecimal(0);
        BigDecimal  hejiBennian = new BigDecimal(0);
        BigDecimal  hejiBenci = new BigDecimal(0);

        for(AudBudgetpay s : sheet.getBudgetpayList()) {
            hejiZong = hejiZong.add(s.getZong());
            hejiXiaoji = hejiXiaoji.add(s.getXiaoji());
            hejiYiqian = hejiYiqian.add(s.getYiqian());
            hejiBennian = hejiBennian.add(s.getBennian());
            hejiBenci = hejiBenci.add(s.getBenci());
        }
        sheet.setHejiZong(hejiZong);
        sheet.setHejiXiaoji(hejiXiaoji);
        sheet.setHejiYiqian(hejiYiqian);
        sheet.setHejiBennian(hejiBennian);
        sheet.setHejiBenci(hejiBenci);
        sheet.setDaxie(ConvertUpMoney.toChinese(hejiBenci.toString()));
        sheet.setReferenceid("");

        Integer result = audSheetMapper.insertAudSheet(sheet);

        log.debug("sheet id is " + sheet.getSheetid());

        for(AudBudgetpay s : sheet.getBudgetpayList()) {
            log.debug("AudBudgetpay is " + s.toString());
            s.setSheetid(sheet.getSheetid());
            s.setProjectid(sheet.getProjectid());
            result = audBudgetpayMapper.insertAudBudgetpay(s);
        }


        //给相应人员发提醒消息
        String title = "新的拨付单待审批";
        String content = "拨付单：" + sheet.getSheetcode() + "待审批。"
                + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=3&f=todo&type=bf&kid=" + sheet.getSheetid() + "\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
        //发送给项目的负责人
        AudMessage message = new AudMessage();
        message.setMessagetime(DateUtils.dateTimeNow());
        message.setMessagetitle(title);
        message.setMessagecontent(content);
        message.setTouserid(sheet.getProjectmanagerid());
        message.setRelatedsheettype("拨付单");
        message.setRelatedsheetid(sheet.getSheetid());
        messageMapper.insertAudMessage(message);

//        api.MsgManager.SendToAUser(am.ToUserID.ToString(), am.MessageTitle,
//                "新的拨付单：" + code + "待审批。");
//
//        //发送短信
//        CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                "#type#=拨付单&#name#=" + code);

        // 发送推送
        Integer userid = sheet.getProjectmanagerid();
        List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
        for (AppClientinfo client : clientList) {
            String clientId = client.getClientid();
            String msgTitle = "新的拨付单待审批";
            String msgBody = "新的拨付单：" + sheet.getSheetcode() + "待审批。";
            // 暂时 关闭，调试中。
            PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
        }

        //发送短信
        String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode();
        // 暂时 关闭，调试中。
        Juhe.mobileQuery("13776614820", msg);

        return result;
    }


    public List<AudSheet> selectSheetXiangmuByUserid(Long userId) {
        Integer uid = userId.intValue();

        List<AudSheet> sheetList = audSheetMapper.selectSheetXiangmuByUserid(uid);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudSheet> selectSheetBuMenByUserid(Long userId) {
        Integer uid = userId.intValue();

        List<AudSheet> sheetList = audSheetMapper.selectSheetBuMenByUserid(uid);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }

    @Transactional
    public Integer updateSheetAuditStatus(AudSheet sheet, AudSheetauditrecord record ) {
        Integer result = 0;

        //消除经办人以前的待办事项“针对同一个类型的同一张单，因为要生成新的待办事项，所以讲旧的设为已读”
        // iamm.SetProcessed(lab_SheetUserID.Text, "拨付单", lab_SheetID.Text);
        AudMessage query = new AudMessage();
        query.setTouserid(sheet.getSheetuserid());
        query.setRelatedsheettype("拨付单");
        query.setRelatedsheetid(sheet.getSheetid());
        messageMapper.updateIfProcessedAudMessageBySheetAndId(query);

        //消除本人的待办事项
        // iamm.SetProcessed(Session["CurrentUserID"].ToString(), "拨付单", lab_SheetID.Text);
        query = new AudMessage();
        query.setTouserid(record.getAudituserid());
        query.setRelatedsheettype("拨付单");
        query.setRelatedsheetid(sheet.getSheetid());
        messageMapper.updateIfProcessedAudMessageBySheetAndId(query);

        if (sheet.getSheetstatus() == SheetStatus.NoPass.getCode()) {
            audSheetMapper.updateAudSheetStatus(sheet);
            //给相应人员发提醒消息
            String title = "拨付单审批不通过";
            String content =  "您提交的拨付单："
                    + sheet.getSheetcode() + "审批不通过。"
                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);

            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(sheet.getSheetuserid());
            message.setRelatedsheettype("拨付单");
            message.setRelatedsheetid(sheet.getSheetid());
            messageMapper.insertAudMessage(message);
            // 这个逻辑放到 controller 里实现，这是异步操作。
//            //发送短信
//            CommonFunc.SendSMS_Audited_ToSheetUser(am.ToUserID.ToString(),        //发送给经办人
//                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=不通过");
//            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(),        //发送给当前审核人
//                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=不通过"
//                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);

            //发送短信
            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=不通过";
            // 暂时 关闭，调试中。
            Juhe.mobileQuery("13776614820", msg);

            String danwei = "";
            if (sheet.getBudgetpayList().size() > 1) {
                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
            }
            else {
                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
            }
            msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=不通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
            Juhe.mobileQuery("13776614820", msg);

        }
        else if (sheet.getSheetstatus() == SheetStatus.BuMenShenPi.getCode()) {
            audSheetMapper.updateAudSheetStatus(sheet);

            //给相应人员发提醒消息
            //项目负责人审批通过，给经办人发信息
            String title = "拨付单审批中";
            String content =  "您提交的拨付单："
                    + sheet.getSheetcode() + "由项目负责人审批通过。等待部门负责人审批。"
                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" +  sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(sheet.getSheetuserid());
            message.setRelatedsheettype("拨付单");
            message.setRelatedsheetid(sheet.getSheetid());
            messageMapper.insertAudMessage(message);

            //给部门负责人发信息
            title =  "拨付单待您审批";
            content =  "拨付单："
                    +  sheet.getSheetcode() + "待您审批。"
                    + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=4&f=todo&type=bf&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";   //t=4表示部门负责人审批
            //查询消息要发送给哪个人员
            //发送给项目所属的部门的负责人
            Integer managerId = 0;
            SysDept dept = deptMapper.selectDeptById(sheet.getOrganizationid().longValue());
            managerId = dept.getManagerId().intValue();
            message.setTouserid(managerId);
            message.setRelatedsheettype("拨付单");
            message.setRelatedsheetid(sheet.getSheetid());
            messageMapper.insertAudMessage(message);

            // 这个逻辑放到 controller 里实现，这是异步操作。
//            SendAppMsg(am.ToUserID.ToString());
//
//            //发送短信
//            CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                    "#type#=拨付单&#name#=" + lab_SheetCode.Text);
//
//            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(),        //发送给当前审核人
//                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=通过"
//                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);

        }
        else if (sheet.getSheetstatus() == SheetStatus.ChuShenPi.getCode()) {
            audSheetMapper.updateAudSheetStatus(sheet);

            //给相应人员发提醒消息
            //部门负责人审批通过，给经办人发信息
            String title = "拨付单审批中";
            String content = "您提交的拨付单："
                    +  sheet.getSheetcode()  + "由部门负责人审批通过。等待分管处审批。"
                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(sheet.getSheetuserid());
            message.setRelatedsheettype("拨付单");
            message.setRelatedsheetid(sheet.getSheetid());
            messageMapper.insertAudMessage(message);

            //给相关处的负责人发信息
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(10L);
            for (SysUserRole u : userRoleList) {
                Integer userid = u.getUserId().intValue();
                title =  "拨付单待您审批";
                content = "拨付单："
                        + sheet.getSheetcode() + "待您审批。"
                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=5&f=todo&type=bf&kid=" +  sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给分管处的负责人
                message.setTouserid(userid);
                message.setRelatedsheettype("拨付单");
                message.setRelatedsheetid(sheet.getSheetid());
                messageMapper.insertAudMessage(message);

// 这个逻辑放到 controller 里实现，这是异步操作。
//                SendAppMsg(am.ToUserID.ToString());
//
//                //发送短信
//                CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                        "#type#=拨付单&#name#=" + lab_SheetCode.Text);
            }



        }
        else if (sheet.getSheetstatus() == SheetStatus.FenGuanSuoShenPi.getCode()) {
            audSheetMapper.updateAudSheetStatus(sheet);


        }
        else if (sheet.getSheetstatus() == SheetStatus.SuoZhangShenPi.getCode()) {
            audSheetMapper.updateAudSheetStatus(sheet);


        }
        else if (sheet.getSheetstatus() == SheetStatus.ShenPiWanCheng.getCode()) {
            audSheetMapper.updateAudSheetStatus(sheet);
            AudBudgetpay pay = new AudBudgetpay();
            pay.setSheetid(sheet.getSheetid());
            pay.setAudittime(DateUtils.dateTimeNow());
            audBudgetpayMapper.updateAudBudgetpayAduitTime(pay);
        }

        //记录审核结论
        audSheetauditrecordMapper.insertAudSheetauditrecord(record);

        return result;
    }

}

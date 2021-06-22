package com.ruoyi.contract.service;


import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.*;
import com.ruoyi.audit.mapper.*;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.ConvertUpMoney;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SMSSender.Juhe;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.contract.domain.AudContract;
import com.ruoyi.contract.domain.AudContractdoc;
import com.ruoyi.contract.domain.AudContractpay;
import com.ruoyi.contract.mapper.AudContractMapper;
import com.ruoyi.contract.mapper.AudContractdocMapper;
import com.ruoyi.contract.mapper.AudContractpayMapper;
import com.ruoyi.expense.domain.AudExpense;
import com.ruoyi.project.mapper.AudProjectMapper;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudContractService {

    private static final Logger log = LoggerFactory.getLogger(AudContractService.class);

    @Resource
    AudContractMapper audContractMapper;

    @Resource
    AudSheetauditrecordMapper audSheetauditrecordMapper;

    @Resource
    AudContractpayMapper audContractpayMapper;

    @Resource
    AudContractdocMapper audContractdocMapper;


    @Resource
    AudProjectMapper projectMapper;

    @Resource
    AudBudgetpayMapper audBudgetpayMapper;

    @Resource
    AudSheetMapper audSheetMapper;

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
    private AudContractdocMapper contractdocMapper;


    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private SysUserMapper userMapper;

    public List<AudContract> selectContractTijiaoren(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractTijiaoren(sheet);

        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);

            log.debug("contract.getPaySheetList.size is " + contract.getPaySheetList().size());

        }

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }


    public List<AudContract> selectContractXiangmu(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractXiangmu(sheet);
        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }
        log.debug("request selectContractXiangmu list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudContract> selectContractBumen(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractBumen(sheet);
        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }
        log.debug("request selectContractBumen list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudContract> selectContractChu(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractChu(sheet);
        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }
        log.debug("request selectContractChu list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudContract> selectContractFenguansuo(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractFenguansuo(sheet);
        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }
        log.debug("request selectContractFenguansuo list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudContract> selectContractSuo(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractSuo(sheet);
        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }
        log.debug("request selectContractSuo list is " + sheetList.toString());

        return sheetList;
    }


    public List<AudContract> selectContractApplyDelete(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractApplyDelete(sheet);
        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }
        log.debug("request selectContractApplyDelete list is " + sheetList.toString());

        return sheetList;
    }

    public List<AudContract> selectContractToExecute(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractToExecute(sheet);

        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);
        }

        log.debug("request selectContractToExecute list is " + sheetList.toString());

        return sheetList;
    }



    public AudContract selectContractById(Integer contractid) {

        AudContract contract = audContractMapper.selectContractById(contractid);
        contract.setProjectinfo(projectMapper.selectProjectById(contract.getProjectid()));
        contract.setSupplierinfo(supplierinfoMapper.selectSupplierInfoById(contract.getSupplierid()));

        AudContractpay record = new AudContractpay();
        record.setContractid(contractid);
        List<AudContractpay> contractpayList = audContractpayMapper.selectContractPay(record);
        contract.setContractpayList(contractpayList);

        AudContractdoc doc = new AudContractdoc();
        record.setContractid(doc.getContractid());
        List<AudContractdoc> contractdocList = contractdocMapper.selectAudContractdocList(doc);
        contract.setContractdocList(contractdocList);

        return contract;
    }

    @Transactional
    public Integer addContractTijiaoren(AudContract contract) {
        Integer result = 1;

        // ConvertUpMoney.toChinese(contract.getContractmoney().toString());

        if (contract.getContractcode() == null || contract.getContractcode().isEmpty()) {
            BasUsedmaxserialnumber query = new BasUsedmaxserialnumber();
            query.setModulename("合同编号");
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

            String contractcode = "HT" + DateUtils.dateTimeNow("yyyy") + String.format("%04d", serialnumber);

            log.debug("contractcode is " + contractcode);

            contract.setContractcode(contractcode);
        }

        result = audContractMapper.insertAudContract(contract);

        Integer contractid = contract.getContractid();

        AudContractpay record = new AudContractpay();
        record.setContractid(contractid);

        audContractpayMapper.deleteContractPay(record);

        for (AudContractpay contractpay : contract.getContractpayList()) {
            contractpay.setContractid(contractid);
            audContractpayMapper.insertContractPay(contractpay);
        }

        return contractid;
    }




    @Transactional
    public Integer updateContractTijiaoren(AudContract contract) {
        Integer result = 1;

        ConvertUpMoney.toChinese(contract.getContractmoney().toString());

        result = audContractMapper.updateAudContract(contract);

        Integer contractid = contract.getContractid();

        AudContractpay record = new AudContractpay();
        record.setContractid(contractid);

        audContractpayMapper.deleteContractPay(record);

        for (AudContractpay contractpay : contract.getContractpayList()) {
            contractpay.setContractid(contractid);
            audContractpayMapper.insertContractPay(contractpay);
        }

        // log.debug("contract is " + contract.toString());

        if (contract.getContractdocList() != null && contract.getContractdocList().size() > 0) {

            List<AudContractdoc> docList = contract.getContractdocList();

            for (AudContractdoc doc : docList) {
                doc.setContractid(contract.getContractid());
            }
            log.debug("getContractdocList is " + docList.toString());

            audContractdocMapper.mergeAudContractdoc(docList);
            audContractdocMapper.sourceMergeAudContractdoc(docList);
        } else {
            AudContractdoc doc = new AudContractdoc();
            doc.setContractid(contractid);
            audContractdocMapper.deleteAudContractdoc(doc);
        }

        return result;
    }

    @Transactional
    public Integer mergeContractDoc(Integer contractid, BasDoc doc) {

        Integer result = basDocMapper.insertBasDoc(doc);

        log.debug("mergeContractDoc docid is " + doc.getDocid().toString());

        AudContractdoc record = new AudContractdoc();
        record.setContractid(contractid);
        record.setDocid(doc.getDocid());
        record.setIfdeleted(0);

        contractdocMapper.insertAudContractdoc(record);

        return doc.getDocid();
    }


    public List<AudContractdoc> selectAudContractdocList(AudContractdoc sheet) {

        List<AudContractdoc> sheetList = contractdocMapper.selectAudContractdocList(sheet);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }


    // 用于 提交审批。
    @Transactional
    public Integer updateAudContractStatus(AudContract contract) {
        Integer result = 0;

        if (contract.getSheetstatus().equals(SheetStatus.XiangMuShenPi.getCode())) {
            //合同提交审核了，这时候要自动生成第一期的拨付单

            result = audContractMapper.updateAudContractStatus(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            AudContract thisContract = this.selectContractById(contract.getContractid());
            AudContractpay thispay = thisContract.getContractpayList().get(0);

            // 生成 合同拨付单 ，逻辑如下：
            List<AudBudgetpay> paylist = new ArrayList<>();

            AudBudgetpay pay = new AudBudgetpay();
            pay.setProjectid(contract.getProjectid());
            pay.setSupplierid(contract.getSupplierid());
            pay.setZong(thisContract.getContractmoney());
            pay.setXiaoji(thispay.getPercentmoney());
            pay.setYiqian(BigDecimal.ZERO);
            pay.setBennian(BigDecimal.ZERO);
            pay.setBenci(thispay.getPercentmoney());

            paylist.add(pay);
            //生成 合同拨付单实体
            AudSheet sheet = new AudSheet();
            sheet.setSheettype("合同拨付单");
            sheet.setRelatedcontractid(contract.getContractid());
            sheet.setThispaytimes("1"); //付款期数为第一期
            sheet.setSheetuserid(contract.getContractuserid());
            sheet.setSheettime(DateUtils.dateTimeNow());
            sheet.setProjectid(contract.getProjectid());
            sheet.setOrganizationid(contract.getOrganizationid());
            sheet.setHejiZong(contract.getContractmoney()); //总付款额就是合同总金额
            sheet.setHejiXiaoji(pay.getXiaoji());    //小计就是第一次的金额
            sheet.setHejiYiqian(BigDecimal.ZERO);  //以前付款为0
            sheet.setHejiBennian(BigDecimal.ZERO);   //本年付款为0
            sheet.setHejiBenci(pay.getBenci());            //本次付款就是第一期的金额
            sheet.setDaxie(ConvertUpMoney.toChinese(pay.getBenci().toString()));             //大写还没转换，待修改
            sheet.setReferenceid("");
            sheet.setSheetstatus(SheetStatus.XiangMuShenPi.getCode());

            sheet.setBudgetpayList(paylist);
            //保存 合同拨付单

            if (sheet.getSheetcode() == null || sheet.getSheetcode().isEmpty()) {
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

            BigDecimal hejiZong = new BigDecimal(0);
            BigDecimal hejiXiaoji = new BigDecimal(0);
            BigDecimal hejiYiqian = new BigDecimal(0);
            BigDecimal hejiBennian = new BigDecimal(0);
            BigDecimal hejiBenci = new BigDecimal(0);

            for (AudBudgetpay s : sheet.getBudgetpayList()) {
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

            result = audSheetMapper.insertAudSheet(sheet);

            log.debug("sheet id is " + sheet.getSheetid());

            for (AudBudgetpay s : sheet.getBudgetpayList()) {
                log.debug("AudBudgetpay is " + s.toString());
                s.setSheetid(sheet.getSheetid());
                s.setProjectid(sheet.getProjectid());
                result = audBudgetpayMapper.insertAudBudgetpay(s);
            }


            //ism.AddNew(sheet, payList, false);


//            //记录系统日志
//            SystemLog slog = new SystemLog();
//            slog.LogType = (int)Entity.Enums.LogType.tijiaoshenpi;
//            slog.LogUserID = int.Parse(Session["CurrentUserID"].ToString().ToString());
//            slog.LogTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//            slog.LogContent = "提交了合同：" + txt_ContractName.Text + "。编号为：" + lab_ContractCode.Text;
//            ISystemLogManager islm = SystemLogManager.GetInstance();
//            islm.AddNew(slog);
//
//            //给相应人员发提醒消息
//            AudMessage am = new AudMessage();
//            am.MessageTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//            am.MessageTitle = "新合同待审批";
//            am.MessageContent = "合同：" + txt_ContractName.Text + "待审批。"
//                    + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=3&f=todo&type=ht&kid=" + lab_ContractID.Text + "\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
//            //查询消息要发送给哪个人员
//            //发送给项目的负责人
//            am.ToUserID = int.Parse(lab_PMUserID.Text);
//            am.RelatedSheetType = "合同";
//            am.RelatedSheetID = int.Parse(lab_ContractID.Text);
//            IAudMessageManager iamm = AudMessageManager.GetInstance();
//            iamm.AddNew(am);
//
//            api.MsgManager.SendToAUser(am.ToUserID.ToString(), am.MessageTitle,
//                    "新合同：" + txt_ContractName.Text + "  待审批。");
//
//            //发送短信
//            CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                    "#type#=合同&#name#=" + txt_ContractName.Text);


            String title = "新合同待审批";
            String content = "合同：" + contract.getContractname() + "待审批。" + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=3&f=todo&type=ht&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getProjectinfo().getProjectmanagerid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            // 发送推送

            Integer userid = message.getTouserid();
            List<AppClientinfo> clientList = clientinfoMapper.selectAppClientinfoByUserid(userid);
            for (AppClientinfo client : clientList) {
                String clientId = client.getClientid();
                String msgTitle = "新合同待审批";
                String msgBody = "新合同：" + contract.getContractname() + "待审批。";
                // 暂时 关闭，调试中。
                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
            }


            //发送短信

            // 获取 短信服务器的密钥信息
            String url = "";
            String key = "";

            List<SysDictData> dictList = dictDataMapper.selectDictDataByType("短信服务商");
            for (SysDictData dict : dictList) {
                if (dict.getDictLabel().equals("key")) {
                    key = dict.getDictValue();
                }
                if (dict.getDictLabel().equals("url")) {
                    url = dict.getDictValue();
                }
            }

            if (url.length() > 0) {
                log.debug("发送短息开始，" + url + " key: " + key);
                String msg = "#type#=合同&#name#=" + contract.getContractname();
                // 发送给用户。
                SysUser touser = userMapper.selectUserById(Long.valueOf(userid));

                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);
            } else {
                log.debug("发送短息失败， 密钥为空");
            }

        }
        else if (contract.getSheetstatus().equals(SheetStatus.ShenPiWanCheng.getCode())) {
            //如果是审批完成，则还要加上“审核通过”时间
            contract.setPasstime(DateUtils.dateTimeNow());
            result = audContractMapper.updateAudContractStatus(contract);

            contract.setPayedtimes(1);
            audContractMapper.updateAudContract(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

        }
        else if (contract.getSheetstatus().equals(SheetStatus.ShenQingZuoFei.getCode())) {
            //申请作废。
            AudApply apply = new AudApply();
            apply.setApplytype("合同作废申请");
            apply.setRelatedid(contract.getContractid());
            apply.setApplyuserid(contract.getConfirmUserid());
            apply.setApplytime(DateUtils.dateTimeNow());
            apply.setApplyreason(contract.getApplyDeleteReason());
            apply.setApplystatus(0);

            applyMapper.insertAudApply(apply);

            //更新合同状态
            result = audContractMapper.updateAudContractStatus(contract);

            //发送通知消息(发送给具有“合同作废申请审批”功能的人员)
            List<SysUser> userList = userMapper.selectUserListWithMenu(31);

            for (SysUser user : userList) {
                String title = "合同作废申请待审批";
                String content = "合同：" + contract.getContractname() + "申请作废，待审批。"
                        + "<a href=\"/Contract/ApplyDeleteList.aspx?kid=" + contract.getContractid() + "&f=todo\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
                AudMessage message = new AudMessage();
                message.setMessagetime(DateUtils.dateTimeNow());
                message.setMessagetitle(title);
                message.setMessagecontent(content);
                message.setTouserid(user.getUserId().intValue());
                message.setRelatedsheettype("合同作废申请");
                message.setRelatedsheetid(contract.getContractid());
                messageMapper.insertAudMessage(message);
            }
        }
        else if (contract.getSheetstatus().equals(SheetStatus.YiZuoFei.getCode())) {

        }
        else {
            result = audContractMapper.updateAudContractStatus(contract);
            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());
        }

        return result;
    }

    @Transactional
    public  Integer confirmApplydeleteAudContract(AudContract contract) {
        Integer result = 0;
        //已作废。
        AudApply apply = new AudApply();
        apply.setApplytype("合同作废申请");
        apply.setRelatedid(contract.getContractid());
        AudApply apply2 =  applyMapper.selectApplyByTypeAndRelatedID(apply);
        apply2.setApplystatus(1);
        apply2.setAudittime(DateUtils.dateTimeNow());
        apply2.setAudituserid(contract.getConfirmUserid());
        apply2.setAuditopinion("");
        applyMapper.updateAudApply(apply2);

        //更新合同状态
        result = audContractMapper.updateAudContractStatus(contract);
        //消除待办事项
        AudMessage message = new AudMessage();
        message.setRelatedsheettype("合同作废申请");
        message.setRelatedsheetid(contract.getContractid());
        messageMapper.updateIfProcessedAudMessageBySheetAndId(message);

        //发消息给经办人
        String title = "合同作废申请已审批通过";
        String content =  "您提交的合同：" + contract.getContractname() + "   的作废申请已经审批通过。"
                + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";
        message = new AudMessage();
        message.setMessagetime(DateUtils.dateTimeNow());
        message.setMessagetitle(title);
        message.setMessagecontent(content);
        message.setTouserid(contract.getContractuserid());
        message.setRelatedsheettype("合同作废申请");
        message.setRelatedsheetid(contract.getContractid());
        messageMapper.insertAudMessage(message);

        return result;
    }

    @Transactional
    public  Integer nopassApplydeleteAudContract(AudContract contract) {
        Integer result = 0;
        //已作废。
        AudApply apply = new AudApply();
        apply.setApplytype("合同作废申请");
        apply.setRelatedid(contract.getContractid());
        AudApply apply2 =  applyMapper.selectApplyByTypeAndRelatedID(apply);
        apply2.setApplystatus(2);
        apply2.setAudittime(DateUtils.dateTimeNow());
        apply2.setAudituserid(contract.getConfirmUserid());
        apply2.setAuditopinion("");
        applyMapper.updateAudApply(apply2);

        //更新合同状态
        result = audContractMapper.updateAudContractStatus(contract);
        //消除待办事项
        AudMessage message = new AudMessage();
        message.setRelatedsheettype("合同作废申请");
        message.setRelatedsheetid(contract.getContractid());
        messageMapper.updateIfProcessedAudMessageBySheetAndId(message);

        //发消息给经办人
        String title = "合同作废申请审批不通过";
        String content = "您提交的合同：" + contract.getContractname() + "   的作废申请审批不通过。"
                + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" +  contract.getContractid() + "\" target=\"_self\" >查看</a> ";
        message = new AudMessage();
        message.setMessagetime(DateUtils.dateTimeNow());
        message.setMessagetitle(title);
        message.setMessagecontent(content);
        message.setTouserid(contract.getContractuserid());
        message.setRelatedsheettype("合同作废申请");
        message.setRelatedsheetid(contract.getContractid());
        messageMapper.insertAudMessage(message);

        return result;
    }



    // 用于 审批流程。
    @Transactional
    public Integer updateAudContractStatus(AudContract contract, AudSheetauditrecord record ) {
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

        if (contract.getSheetstatus().equals(SheetStatus.XiangMuShenPi.getCode()) == false) {
            //消除经办人以前的待办事项“针对同一个类型的同一张单，因为要生成新的待办事项，所以讲旧的设为已读”
            // iamm.SetProcessed(lab_SheetUserID.Text, "合同", lab_ContractID.Text);
            AudMessage query = new AudMessage();
            query.setProcessedtime(DateUtils.dateTimeNow());
            query.setTouserid(contract.getContractuserid());
            query.setRelatedsheettype("合同");
            query.setRelatedsheetid(contract.getContractid());
            messageMapper.updateIfProcessedAudMessageBySheetAndId(query);

            //消除本人的待办事项
            //  iamm.SetProcessed(Session["CurrentUserID"].ToString(), "合同", lab_ContractID.Text);
            query = new AudMessage();
            query.setProcessedtime(DateUtils.dateTimeNow());
            if (record != null) {
                query.setTouserid(record.getAudituserid());
            }
            query.setRelatedsheettype("合同");
            query.setRelatedsheetid(contract.getContractid());
            messageMapper.updateIfProcessedAudMessageBySheetAndId(query);
        }


        if (contract.getSheetstatus() == SheetStatus.YiZuoFei.getCode()) {
            // 已作废。
//            audSheetMapper.updateAudSheetStatus(contract);
            // 级联更新， 合同拨付单的状态等信息
//            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            return 1;
        }
        else if (contract.getSheetstatus() == SheetStatus.NoPass.getCode()) {
            audContractMapper.updateAudContractStatus(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            //给相应人员发提醒消息
            String title = "新合同审批不通过";
            String content =   "您提交的合同：" + contract.getContractname() + "   审批不通过。"
                    + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);

            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getContractuserid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            //发送短信 发送给经办人
            String msg = "#type#=合同&#name#=" + contract.getContractname()  + "&#result#=不通过";
            String jinbanren = contract.getContractuserid().toString();

            SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getContractuserid()));

            Juhe.sendSMS_Audited_ToSheetUser(touser.getPhonenumber(), msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=不通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;

            touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

        }
        else  if (contract.getSheetstatus().equals(SheetStatus.XiangMuShenPi.getCode())) {
            //合同提交审核了，这时候要自动生成第一期的拨付单

            result = audContractMapper.updateAudContractStatus(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            AudContract thisContract = this.selectContractById(contract.getContractid());
            AudContractpay thispay = thisContract.getContractpayList().get(0);

            // 生成 合同拨付单 ，逻辑如下：
            List<AudBudgetpay> paylist = new ArrayList<>();

            AudBudgetpay pay = new AudBudgetpay();
            pay.setProjectid(contract.getProjectid());
            pay.setSupplierid(contract.getSupplierid());
            pay.setZong(thisContract.getContractmoney());
            pay.setXiaoji(thispay.getPercentmoney());
            pay.setYiqian(BigDecimal.ZERO);
            pay.setBennian(BigDecimal.ZERO);
            pay.setBenci(thispay.getPercentmoney());

            paylist.add(pay);
            //生成 合同拨付单实体
            AudSheet sheet = new AudSheet();
            sheet.setSheettype("合同拨付单");
            sheet.setRelatedcontractid(contract.getContractid());
            sheet.setThispaytimes("1"); //付款期数为第一期
            sheet.setSheetuserid(contract.getContractuserid());
            sheet.setSheettime(DateUtils.dateTimeNow());
            sheet.setProjectid(contract.getProjectid());
            sheet.setOrganizationid(contract.getOrganizationid());
            sheet.setHejiZong(contract.getContractmoney()); //总付款额就是合同总金额
            sheet.setHejiXiaoji(pay.getXiaoji());    //小计就是第一次的金额
            sheet.setHejiYiqian(BigDecimal.ZERO);  //以前付款为0
            sheet.setHejiBennian(BigDecimal.ZERO);   //本年付款为0
            sheet.setHejiBenci(pay.getBenci());            //本次付款就是第一期的金额
            sheet.setDaxie(ConvertUpMoney.toChinese(pay.getBenci().toString()));             //大写还没转换，待修改
            sheet.setReferenceid("");
            sheet.setSheetstatus(SheetStatus.XiangMuShenPi.getCode());

            sheet.setBudgetpayList(paylist);
            //保存 合同拨付单

            if (sheet.getSheetcode() == null || sheet.getSheetcode().isEmpty()) {
                BasUsedmaxserialnumber querysn = new BasUsedmaxserialnumber();
                querysn.setModulename("拨付单号");
                querysn.setSomedate(DateUtils.getDate());
                Integer serialnumber = 1;
                BasUsedmaxserialnumber recordsn = usedmaxserialnumberMapper.selectBasUsedmaxserialnumber(querysn);
                if (recordsn == null) {
                    querysn.setUsedmaxserialnumber(serialnumber);
                    usedmaxserialnumberMapper.insertBasUsedmaxserialnumber(querysn);
                } else {
                    serialnumber = recordsn.getUsedmaxserialnumber() + 1;
                    querysn.setUsedmaxserialnumber(serialnumber);
                    usedmaxserialnumberMapper.updateBasUsedmaxserialnumber(querysn);
                }

                String sheetCode = "BFD" + DateUtils.dateTime() + String.format("%03d", serialnumber);

                log.debug("sheetCode is " + sheetCode);

                sheet.setSheetcode(sheetCode);
            }

            BigDecimal hejiZong = new BigDecimal(0);
            BigDecimal hejiXiaoji = new BigDecimal(0);
            BigDecimal hejiYiqian = new BigDecimal(0);
            BigDecimal hejiBennian = new BigDecimal(0);
            BigDecimal hejiBenci = new BigDecimal(0);

            for (AudBudgetpay s : sheet.getBudgetpayList()) {
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

            result = audSheetMapper.insertAudSheet(sheet);

            log.debug("sheet id is " + sheet.getSheetid());

            for (AudBudgetpay s : sheet.getBudgetpayList()) {
                log.debug("AudBudgetpay is " + s.toString());
                s.setSheetid(sheet.getSheetid());
                s.setProjectid(sheet.getProjectid());
                result = audBudgetpayMapper.insertAudBudgetpay(s);
            }


            String title = "新合同待审批";
            String content = "合同：" + contract.getContractname() + "待审批。" + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=3&f=todo&type=ht&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getProjectinfo().getProjectmanagerid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            // 发送推送

            Integer userid = contract.getProjectinfo().getProjectmanagerid();
            List<AppClientinfo> clientList = clientinfoMapper.selectAppClientinfoByUserid(userid);
            for (AppClientinfo client : clientList) {
                String clientId = client.getClientid();
                String msgTitle = "新合同待审批";
                String msgBody = "新合同：" + contract.getContractname() + "待审批。";
                // 暂时 关闭，调试中。
                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
            }


            //发送短信

            if (url.length() > 0) {
                log.debug("发送短息开始，" + url + " key: " + key);
                String msg = "#type#=合同&#name#=" + contract.getContractname();
                SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getProjectinfo().getProjectmanagerid()));

                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);
            } else {
                log.debug("发送短息失败， 密钥为空");
            }

        }
        else if (contract.getSheetstatus() == SheetStatus.BuMenShenPi.getCode()) {
            audContractMapper.updateAudContractStatus(contract);
            log.debug("updateAudContract is " + contract.toString());

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());


            //给相应人员发提醒消息
            //项目负责人审批通过，给经办人发信息
            String title = "新合同审批中";
            String content = "您提交的合同：" +  contract.getContractname() + "   项目负责人审批通过。等待部门负责人审批。"
                    + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" +  contract.getContractid() + "\" target=\"_self\" >查看</a> ";

            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getContractuserid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            //给部门负责人发信息
            title =  "新合同待您审批";
            content =  "合同：" + contract.getContractname() + "待您审批。"
                    + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=4&f=todo&type=ht&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";   //t=4表示项目负责人审批
            //查询消息要发送给哪个人员
            //发送给项目所属的部门的负责人
            Integer managerId = 0;
            SysDept dept = deptMapper.selectDeptById(contract.getProjectinfo().getOrganizationid().longValue());
            managerId = dept.getManagerId().intValue();
            message.setTouserid(managerId);
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            // 发送推送

            Integer userid = message.getTouserid();
            List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
            for (AppClientinfo client : clientList) {
                String clientId = client.getClientid();
                String msgTitle = "新合同待您审批";
                String msgBody = "合同：" + contract.getContractname() + "待您审批。";
                // 暂时 关闭，调试中。
                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
            }

            //发送短信 经办人。
            String msg = "#type#=合同&#name#=" + contract.getContractname();
            SysUser touser = userMapper.selectUserById(Long.valueOf(contract.getContractuserid()));
            Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;

            touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

        }
        else if (contract.getSheetstatus() == SheetStatus.ChuShenPi.getCode()) {
            audContractMapper.updateAudContractStatus(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            //给相应人员发提醒消息
            //部门负责人审批通过，给经办人发信息
            String title = "新合同审批中";
            String content =  "您提交的合同：" + contract.getContractname() + "   部门负责人审批通过。等待分管处审批。"
                    + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);

            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getContractuserid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            //给相关处的负责人发信息
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(10L);

            for (SysUserRole u : userRoleList) {
                log.debug("userrole is " + u.toString());
                Integer userid = u.getUserId().intValue();
                title =  "新合同待您审批";
                content =  "合同：" + contract.getContractname() + "待您审批。"
                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=5&f=todo&type=ht&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给分管处的负责人
                message.setTouserid(userid);
                message.setRelatedsheettype("合同");
                message.setRelatedsheetid(contract.getContractid());
                messageMapper.insertAudMessage(message);


                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
                for (AppClientinfo client : clientList) {
                    String clientId = client.getClientid();
                    String msgTitle = "新合同待您审批";
                    String msgBody = "合同：" + contract.getContractname() + "待您审批。";
                    // 暂时 关闭，调试中。
                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
                }

                //发送短信
                String msg = "#type#=合同&#name#=" + contract.getContractname();
                SysUser touser = userMapper.selectUserById(Long.valueOf(userid));
                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            }

            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            String  msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;

            SysUser touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));

            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

        }
        else if (contract.getSheetstatus() == SheetStatus.FenGuanSuoShenPi.getCode()) {
            audContractMapper.updateAudContractStatus(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            //给相应人员发提醒消息
            //相关处的负责人审批通过，给经办人发信息
            String title = "新合同审批中";
            String content ="您提交的合同：" + contract.getContractname()+ "   分管处审批通过。等待分管所长审批。"
                    + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getContractuserid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            //给分管所长发信息
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(11L);
            for (SysUserRole u : userRoleList) {
                Integer userid = u.getUserId().intValue();
                title =  "新合同待您审批";
                content =  "合同：" + contract.getContractname() + "待您审批。"
                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=6&f=todo&type=ht&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给分管所长
                message.setTouserid(userid);
                message.setRelatedsheettype("合同");
                message.setRelatedsheetid(contract.getContractid());
                messageMapper.insertAudMessage(message);

                //发送app推送
                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
                for (AppClientinfo client : clientList) {
                    String clientId = client.getClientid();
                    String msgTitle = "新合同待您审批";
                    String msgBody = "合同：" + contract.getContractname() + "待您审批。";
                    // 暂时 关闭，调试中。
                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
                }
                //发送短信
                String msg = "#type#=拨付单&#name#=" + contract.getContractname();
                SysUser touser = userMapper.selectUserById(Long.valueOf(userid));
                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            }

            //发送给当前审核人，分管所长 审批之后 C#中 不发短信通知了。

        }
        else if (contract.getSheetstatus() == SheetStatus.SuoZhangShenPi.getCode()) {
            audContractMapper.updateAudContractStatus(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());


            //给相应人员发提醒消息
            //分管所长审批通过，给经办人发信息
            String title = "新合同审批中";
            String content =  "您提交的合同：" + contract.getContractname() + "   分管所长审批通过。等待所长审批。"
                    + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid()  + "\" target=\"_self\" >查看</a> ";
            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getContractuserid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);

            //给所长发信息
            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(12L);
            for (SysUserRole u : userRoleList) {
                Integer userid = u.getUserId().intValue();
                title =  "新合同待您审批";
                content =  "合同：" + contract.getContractname()  + "待您审批。"
                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=7&f=todo&type=ht&kid=" + contract.getContractid() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
                //查询消息要发送给哪个人员
                //发送给 所长
                message.setTouserid(userid);
                message.setRelatedsheettype("合同");
                message.setRelatedsheetid(contract.getContractid());
                messageMapper.insertAudMessage(message);

                //发送app推送
                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
                for (AppClientinfo client : clientList) {
                    String clientId = client.getClientid();
                    String msgTitle = "新合同待您审批";
                    String msgBody = "合同：" + contract.getContractname() + "待您审批。";
                    // 暂时 关闭，调试中。
                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
                }
                //发送短信
                String msg = "#type#=拨付单&#name#=" + contract.getContractname();
                SysUser touser = userMapper.selectUserById(Long.valueOf(userid));
                Juhe.sendSMS_ToAudit(touser.getPhonenumber(), msg, url, key);

            }

            //发送给当前审核人，分管所长 审批之后 C#中 不发短信通知了。


        }
        else if (contract.getSheetstatus() == SheetStatus.ShenPiWanCheng.getCode()) {
            //如果是审批完成，则还要加上“审核通过”时间
            contract.setPasstime(DateUtils.dateTimeNow());
            audContractMapper.updateAudContractStatus(contract);

            contract.setPayedtimes(1);
            audContractMapper.updateAudContract(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

            //  //合同审批通过后，要改写合同正文文档，加上签名，加上二维码
            //                        ProcessDoc();

            //给相应人员发提醒消息
            //分管所长审批通过，给经办人发信息
            String title = "新合同审批完成";
            String content = "";
            log.debug("新合同审批完成 audit is " + contract.getContractname());
            if (record.getAudittype().equals("6")) {
                content = "您提交的合同：" + contract.getContractname()  + "   由分管所长审批通过。"
                        + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid()  + "\" target=\"_self\" >查看</a> ";

            }
            else if (record.getAudittype().equals("7")) {
                content ="您提交的合同：" + contract.getContractname()  + "   由所长审批通过。"
                        + "<a href=\"/Contract/ContractList_tijiaoren.aspx?f=todo&kid=" + contract.getContractid()  + "\" target=\"_self\" >查看</a> ";

            }

            AudMessage message = new AudMessage();
            message.setMessagetime(DateUtils.dateTimeNow());
            message.setMessagetitle(title);
            message.setMessagecontent(content);
            //查询消息要发送给哪个人员
            //发送给合同的经办人
            message.setTouserid(contract.getContractuserid());
            message.setRelatedsheettype("合同");
            message.setRelatedsheetid(contract.getContractid());
            messageMapper.insertAudMessage(message);


            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            String  msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;

            SysUser touser = userMapper.selectUserById(Long.valueOf(record.getAudituserid()));
            Juhe.sendSMS_Audited(touser.getPhonenumber(), msg, url, key);

            //发送短信，给经办人
            msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过";
            touser = userMapper.selectUserById(Long.valueOf(contract.getContractuserid()));

            Juhe.sendSMS_Audited_ToSheetUser(touser.getPhonenumber(), msg, url, key);

        }

        //记录审核结论
        if (record != null ) {
            audSheetauditrecordMapper.insertAudSheetauditrecord(record);
        }

        return result;
    }

    public List<AudSheet> selectContractPaySheetByContractid(Integer contractid) {

        List<AudSheet> sheetList = audSheetMapper.selectContractPaySheetByContractid(contractid);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }

    // 用于
    @Transactional
    public Integer updateContractPaysheet(Integer contractid, Integer status)
    {

        List<AudSheet> sheetList =  audSheetMapper.selectContractPaySheetByContractid(contractid);

        for (AudSheet sheet : sheetList) {

            if (sheet.getThispaytimes() == "1") {
                sheet.setSheetstatus(status);
                audSheetMapper.updateAudSheetStatus(sheet);

                //如果是完成状态，还要在明细记录里写入审核时间。表示这个付款明细已经执行了
                if (status ==  SheetStatus.ShenPiWanCheng.getCode()) {
                    AudBudgetpay record = new AudBudgetpay();
                    record.setSheetid(contractid);
                    record.setAudittime(DateUtils.dateTimeNow());
                    audBudgetpayMapper.updateAudBudgetpayAduitTime(record);
                }
            }
        }

        //
//            //将对应的第一个拨付单的状态设置成相同的状态
//            //获取该合同对应的第一个拨付单的ID
//            string idSql = " select sheetID from Aud_Sheet "
//                    + " where RelatedContractID = " + contractID
//                    + " and ThisPayTimes = '1' ";    //第一次付款的拨付单
//            string id = DBAccess.DB.ExecuteScalar(CommandType.Text , idSql).ToString();
//            ISheetManager isheetm = SheetManager.GetInstance();
//            isheetm.SetStatus(status, id);

        return 1;
    }

    // 用于
    @Transactional
    public Integer updateContractFirstPay(Integer contractid)
    {

        AudContract record = new AudContract();
        record.setContractid(contractid);
        record.setFirstpaytime(DateUtils.dateTimeNow());

        Integer result =  audContractMapper.updateAudContractFirstPayTime(record);

        return result;
    }



    public List<AudContract> queryContractList(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.queryContractList(sheet);


        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }



}

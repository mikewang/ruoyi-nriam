package com.ruoyi.contract.service;


import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysDictData;
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
import com.ruoyi.project.mapper.AudProjectMapper;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.sheet.domain.AudBudgetpay;
import com.ruoyi.sheet.domain.AudSheet;
import com.ruoyi.sheet.domain.AudSheetauditrecord;
import com.ruoyi.sheet.domain.BasUsedmaxserialnumber;
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
import java.util.List;

@Service
public class AudContractService {

    private static final Logger log = LoggerFactory.getLogger(AudApplyService.class);

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

    @Resource
    private AudContractdocMapper contractdocMapper;


    public List<AudContract> selectContractTijiaoren(AudContract sheet) {

        List<AudContract> sheetList = audContractMapper.selectContractTijiaoren(sheet);

        for (AudContract contract : sheetList) {
            List<AudSheet> paySheets = audSheetMapper.selectContractPaySheetByContractid(contract.getContractid());
            contract.setPaySheetList(paySheets);

//            AudContractpay record = new AudContractpay();
//            record.setContractid(contract.getContractid());
//            contract.setContractpayList(audContractpayMapper.selectContractPay(record));
//
//            AudContractdoc doc = new AudContractdoc();
//            record.setContractid(doc.getContractid());
//            List<AudContractdoc> contractdocList = contractdocMapper.selectAudContractdocList(doc);
//            contract.setContractdocList(contractdocList);
//
//            contract.setProjectinfo(projectMapper.selectProjectById(contract.getProjectid()));
//            contract.setSupplierinfo(supplierinfoMapper.selectSupplierInfoById(contract.getSupplierid()));
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



    public AudContract selectContractById(Integer contractid) {

        AudContract contract = audContractMapper.selectContractById(contractid);

        AudContractpay record = new AudContractpay();
        record.setContractid(contractid);
        List<AudContractpay> contractpayList = audContractpayMapper.selectContractPay(record);
        contract.setContractpayList(contractpayList);

        AudContractdoc doc = new AudContractdoc();
        record.setContractid(doc.getContractid());
        List<AudContractdoc> contractdocList = contractdocMapper.selectAudContractdocList(doc);
        contract.setContractdocList(contractdocList);

        contract.setProjectinfo(projectMapper.selectProjectById(contract.getProjectid()));
        contract.setSupplierinfo(supplierinfoMapper.selectSupplierInfoById(contract.getSupplierid()));
        return contract;
    }

    @Transactional
    public Integer addContractTijiaoren(AudContract contract) {
        Integer result = 1;

       // ConvertUpMoney.toChinese(contract.getContractmoney().toString());

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
                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
            } else {
                log.debug("发送短息失败， 密钥为空");
            }

        } else if (contract.getSheetstatus().equals(SheetStatus.ShenPiWanCheng.getCode())) {
            //如果是审批完成，则还要加上“审核通过”时间
            result = audContractMapper.updateAudContractStatus(contract);

            contract.setPayedtimes(1);
            contract.setPasstime(DateUtils.dateTimeNow());
            audContractMapper.updateAudContract(contract);

            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

        } else {
            result = audContractMapper.updateAudContractStatus(contract);
            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());
        }

        //将对应的第一个拨付单的状态设置成相同的状态
        //获取该合同对应的第一个拨付单的ID
//        string idSql = " select sheetID from Aud_Sheet "
//                + " where RelatedContractID = " + contractID
//                + " and ThisPayTimes = '1' ";    //第一次付款的拨付单
//        string id = DBAccess.DB.ExecuteScalar(CommandType.Text , idSql).ToString();
//        ISheetManager isheetm = SheetManager.GetInstance();
//        isheetm.SetStatus(status, id);


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

        if (contract.getSheetstatus() == SheetStatus.YiZuoFei.getCode()) {
//            audSheetMapper.updateAudSheetStatus(contract);
            // 级联更新， 合同拨付单的状态等信息
            updateContractPaysheet(contract.getContractid(), contract.getSheetstatus());

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
            // 暂时 关闭，调试中。
            Juhe.sendSMS_Audited_ToSheetUser("13776614820", msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=不通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Juhe.sendSMS_Audited("13776614820", msg, url, key);

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
            // 暂时 关闭，调试中。
            Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Juhe.sendSMS_Audited("13776614820", msg, url, key);


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
                // 暂时 关闭，调试中。
                // userid ..
                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

            }

            //发送短信 发送给当前审核人
            String danwei = contract.getSupplierinfo().getSuppliername();

            String  msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过" + "&#money#=" + contract.getContractmoney().toString() + "&#danwei#=" + danwei;
            String danqianshenheren = record.getAudituserid().toString();

            Juhe.sendSMS_Audited("13776614820", msg, url, key);

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
                // 暂时 关闭，调试中。
                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

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
                // 暂时 关闭，调试中。
                Juhe.sendSMS_ToAudit("13776614820", msg, url, key);

            }

            //发送给当前审核人，分管所长 审批之后 C#中 不发短信通知了。


        }
        else if (contract.getSheetstatus() == SheetStatus.ShenPiWanCheng.getCode()) {
            audContractMapper.updateAudContractStatus(contract);

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
            String danqianshenheren = record.getAudituserid().toString();

            Juhe.sendSMS_Audited("13776614820", msg, url, key);


            //发送短信，给经办人
            msg = "#type#=合同&#name#=" + contract.getContractname() + "&#result#=通过";
            // 暂时 关闭，调试中。
            Juhe.sendSMS_Audited_ToSheetUser("13776614820", msg, url, key);

            //对应的协作单位审批通过 ，在协作单位开发完成后补充。
//            for (int s = 0; s < dpl_SupplierIDList.Items.Count; s++)
//            {
//                isupm.AuditPass(dpl_SupplierIDList.Items[s].Text);
//            }

        }

        //记录审核结论
        audSheetauditrecordMapper.insertAudSheetauditrecord(record);

        return result;
    }

    public List<AudSheet> selectContractPaySheetByContractid(Integer contractid) {

        List<AudSheet> sheetList = audSheetMapper.selectContractPaySheetByContractid(contractid);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }


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

//
//
//    public List<AudBudgetpay> selectSheetBudgetPayById(Integer sheetid) {
//
//        List<AudBudgetpay> payList = audBudgetpayMapper.selectBudgetPayOfSheetBySheetid(sheetid);
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
//    public List<AudContractauditrecord> selectSheetauditRecord(AudContractauditrecord query) {
//
//        log.debug("selectBudgetPayRecord  query is " + query.toString());
//
//        List<AudContractauditrecord> payList = audContractauditrecordMapper.selectSheetauditRecord(query);
//
//        log.debug("request selectBudgetPayRecord  list is " + payList.toString());
//
//        return payList;
//    }
//
//
//    @Transactional
//    public Integer addContractTijiaoren(AudContract contract) {
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
//        if (sheet.getSheetcode() == null || sheet.getSheetcode().isEmpty()) {
////        if (ifCopy == false)   //说明是新单，单号要新生成。否则，就用旧单号
////        {
////            //在保存之前，确保号是最新的
////            //获取最新的流水号
////            ISerialNumberManager isnm = SerialNumberManager.GetInstance();
////            int newSerialNumber = isnm.GetNewSerialNumber("拨付单号");
////
////            //（单号规则：BFD+8位年月日+3位流水号）  待定
////            curCode = "BFD" + DateTime.Now.ToString("yyyyMMdd") + newSerialNumber.ToString("000");
////            newEntity.SheetCode = curCode;
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
//            sheet.setSheetcode(sheetCode);
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
//        Integer result = audContractMapper.insertAudContract(sheet);
//
//        log.debug("sheet id is " + sheet.getSheetid());
//
//        for(AudBudgetpay s : sheet.getBudgetpayList()) {
//            log.debug("AudBudgetpay is " + s.toString());
//            s.setSheetid(sheet.getSheetid());
//            s.setProjectid(sheet.getProjectid());
//            result = audBudgetpayMapper.insertAudBudgetpay(s);
//        }
//
//
//        //给相应人员发提醒消息
//        String title = "新的拨付单待审批";
//        String content = "拨付单：" + sheet.getSheetcode() + "待审批。"
//                + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=3&f=todo&type=bf&kid=" + sheet.getSheetid() + "\" target=\"_self\" >查看</a> ";   //t=3表示项目负责人审批
//        //发送给项目的负责人
//        AudMessage message = new AudMessage();
//        message.setMessagetime(DateUtils.dateTimeNow());
//        message.setMessagetitle(title);
//        message.setMessagecontent(content);
//        message.setTouserid(sheet.getProjectmanagerid());
//        message.setRelatedsheettype("拨付单");
//        message.setRelatedsheetid(sheet.getSheetid());
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
//            String msgBody = "新的拨付单：" + sheet.getSheetcode() + "待审批。";
//            // 暂时 关闭，调试中。
//            PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//        }
//
//        //发送短信
//        String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode();
//        // 暂时 关闭，调试中。
//        Juhe.sendSMS_ToAudit("13776614820", msg, url, key);
//
//        return result;
//    }
////
//
//    public List<AudContract> selectSheetXiangmuByUserid(Integer uid) {
//
//        List<AudContract> sheetList = audContractMapper.selectSheetXiangmuByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudContract> selectSheetBuMenByUserid(Integer uid) {
//
//        List<AudContract> sheetList = audContractMapper.selectSheetBuMenByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudContract> selectSheetChuByUserid(Integer uid) {
//
//
//        List<AudContract> sheetList = audContractMapper.selectSheetChuByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudContract> selectSheetFenguansuoByUserid(Integer uid) {
//
//
//        List<AudContract> sheetList = audContractMapper.selectSheetFenguansuoByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    public List<AudContract> selectSheetSuozhangByUserid(Integer uid) {
//
//
//        List<AudContract> sheetList = audContractMapper.selectSheetSuozhangByUserid(uid);
//
//        log.debug("request sheetList list is " + sheetList.toString());
//
//        return sheetList;
//    }
//
//    @Transactional
//    public Integer updateSheetAuditStatus(AudContract sheet, AudContractauditrecord record ) {
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
//        // iamm.SetProcessed(lab_SheetUserID.Text, "拨付单", lab_SheetID.Text);
//        AudMessage query = new AudMessage();
//        query.setProcessedtime(DateUtils.dateTimeNow());
//        query.setTouserid(sheet.getSheetuserid());
//        query.setRelatedsheettype("拨付单");
//        query.setRelatedsheetid(sheet.getSheetid());
//        messageMapper.updateIfProcessedAudMessageBySheetAndId(query);
//
//        //消除本人的待办事项
//        // iamm.SetProcessed(Session["CurrentUserID"].ToString(), "拨付单", lab_SheetID.Text);
//        query = new AudMessage();
//        query.setProcessedtime(DateUtils.dateTimeNow());
//        if (record != null) {
//            query.setTouserid(record.getAudituserid());
//        }
//        query.setRelatedsheettype("拨付单");
//        query.setRelatedsheetid(sheet.getSheetid());
//        messageMapper.updateIfProcessedAudMessageBySheetAndId(query);
//
//        if (sheet.getSheetstatus() == SheetStatus.YiZuoFei.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//            return 1;
//        }
//        else if (sheet.getSheetstatus() == SheetStatus.NoPass.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//            //给相应人员发提醒消息
//            String title = "拨付单审批不通过";
//            String content =  "您提交的拨付单："
//                    + sheet.getSheetcode() + "审批不通过。"
//                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getSheetuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
//            messageMapper.insertAudMessage(message);
//            // 这个逻辑放到 controller 里实现，这是异步操作。
////            //发送短信
////            CommonFunc.SendSMS_Audited_ToSheetUser(am.ToUserID.ToString(),        //发送给经办人
////                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=不通过");
////            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(),        //发送给当前审核人
////                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=不通过"
////                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);
//
//            //发送短信
//            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=不通过";
//            // 暂时 关闭，调试中。
//            Juhe.sendSMS_Audited_ToSheetUser("13776614820", msg, url, key);
//
//            String danwei = "";
//            if (sheet.getBudgetpayList().size() > 1) {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername() + "...等";
//            }
//            else {
//                danwei = sheet.getBudgetpayList().get(0).getSuppliername();
//            }
//            msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=不通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//        }
//        else if (sheet.getSheetstatus() == SheetStatus.BuMenShenPi.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//
//            //给相应人员发提醒消息
//            //项目负责人审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content =  "您提交的拨付单："
//                    + sheet.getSheetcode() + "由项目负责人审批通过。等待部门负责人审批。"
//                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" +  sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getSheetuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
//            messageMapper.insertAudMessage(message);
//
//            //给部门负责人发信息
//            title =  "拨付单待您审批";
//            content =  "拨付单："
//                    +  sheet.getSheetcode() + "待您审批。"
//                    + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=4&f=todo&type=bf&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";   //t=4表示部门负责人审批
//            //查询消息要发送给哪个人员
//            //发送给项目所属的部门的负责人
//            Integer managerId = 0;
//            SysDept dept = deptMapper.selectDeptById(sheet.getOrganizationid().longValue());
//            managerId = dept.getManagerId().intValue();
//            message.setTouserid(managerId);
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
//            messageMapper.insertAudMessage(message);
//
//            // 这个逻辑放到 controller 里实现，这是异步操作。
////            SendAppMsg(am.ToUserID.ToString());  api.MsgManager.SendToAUser(userID, "拨付单待您审批", "拨付单：" + lab_SheetCode.Text + "待您审批。");
////
////            //发送短信
////            CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
////                    "#type#=拨付单&#name#=" + lab_SheetCode.Text);
////
////            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(),        //发送给当前审核人
////                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=通过"
////                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);
//
//            // 发送推送
//
//            Integer userid = message.getTouserid();
//            List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//            for (AppClientinfo client : clientList) {
//                String clientId = client.getClientid();
//                String msgTitle = "拨付单待您审批";
//                String msgBody = "拨付单：" + sheet.getSheetcode() + "待您审批。";
//                // 暂时 关闭，调试中。
//                PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//            }
//
//            //发送短信
//            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode();
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
//            msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//
//        }
//        else if (sheet.getSheetstatus() == SheetStatus.ChuShenPi.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//
//            //给相应人员发提醒消息
//            //部门负责人审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content = "您提交的拨付单："
//                    +  sheet.getSheetcode()  + "由部门负责人审批通过。等待分管处审批。"
//                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getSheetuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
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
//                        + sheet.getSheetcode() + "待您审批。"
//                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=5&f=todo&type=bf&kid=" +  sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
//                //查询消息要发送给哪个人员
//                //发送给分管处的负责人
//                message.setTouserid(userid);
//                message.setRelatedsheettype("拨付单");
//                message.setRelatedsheetid(sheet.getSheetid());
//                messageMapper.insertAudMessage(message);
//
//                // 这个逻辑放到 controller 里实现，这是异步操作。
//                //                SendAppMsg(am.ToUserID.ToString());
//                //
//                //                //发送短信
//                //                CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                //                        "#type#=拨付单&#name#=" + lab_SheetCode.Text);
//
//                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//                for (AppClientinfo client : clientList) {
//                    String clientId = client.getClientid();
//                    String msgTitle = "拨付单待您审批";
//                    String msgBody = "拨付单：" + sheet.getSheetcode() + "待您审批。";
//                    // 暂时 关闭，调试中。
//                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//                }
//                //发送短信
//                String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode();
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
//            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//        }
//        else if (sheet.getSheetstatus() == SheetStatus.FenGuanSuoShenPi.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//
//            //给相应人员发提醒消息
//            //相关处的负责人审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content = "您提交的拨付单："
//                    +  sheet.getSheetcode()  + "由分管处审批通过。等待分管所长审批。"
//                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getSheetuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
//            messageMapper.insertAudMessage(message);
//
//            //给分管所长发信息
//            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(11L);
//            for (SysUserRole u : userRoleList) {
//                Integer userid = u.getUserId().intValue();
//                title =  "拨付单待您审批";
//                content = "拨付单："
//                        + sheet.getSheetcode() + "待您审批。"
//                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=6&f=todo&type=bf&kid=" +  sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
//                //查询消息要发送给哪个人员
//                //发送给分管所长
//                message.setTouserid(userid);
//                message.setRelatedsheettype("拨付单");
//                message.setRelatedsheetid(sheet.getSheetid());
//                messageMapper.insertAudMessage(message);
//
//                //发送app推送
//                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//                for (AppClientinfo client : clientList) {
//                    String clientId = client.getClientid();
//                    String msgTitle = "拨付单待您审批";
//                    String msgBody = "拨付单：" + sheet.getSheetcode() + "待您审批。";
//                    // 暂时 关闭，调试中。
//                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//                }
//                //发送短信
//                String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode();
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
////            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
////            Juhe.sendSMS_Audited("13776614820", msg);
//
//        }
//        else if (sheet.getSheetstatus() == SheetStatus.SuoZhangShenPi.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//            //给相应人员发提醒消息
//            //分管所长审批通过，给经办人发信息
//            String title = "拨付单审批中";
//            String content = "您提交的拨付单："
//                    +  sheet.getSheetcode()  + "由分管所长审批通过。等待所长审批。"
//                    + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getSheetuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
//            messageMapper.insertAudMessage(message);
//
//            //给所长发信息
//            List<SysUserRole> userRoleList = userRoleMapper.selectUserRoleByRoleId(12L);
//            for (SysUserRole u : userRoleList) {
//                Integer userid = u.getUserId().intValue();
//                title =  "拨付单待您审批";
//                content = "拨付单："
//                        + sheet.getSheetcode() + "待您审批。"
//                        + "<a href=\"/Audit/SheetList_ToAudit.aspx?t=7&f=todo&type=bf&kid=" +  sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";   //t=5表示分管处审批
//                //查询消息要发送给哪个人员
//                //发送给 所长
//                message.setTouserid(userid);
//                message.setRelatedsheettype("拨付单");
//                message.setRelatedsheetid(sheet.getSheetid());
//                messageMapper.insertAudMessage(message);
//
//                //发送app推送
//                List<AppClientinfo>  clientList =  clientinfoMapper.selectAppClientinfoByUserid(userid);
//                for (AppClientinfo client : clientList) {
//                    String clientId = client.getClientid();
//                    String msgTitle = "拨付单待您审批";
//                    String msgBody = "拨付单：" + sheet.getSheetcode() + "待您审批。";
//                    // 暂时 关闭，调试中。
//                    PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);
//                }
//                //发送短信
//                String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode();
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
////            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
////            Juhe.sendSMS_Audited("13776614820", msg);
//
//        }
//        else if (sheet.getSheetstatus() == SheetStatus.ShenPiWanCheng.getCode()) {
//            audContractMapper.updateAudContractStatus(sheet);
//
//            //如果是完成状态，还要在明细记录里写入审核时间。表示这个付款明细已经执行了
//            AudBudgetpay pay = new AudBudgetpay();
//            pay.setSheetid(sheet.getSheetid());
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
//                        +  sheet.getSheetcode()  + "由分管所长审批通过。"
//                        + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//
//            }
//            else if (record.getAudittype().equals("7")) {
//                content = "您提交的拨付单："
//                        +  sheet.getSheetcode()  + "由所长审批通过。"
//                        + "<a href=\"/Audit/SheetList_tijiaoren.aspx?f=todo&kid=" + sheet.getSheetid().toString() + "\" target=\"_self\" >查看</a> ";
//
//            }
//
//            AudMessage message = new AudMessage();
//            message.setMessagetime(DateUtils.dateTimeNow());
//            message.setMessagetitle(title);
//            message.setMessagecontent(content);
//            //查询消息要发送给哪个人员
//            //发送给合同的经办人
//            message.setTouserid(sheet.getSheetuserid());
//            message.setRelatedsheettype("拨付单");
//            message.setRelatedsheetid(sheet.getSheetid());
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
//            String msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=通过" + "&#money#=" + sheet.getHejiBenci().toString() + "&#danwei#=" + danwei;
//            Juhe.sendSMS_Audited("13776614820", msg, url, key);
//
//            //发送短信，给经办人
//            msg = "#type#=拨付单&#name#=" + sheet.getSheetcode() + "&#result#=通过";
//            // 暂时 关闭，调试中。
//            Juhe.sendSMS_Audited_ToSheetUser("13776614820", msg, url, key);
//
////            CommonFunc.SendSMS_Audited(Session["CurrentUserID"].ToString(), 、、发送短信，给当前审核人
////                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=通过"
////                            + "&#money#=" + lab_heji_benci.Text + "&#danwei#=" + lab_SupNames.Text);
////
////            CommonFunc.SendSMS_Audited_ToSheetUser(am.ToUserID.ToString(),        //发送给经办人
////                    "#type#=拨付单&#name#=" + lab_SheetCode.Text + "&#result#=通过");
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
//        audContractauditrecordMapper.insertAudContractauditrecord(record);
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

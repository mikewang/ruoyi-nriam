package com.ruoyi.sheet.service;


import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.ConvertUpMoney;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.sheet.domain.*;
import com.ruoyi.sheet.mapper.*;
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
    private AudMessageMapper messageMapper;


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

        return result;
    }


    public List<AudSheet> selectSheetXiangmuByUserid(Long userId) {
        Integer uid = userId.intValue();

        List<AudSheet> sheetList = audSheetMapper.selectSheetXiangmuByUserid(uid);

        log.debug("request sheetList list is " + sheetList.toString());

        return sheetList;
    }

    @Transactional
    public Integer updateSheetAuditStatus(AudSheet sheet) {
        Integer result = 0;

        if (sheet.getSheetstatus() == SheetStatus.NoPass.getCode()) {

        }
        else if (sheet.getSheetstatus() == SheetStatus.BuMenShenPi.getCode()) {

        }


        return result;
    }

}

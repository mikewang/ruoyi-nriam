package com.ruoyi.sheet.mapper;

import com.ruoyi.sheet.domain.AudBudgetpay;
import com.ruoyi.sheet.domain.AudBudgetpayRecordQuery;

import java.util.HashMap;
import java.util.List;

public interface AudBudgetpayMapper {

    List<AudBudgetpay> selectBudgetPayOfSheetBySheetid(Integer sheetid);

    List<AudBudgetpay> selectBudgetPayRecord(AudBudgetpayRecordQuery query);

    int insertAudBudgetpay(AudBudgetpay record);

    int updateAudBudgetpayAduitTime(AudBudgetpay record);


    int deleteByPrimaryKey(Integer payid);

    AudBudgetpay selectByPrimaryKey(Integer payid);

    List<AudBudgetpay> selectAll();

    int updateByPrimaryKey(AudBudgetpay record);
}
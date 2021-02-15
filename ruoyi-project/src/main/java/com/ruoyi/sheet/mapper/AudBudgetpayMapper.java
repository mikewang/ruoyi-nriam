package com.ruoyi.sheet.mapper;

import com.ruoyi.sheet.domain.AudBudgetpay;
import com.ruoyi.sheet.domain.AudBudgetpayRecordQuery;

import java.util.HashMap;
import java.util.List;

public interface AudBudgetpayMapper {

    List<AudBudgetpay> selectBudgetPayOfSheetBySheetid(Integer sheetid);

    List<AudBudgetpay> selectBudgetPayRecord(AudBudgetpayRecordQuery query);

    int deleteByPrimaryKey(Integer payid);

    int insert(AudBudgetpay record);

    AudBudgetpay selectByPrimaryKey(Integer payid);

    List<AudBudgetpay> selectAll();

    int updateByPrimaryKey(AudBudgetpay record);
}
package com.ruoyi.sheet.service;


import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.sheet.domain.AudBudgetpay;
import com.ruoyi.sheet.domain.AudBudgetpayRecordQuery;
import com.ruoyi.sheet.domain.AudSheet;
import com.ruoyi.sheet.mapper.AudBudgetpayMapper;
import com.ruoyi.sheet.mapper.AudSheetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class AudSheetService {

    private static final Logger log = LoggerFactory.getLogger(AudApplyService.class);

    @Resource
    AudSheetMapper audSheetMapper;


    @Resource
    AudBudgetpayMapper audBudgetpayMapper;

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

        log.debug("request selectBudgetPayRecord  list is " + payList.toString());

        return payList;
    }
}

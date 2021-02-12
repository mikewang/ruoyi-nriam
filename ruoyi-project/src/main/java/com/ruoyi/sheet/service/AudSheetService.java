package com.ruoyi.sheet.service;


import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.sheet.domain.AudSheet;
import com.ruoyi.sheet.mapper.AudSheetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudSheetService {

    private static final Logger log = LoggerFactory.getLogger(AudApplyService.class);

    @Resource
    AudSheetMapper audSheetMapper;

    public List<AudSheet> selectSheetTijiaorenByUserid(Long userId) {
        Integer uid = userId.intValue();
        uid = 175;
        List<AudSheet> sheelList = audSheetMapper.selectSheetTijiaorenByUserid(uid);

        log.debug("request sheelList list is " + sheelList.toString());

        return sheelList;
    }

    public AudSheet selectSheetTijiaorenById(Integer sheetid) {

        return null;
    }
}

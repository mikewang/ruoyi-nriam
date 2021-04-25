package com.ruoyi.audit.service;


import com.ruoyi.audit.domain.AudSheetauditrecord;
import com.ruoyi.audit.mapper.AudSheetauditrecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudSheetauditrecordService {

    private static final Logger log = LoggerFactory.getLogger(AudSheetauditrecordService.class);

    @Resource
    AudSheetauditrecordMapper audSheetauditrecordMapper;

    public List<AudSheetauditrecord> selectMyauditrecord(Integer audituserid) {

        List<AudSheetauditrecord> myauditrecordList = audSheetauditrecordMapper.selectMyauditrecord(audituserid);

        log.debug("request myauditrecordList list is " + myauditrecordList.toString());

        return myauditrecordList;
    }

}

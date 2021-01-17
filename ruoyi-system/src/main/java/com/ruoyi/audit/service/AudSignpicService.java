package com.ruoyi.audit.service;

import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.mapper.AudSignpicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudSignpicService {
    private static final Logger log = LoggerFactory.getLogger(AudSignpicService.class);

    @Resource
    private AudSignpicMapper audSignpicMapper;

    public List<AudSignpic> selectSignpicList(AudSignpic signpic) {
        List<AudSignpic> signpicList = audSignpicMapper.selectSignpicList(signpic);

        for(AudSignpic s: signpicList){

//            String content = s.getMessagecontent();
//            log.debug("original content is " + content);
//            Integer index = content.indexOf("<a href");
//            content = content.substring(0,index);
//            log.debug("now      content is " + content);
//            s.setMessagecontent(content);
        }

        return signpicList;
    }


}
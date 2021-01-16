package com.ruoyi.audit.service;

import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudMessageService {
    private static final Logger log = LoggerFactory.getLogger(AudMessageService.class);

    @Resource
    private AudMessageMapper audMessageMapper;

    public List<AudMessage> selectByUserId(Integer userId) {

        List<AudMessage> messageList = audMessageMapper.selectByUserId(userId);
        log.debug("message list is request.");
        return messageList;
    }

    public List<AudMessage> selectAll() {

        return audMessageMapper.selectAll();
    }

}

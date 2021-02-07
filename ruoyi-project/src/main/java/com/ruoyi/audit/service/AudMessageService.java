package com.ruoyi.audit.service;

import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class AudMessageService {
    private static final Logger log = LoggerFactory.getLogger(AudMessageService.class);

    @Resource
    private AudMessageMapper audMessageMapper;

    public List<AudMessage> selectMessageByToUserId(Integer userId) {
        List<AudMessage> messageList = audMessageMapper.selectMessageByToUserId(userId);

        // messagecontent 需要做 <a href > 过滤处理，如何link也需要处理。原来ASP系统写入功能到内容中。

        for (AudMessage s : messageList) {

            String messagecontent = s.getMessagecontent();
            log.debug("original content is " + messagecontent);
            Integer index = messagecontent.indexOf("<a href");
            String content = messagecontent.substring(0, index);
            log.debug("now content is " + content);
            s.setContent(content);

            String link = messagecontent.substring(index + 9);
            index = link.indexOf(".aspx");
            link = link.substring(0, index);
            s.setLink(link);
        }

        log.debug("message list is request.");
        return messageList;
    }

    public Integer updateProcessedByIds(List<Integer> ids) {
        return audMessageMapper.updateProcessedByIds(ids);
    }

}
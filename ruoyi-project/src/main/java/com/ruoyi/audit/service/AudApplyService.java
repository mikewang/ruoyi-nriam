package com.ruoyi.audit.service;

import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AudApplyService {
    private static final Logger log = LoggerFactory.getLogger(AudApplyService.class);

    @Resource
    private AudMessageMapper audMessageMapper;

    @Resource
    private AudApplyMapper audApplyMapper;

    public List<AudMessage> selectByUserId(Long userId) {
        List<AudMessage> messageList = audMessageMapper.selectMessageByToUserId(userId.intValue());

        // messagecontent 需要做 <a href > 过滤处理，如何link也需要处理。原来ASP系统写入功能到内容中。

        for (AudMessage s : messageList) {

            String content = s.getMessagecontent();
            log.debug("original content is " + content);
            Integer index = content.indexOf("<a href");
            content = content.substring(0, index);
            log.debug("now      content is " + content);
            s.setMessagecontent(content);
        }

        log.debug("message list is request.");
        return messageList;
    }

    public AudApply selectApplyByTypeAndRelatedID(AudApply apply) {

      return    audApplyMapper.selectApplyByTypeAndRelatedID(apply);

    }

    public Integer updateProcessedByIds(List<Integer> ids) {

        return audMessageMapper.updateProcessedByIds(ids);
    }

    public Integer auditApply(AudApply apply) {

        return audApplyMapper.updateAudApply(apply);
    }

}
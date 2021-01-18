package com.ruoyi.audit.service;

import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.mapper.AudSignpicMapper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
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

        return signpicList;
    }

    public Integer mergeSignpic(AudSignpic signpic) {
        Integer result = audSignpicMapper.mergeSignpic(signpic);
        return result;
    }


    public Integer removeSignpicByUserIds(List<Long> ids) {

        return audSignpicMapper.removeSignpicByUserIds(ids);
    }

}
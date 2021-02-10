package com.ruoyi.achieve.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchPatentMapper;
import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudApplyAchieve;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AchToconfirmService {
    private static final Logger log = LoggerFactory.getLogger(AchToconfirmService.class);

    @Resource
    private AchPatentMapper patentMapper;

    @Resource
    private AchAuthorMapper authorMapper;

    @Resource
    private BasDocMapper basDocMapper;

    @Resource
    private AudApplyMapper applyMapper;

    @Resource
    private SysUserMenuMapper userMenuMapper;

    @Resource
    private AudMessageMapper messageMapper;


    public List<AudApplyAchieve> selectAchieveToConfirmList(HashMap map) {

        List<AudApplyAchieve> applyList = applyMapper.selectAchieveToConfirmList(map);

        log.debug("AchPatent list is request.");
        return applyList;
    }

}
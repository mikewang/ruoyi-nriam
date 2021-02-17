package com.ruoyi.api.service;

import com.ruoyi.achieve.domain.AchAuthor;
import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.mapper.AchAuthorMapper;
import com.ruoyi.achieve.mapper.AchPatentMapper;
import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.mapper.AudApplyMapper;
import com.ruoyi.audit.mapper.AudMessageMapper;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.AchieveType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.system.domain.SysUserMenu;
import com.ruoyi.system.mapper.SysUserMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppClientinfoService {
    private static final Logger log = LoggerFactory.getLogger(AppClientinfoService.class);

    @Resource
    private AppClientinfoMapper clientinfoMapper;

    public AppClientinfo selectAppClientinfoByUserid(Integer userid) {

        AppClientinfo p =  clientinfoMapper.selectAppClientinfoByUserid(userid);

        return  p;
    }

}
package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.service.AchPatentService;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.service.AudMessageService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/achieve/patent")
public class AchPatentController extends BaseController {
    @Resource
    private AchPatentService patentService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('achieve:patent:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchPatent query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query  is " + query.toString());

        startPage();
        List<AchPatent> list = patentService.selectAchPatentList(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('achieve:patent:list')")
    @GetMapping( "/unique")
    public AjaxResult getIfDuplicate(AchPatent patent)
    {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters getPatentcode is " + patent.getPatentcode());

        Integer patentid = patent.getPatentid();

        String patentcode = patent.getPatentcode();

        if (StringUtils.isNotNull(patentcode))
        {
            ajax.put(AjaxResult.DATA_TAG, patentService.queryIfDuplicate(patent));
        }
        return ajax;
    }


}
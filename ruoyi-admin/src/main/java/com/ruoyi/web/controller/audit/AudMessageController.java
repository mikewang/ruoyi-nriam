package com.ruoyi.web.controller.audit;

import com.ruoyi.audit.service.AudMessageService;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.service.AudMessageService;
import com.ruoyi.logis.domain.LogisContract;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/audit/message")
public class AudMessageController extends BaseController
{
    @Resource
    private AudMessageService audMessageService;


    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('audit:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudMessage audMessage)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();
        List<AudMessage> list = audMessageService.selectByUserId(87);

        return getDataTable(list);
    }


}

package com.ruoyi.web.controller.audit;

import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.service.AudMessageService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.logis.domain.LogisContract;
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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

        userId =  new Long(87);

        startPage();
        List<AudMessage> list = audMessageService.selectByUserId(userId);

        return getDataTable(list);
    }

    /**
     * 消息已读设置 更新操作
     */
    @PreAuthorize("@ss.hasPermi('audit:message:remove')")
    @Log(title = "待办事项消息已读", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageids}")
    public AjaxResult remove(@PathVariable List<Integer> messageids)
    {
        logger.debug("read message are " + messageids);

        return toAjax(audMessageService.updateProcessedByIds(messageids));
    }

}
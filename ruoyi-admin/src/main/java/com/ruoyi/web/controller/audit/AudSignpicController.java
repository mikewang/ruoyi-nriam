package com.ruoyi.web.controller.audit;

import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.service.AudMessageService;
import com.ruoyi.audit.service.AudSignpicService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/signpic")
public class AudSignpicController extends BaseController
{
    @Resource
    private AudSignpicService audSignpicService;


    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('system:signpic:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudSignpic audSignpic)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();
        List<AudSignpic> list = audSignpicService.selectSignpicList(audSignpic);

        return getDataTable(list);
    }

    /**
     * 消息已读设置 更新操作
     */
//    @PreAuthorize("@ss.hasPermi('system:signpic:remove')")
//    @Log(title = "待办事项消息已读", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{messageids}")
//    public AjaxResult remove(@PathVariable List<Integer> messageids)
//    {
//        logger.debug("read message are " + messageids);
//
//        return toAjax(audMessageService.updateProcessedByIds(messageids));
//    }

}
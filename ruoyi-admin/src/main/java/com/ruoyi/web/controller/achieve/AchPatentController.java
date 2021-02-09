package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.service.AchPatentService;
import com.ruoyi.audit.domain.AudMessage;
import com.ruoyi.audit.service.AudMessageService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:patent:list')")
    @GetMapping(value = { "/{patentid}" })
    public AjaxResult getPatent(@PathVariable(value = "patentid", required = false) Integer patentid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(patentid))
        {
            ajax.put(AjaxResult.DATA_TAG, patentService.selectAchPatentById(patentid));
        }
        return ajax;
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


    @PreAuthorize("@ss.hasPermi('achieve:patent:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchPatent patent) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        patent.setCreateuserid(userId.intValue());

        Integer status = patent.getStatus();
        logger.debug("AchPatent .getStatus is " + status.toString());

        if (StringUtils.isNotEmpty(patent.getPatentcode())
                && UserConstants.NOT_UNIQUE.equals(patentService.queryIfDuplicate(patent)))
        {
            return AjaxResult.error("操作'" + patent.getPatentname() + "'失败，专利号已存在");
        }

        patent.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = patentService.insertAchPatent(patent);

        if (rows > 0 ) {
            Integer patentid = patent.getPatentid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, patentid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:patent:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchPatent patent) {

        AchPatent undoPatent = patentService.selectAchPatentById(patent.getPatentid());

        if (StringUtils.isNotEmpty(patent.getPatentcode()) &&undoPatent.getPatentcode() != patent.getPatentcode()) {

            if (UserConstants.NOT_UNIQUE.equals(patentService.queryIfDuplicate(patent)))
            {
                return AjaxResult.error("操作'" + patent.getPatentname() + "'失败，专利号已存在");
            }
        }
        Integer status = patent.getStatus();
        logger.debug("patent.getStatus is " + status.toString());

        Integer res = patentService.updateAchPatent(patent);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }
}
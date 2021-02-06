package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.PmProjectacceptance;
import com.ruoyi.project.service.PmProjectacceptanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/project/acceptance")
public class PmProjectacceptanceController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private PmProjectacceptanceService projectacceptanceService;


    /**
     * 根据编号获取验收信息
     */
    @PreAuthorize("@ss.hasPermi('project:project:query')")
    @GetMapping(value = { "/{projectId}" })
    public AjaxResult getProjectacceptance(@PathVariable(value = "projectId", required = false) Integer projectId)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(projectId))
        {
            ajax.put(AjaxResult.DATA_TAG, projectacceptanceService.selectProjectacceptanceById(projectId));
        }
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('project:project:edit')")
    @Log(title = "项目验收申请", businessType = BusinessType.UPDATE)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmProjectacceptance project) {
        logger.debug("PmProjectacceptance add is " + project.toString());
        AjaxResult ajax = AjaxResult.success();

        Integer rows = projectacceptanceService.mergeProjectacceptance(project);

        if (rows > 0 ) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('project:project:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  PmProjectacceptance project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;

    }


    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:project:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectids}")
    public AjaxResult remove(@PathVariable Integer[] projectids) {

        return AjaxResult.success("");
    }



}
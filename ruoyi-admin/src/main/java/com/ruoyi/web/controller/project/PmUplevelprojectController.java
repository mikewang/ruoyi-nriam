package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.PmUplevelproject;
import com.ruoyi.project.service.PmUplevelprojectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/project/uplevel")
public class PmUplevelprojectController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private PmUplevelprojectService uplevelprojectService;


    @PreAuthorize("@ss.hasPermi('project:zaiyan:list')")
    @GetMapping("/list")
    public AjaxResult list(PmUplevelproject query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        Integer projectid = query.getProjectid();

        if (projectid != null) {
            List<PmUplevelproject> list = uplevelprojectService.selectUplevelprojectList(query);
            ajax.put(AjaxResult.DATA_TAG, list);
        }

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:query')")
    @GetMapping(value = { "/", "/{projectId}" })
    public AjaxResult getUplevelProject(@PathVariable(value = "projectId", required = true) Integer projectId)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(projectId))
        {
            ajax.put(AjaxResult.DATA_TAG, uplevelprojectService.selectUplevelProjectByProjectid(projectId));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmUplevelproject project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;


    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  PmUplevelproject project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;

    }


    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:zaiyan:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectids}")
    public AjaxResult remove(@PathVariable Integer[] projectids) {

        return AjaxResult.success("");
    }



}
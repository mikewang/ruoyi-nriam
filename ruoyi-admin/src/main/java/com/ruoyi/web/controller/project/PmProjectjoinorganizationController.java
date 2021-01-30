package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.PmProjectjoinorganization;
import com.ruoyi.project.service.PmProjectjoinorganizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/project/joinorg")
public class PmProjectjoinorganizationController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private PmProjectjoinorganizationService projectjoinorganizationService;


    @PreAuthorize("@ss.hasPermi('project:zaiyan:list')")
    @GetMapping("/list")
    public AjaxResult list(PmProjectjoinorganization joinorg) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        String projectid = joinorg.getProjectid();

        if (StringUtils.isNotNull(projectid)) {
            List<PmProjectjoinorganization> list = projectjoinorganizationService.selectProjectjoinorganizationList(joinorg);
            ajax.put(AjaxResult.DATA_TAG, list);
        }

        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('project:zaiyan:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmProjectjoinorganization project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;

//        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//        Long userId = loginUser.getUser().getUserId();
//
//
//        Integer projectid = projectjoinorganizationService.insertProject(project);
//
//        if (projectid > 0 ) {
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put(AjaxResult.DATA_TAG, projectid);
//            return  ajax;
//        }
//        else {
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  PmProjectjoinorganization project) {


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
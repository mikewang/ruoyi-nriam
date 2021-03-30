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
import com.ruoyi.project.domain.PmProjectmember;
import com.ruoyi.project.service.PMProjectMemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/project/member")
public class PmProjectMemberController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private PMProjectMemberService projectMemberService;


    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/list")
    public AjaxResult list(PmProjectmember query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        Integer projectid = query.getProjectid();

        if (projectid != null) {
            List<PmProjectmember> list = projectMemberService.selectProjectmemberByProjectid(projectid);
            logger.debug("selectProjectmemberByProjectid " + projectid.toString() + " " + list.toString());
            ajax.put(AjaxResult.DATA_TAG, list);
        }

        return ajax;
    }



    @PreAuthorize("@ss.hasPermi('project:project:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmProjectmember project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;


    }

    @PreAuthorize("@ss.hasPermi('project:project:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  PmProjectmember project) {

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
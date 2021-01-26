package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.service.AudProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/project/zaiyan")
public class AudProjectController extends BaseController {
    @Resource
    private AudProjectService projectService;

    @Resource
    private TokenService tokenService;

    @PreAuthorize("@ss.hasPermi('project:zaiyan:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudProject project) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectAudProjectZaiYanList(project);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AudProject project) {

        return AjaxResult.success("");
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AudProject project) {
        return AjaxResult.success("");
    }


    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:zaiyan:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectids}")
    public AjaxResult remove(@PathVariable Integer[] projectids) {
//         logger.debug 只有一个参数，后面都不显示，但不报错。

        return AjaxResult.success("");
    }

}
package com.ruoyi.web.controller.project;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.service.PmTeamService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/project/team")
public class PmTeamController extends BaseController {
    @Resource
    private PmTeamService pmTeamService;

    @Resource
    private TokenService tokenService;

    @PreAuthorize("@ss.hasPermi('project:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(PmTeam team) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();
        List<PmTeam> list = pmTeamService.selectTeamList(team);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('project:team:add')")
    @Log(title = "团队新增", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmTeam team) {

        if (StringUtils.isNotEmpty(team.getTeamname())) {
            return AjaxResult.error("新增团队'" + team.getCreateUserRealName() + "'失败，团队名称为空");
        }
        return  toAjax(pmTeamService.addTeam(team));
    }

    @PreAuthorize("@ss.hasPermi('project:team:edit')")
    @Log(title = "团队修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PmTeam team)
    {
        logger.debug(team.toString());

        if (StringUtils.isNotEmpty(team.getTeamname()) == false) {
            return AjaxResult.error("修改团队'" + team.getTeamid() + "'失败，团队名称为空");
        }

        return  toAjax(pmTeamService.updateTeam(team));
    }

}
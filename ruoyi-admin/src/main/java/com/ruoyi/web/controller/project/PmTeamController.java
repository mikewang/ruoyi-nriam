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
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.domain.PmTeamMember;
import com.ruoyi.project.service.PmTeamMemberService;
import com.ruoyi.project.service.PmTeamService;
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
    private PmTeamMemberService pmTeamMemberService;
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

    @PreAuthorize("@ss.hasPermi('project:team:list')")
    @GetMapping("/member/list")
    public TableDataInfo listMember(PmTeamMember member) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query member is " + member.toString());

        startPage();
        List<PmTeamMember> list = pmTeamMemberService.selectTeamMemberList(member);

        logger.debug("list is " + list.toString());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('project:team:list')")
    @Log(title = "团队管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmTeam team) {

        if (StringUtils.isNotEmpty(team.getTeamname())) {
            return AjaxResult.error("新增团队'" + team.getCreateUserRealName() + "'失败，团队名称为空");
        }
        return toAjax(pmTeamService.addTeam(team));
    }

    @PreAuthorize("@ss.hasPermi('project:team:list')")
    @Log(title = "团队管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PmTeam team) {
        logger.debug(team.toString());

        if (StringUtils.isNotEmpty(team.getTeamname()) == false) {
            return AjaxResult.error("修改团队'" + team.getTeamid() + "'失败，团队名称为空");
        }

        return toAjax(pmTeamService.updateTeam(team));
    }


    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:team:list')")
    @Log(title = "团队管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{teamids}")
    public AjaxResult remove(@PathVariable Integer[] teamids) {
//         logger.debug 只有一个参数，后面都不显示，但不报错。
        for (Integer teamid : teamids)
        {
            logger.debug("remove is " + String.valueOf(teamid));
        }

        logger.debug("teamids length is " + String.valueOf(teamids.length));

        return toAjax(pmTeamService.deleteTeamByIds(teamids));
    }

    @PreAuthorize("@ss.hasPermi('project:team:list')")
    @GetMapping("/joinTeamlist/{userid}")
    public AjaxResult joinUserTeamList(@PathVariable Integer userid) {
        AjaxResult ajax = AjaxResult.success();

        List<PmTeam> list = pmTeamService.selectTeamListOfAUserJoin(userid);

        if (list.size() > 0) {
            ajax.put(AjaxResult.DATA_TAG, list);

            return ajax;
        } else {

            return AjaxResult.error("失败，您不属于任何团队，请联系管理员");
        }

    }

}
package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.ConvertUpMoney;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.fourtech.domain.AudFourtech;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.PerIndicator;
import com.ruoyi.performance.domain.PerIndicatorfund;
import com.ruoyi.performance.domain.PerIndicatorproject;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.service.IndicatorProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorProjectController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorProjectService projectService;


    //IndicatorProjectList.aspx
    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorproject/list")
    public TableDataInfo indicatorprojectList(PerIndicatorproject project) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        logger.debug("query parameters is " + project.getProjecttypelinktext());

        startPage();

        List<PerIndicatorproject> list = projectService.selectIndicatorproject(project);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorRelation/list")
    public AjaxResult indicatorRelationList(PerRelation relation) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + relation.toString());
        AjaxResult ajax = AjaxResult.success();
        List<PerRelation> list = projectService.selectPerRelation(relation);
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价关联的考核指标修改", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorRelation")
    public AjaxResult updateIndicatorRelation(@Validated @RequestBody  PerRelation relation){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + relation.toString());
        AjaxResult ajax = AjaxResult.success();
        Integer result = projectService.updateIndicatorRelation(relation);

        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicator/list")
    public AjaxResult indicatorList(PerIndicator indicator) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + indicator.toString());
        AjaxResult ajax = AjaxResult.success();
        List<PerIndicator> list = projectService.selectPerIndicator(indicator);
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }



    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价项目管理", businessType = BusinessType.INSERT)
    @PostMapping("/indicatorproject")
    public AjaxResult add(@Validated @RequestBody PerIndicatorproject project) {

        AjaxResult ajax = AjaxResult.success();

        Integer userid = getCurrentLoginUserid();

        Integer result = projectService.addIndicatorproject(project);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, project.getIndicatorprojectid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价项目管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorproject")
    public AjaxResult update(@Validated @RequestBody  PerIndicatorproject project) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorproject update is " + project.toString());

        Integer result = projectService.updateIndicatorproject(project);

        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/indicatorproject/{projectids}")
    public AjaxResult delete(@PathVariable Integer[] projectids) {
        AjaxResult ajax = AjaxResult.success();

        Integer result = 0;
        for (Integer projectid : projectids ) {
             result = projectService.updateIndicatorprojectIfDeletedById(projectid);
        }


        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

}
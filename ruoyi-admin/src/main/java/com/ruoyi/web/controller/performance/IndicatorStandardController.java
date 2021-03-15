package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.PerIndicatorstandard;
import com.ruoyi.performance.service.IndicatorStandardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorStandardController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorStandardService standardService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorStandard/list")
    public TableDataInfo indicatorStandardList(PerIndicatorstandard standard) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();

        List<PerIndicatorstandard> list = standardService.selectPerIndicatorstandard(standard);

        return getDataTable(list);
    }



    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价标准管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorStandard")
    public AjaxResult update(@Validated @RequestBody  PerIndicatorstandard standard) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorstandard update is " + standard.toString());

        Integer result = standardService.updatePerIndicatorstandard(standard);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


}
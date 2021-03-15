package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.PerIndicatorsoftware;
import com.ruoyi.performance.service.IndicatorSoftwareService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/performance")
public class IndicatorSoftwareController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorSoftwareService fundService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价软件著作权管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorSoftware")
    public AjaxResult update(@Validated @RequestBody PerIndicatorsoftware fund) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorSoftware update is " + fund.toString());

        Integer result = fundService.updatePerIndicatorsoftware(fund);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorSoftware/{softwareid}")
    public AjaxResult getIndicatorSoftware(@PathVariable Integer softwareid) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + softwareid.toString());
        AjaxResult ajax = AjaxResult.success();
        PerIndicatorsoftware fund = fundService.selectPerIndicatorsoftwareById(softwareid);
        ajax.put(AjaxResult.DATA_TAG, fund);
        return ajax;
    }

}
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
import com.ruoyi.performance.domain.PerIndicator;
import com.ruoyi.performance.domain.PerIndicatorfund;
import com.ruoyi.performance.domain.PerIndicatorproject;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.service.IndicatorFundService;
import com.ruoyi.performance.service.IndicatorProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorFundController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorFundService fundService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价经费管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorfund")
    public AjaxResult update(@Validated @RequestBody  PerIndicatorfund fund) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorfund update is " + fund.toString());

        Integer result = fundService.updatePerIndicatorfund(fund);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorfund/{fundid}")
    public AjaxResult getIndicatorfund(@PathVariable Integer fundid) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + fundid.toString());
        AjaxResult ajax = AjaxResult.success();
        PerIndicatorfund fund = fundService.selectPerIndicatorfundById(fundid);
        ajax.put(AjaxResult.DATA_TAG, fund);
        return ajax;
    }

}
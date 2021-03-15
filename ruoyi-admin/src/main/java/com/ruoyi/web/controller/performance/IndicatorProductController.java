package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.PerIndicatorproduct;
import com.ruoyi.performance.service.IndicatorProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/performance")
public class IndicatorProductController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorProductService fundService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价农机新产品管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorProduct")
    public AjaxResult update(@Validated @RequestBody PerIndicatorproduct fund) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorProduct update is " + fund.toString());

        Integer result = fundService.updatePerIndicatorproduct(fund);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorProduct/{productid}")
    public AjaxResult getIndicatorProduct(@PathVariable Integer productid) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + productid.toString());
        AjaxResult ajax = AjaxResult.success();
        PerIndicatorproduct fund = fundService.selectPerIndicatorproductById(productid);
        ajax.put(AjaxResult.DATA_TAG, fund);
        return ajax;
    }

}
package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.PerIndicatorappraisal;
import com.ruoyi.performance.service.IndicatorAppraisalService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/performance")
public class IndicatorAppraisalController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorAppraisalService fundService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价鉴定成果管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorAppraisal")
    public AjaxResult update(@Validated @RequestBody PerIndicatorappraisal fund) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorAppraisal update is " + fund.toString());

        Integer result = fundService.updatePerIndicatorappraisal(fund);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorAppraisal/{appraisalid}")
    public AjaxResult getIndicatorAppraisal(@PathVariable Integer appraisalid) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + appraisalid.toString());
        AjaxResult ajax = AjaxResult.success();
        PerIndicatorappraisal fund = fundService.selectPerIndicatorappraisalById(appraisalid);
        ajax.put(AjaxResult.DATA_TAG, fund);
        return ajax;
    }

}
package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.PerIndicatortech;
import com.ruoyi.performance.service.IndicatorTechService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/performance")
public class IndicatorTechController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorTechService fundService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价农业部主推技术管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorTech")
    public AjaxResult update(@Validated @RequestBody PerIndicatortech fund) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorTech update is " + fund.toString());

        Integer result = fundService.updatePerIndicatortech(fund);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorTech/{techid}")
    public AjaxResult getIndicatorTech(@PathVariable Integer techid) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query parameters is " + techid.toString());
        AjaxResult ajax = AjaxResult.success();
        PerIndicatortech fund = fundService.selectPerIndicatortechById(techid);
        ajax.put(AjaxResult.DATA_TAG, fund);
        return ajax;
    }

}
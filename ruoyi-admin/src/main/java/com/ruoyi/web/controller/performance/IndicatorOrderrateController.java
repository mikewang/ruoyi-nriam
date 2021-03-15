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
import com.ruoyi.performance.domain.PerIndicatororderrate;
import com.ruoyi.performance.service.IndicatorOrderrateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorOrderrateController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorOrderrateService standardService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorOrderrate/list")
    public TableDataInfo indicatorOrderrateList(PerIndicatororderrate standard) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();

        List<PerIndicatororderrate> list = standardService.selectPerIndicatororderrate(standard);

        return getDataTable(list);
    }



    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价标准管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorOrderrate")
    public AjaxResult update(@Validated @RequestBody  PerIndicatororderrate standard) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatororderrate update is " + standard.toString());

        Integer result = standardService.updatePerIndicatororderrate(standard);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


}
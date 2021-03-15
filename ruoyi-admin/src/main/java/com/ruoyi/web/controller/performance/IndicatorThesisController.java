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
import com.ruoyi.performance.domain.PerIndicatorpatent;
import com.ruoyi.performance.domain.PerIndicatorthesis;
import com.ruoyi.performance.service.IndicatorThesisService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorThesisController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorThesisService thesisService;


    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorThesis/list")
    public TableDataInfo indicatorThesisList(PerIndicatorthesis thesis) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();

        List<PerIndicatorthesis> list = thesisService.selectPerIndicatorthesis(thesis);

        return getDataTable(list);
    }



    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价论文管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorThesis")
    public AjaxResult update(@Validated @RequestBody  PerIndicatorthesis thesis) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorthesis update is " + thesis.toString());

        Integer result = thesisService.updatePerIndicatorthesis(thesis);

        if (result > 0) {
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


}
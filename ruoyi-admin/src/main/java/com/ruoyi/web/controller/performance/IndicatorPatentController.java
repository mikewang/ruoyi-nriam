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
import com.ruoyi.performance.domain.PerIndicatorprize;
import com.ruoyi.performance.service.IndicatorPatentService;
import com.ruoyi.performance.service.IndicatorPrizeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorPatentController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorPatentService patentService;


    //IndicatorProjectList.aspx
    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorPatent/list")
    public TableDataInfo indicatorPatentList(PerIndicatorpatent patent) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();

        List<PerIndicatorpatent> list = patentService.selectPerIndicatorpatent(patent);

        return getDataTable(list);
    }

    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价专利管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorPatent")
    public AjaxResult update(@Validated @RequestBody  PerIndicatorpatent patent) {
        AjaxResult ajax = AjaxResult.success();

        Integer result = patentService.updatePerIndicatorpatent(patent);

        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }



}
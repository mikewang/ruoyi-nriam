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
import com.ruoyi.performance.domain.PerIndicatorprize;
import com.ruoyi.performance.domain.PerIndicatorproject;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.service.IndicatorPrizeService;
import com.ruoyi.performance.service.IndicatorProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class IndicatorPrizeController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private IndicatorPrizeService prizeService;


    //IndicatorProjectList.aspx
    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @GetMapping("/indicatorPrize/list")
    public TableDataInfo indicatorPrizeList(PerIndicatorprize prize) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        logger.debug("query parameters is " + prize.getPrizetypelinktext() + "  "+ prize.getPrizetypelinktext());

        startPage();

        List<PerIndicatorprize> list = prizeService.selectPerIndicatorprize(prize);

        return getDataTable(list);
    }

    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价获奖管理", businessType = BusinessType.INSERT)
    @PostMapping("/indicatorPrize")
    public AjaxResult add(@Validated @RequestBody PerIndicatorprize prize) {

        AjaxResult ajax = AjaxResult.success();

        Integer userid = getCurrentLoginUserid();

        Integer result = prizeService.addPerIndicatorprize(prize);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, prize.getIndicatorprizeid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价获奖管理", businessType = BusinessType.UPDATE)
    @PutMapping("/indicatorPrize")
    public AjaxResult update(@Validated @RequestBody  PerIndicatorprize prize) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("PerIndicatorprize update is " + prize.toString());

        Integer result = prizeService.updatePerIndicatorprize(prize);

        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:indicator:list')")
    @Log(title = "绩效评价获奖管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/indicatorPrize/{prizeids}")
    public AjaxResult delete(@PathVariable Integer[] prizeids) {
        AjaxResult ajax = AjaxResult.success();

        Integer result = 0;
        for (Integer prizeid : prizeids ) {
            PerIndicatorprize prize = prizeService.selectPerIndicatorprizeById(prizeid);
            result = prizeService.deletePerIndicatorprize(prize);
        }

        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

}
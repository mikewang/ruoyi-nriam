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
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.service.TeamPerformanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class TeamPerformanceController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private TeamPerformanceService teamPerformanceService;


    //IndicatorProjectList.aspx
    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/verifyteam/list")
    public AjaxResult verifyTeamList(PerTeamperformance prize) {
        AjaxResult ajax = AjaxResult.success();

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        logger.debug("query parameters is " + prize.getTeamidlinktext() + "  "+ prize.getPerformanceyear());


        List<PerTeamperformance> list = teamPerformanceService.selectPerTeamperformance(prize);

        ajax.put(AjaxResult.DATA_TAG,list);

        return ajax;

    }

    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }
//
//    @PreAuthorize("@ss.hasPermi('performance:teamPerformance:list')")
//    @Log(title = "绩效评价 团队考核管理", businessType = BusinessType.INSERT)
//    @PostMapping("/teamPerformance")
//    public AjaxResult add(@Validated @RequestBody PerTeamperformance prize) {
//
//        AjaxResult ajax = AjaxResult.success();
//
//        Integer userid = getCurrentLoginUserid();
//
//        Integer result = teamPerformanceService.addPerTeamperformance(prize);
//
//        if (result > 0) {
//
//            ajax.put(AjaxResult.DATA_TAG, prize.getIndicatorprizeid());
//
//            return ajax;
//        } else {
//
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
//
//    }
//
//    @PreAuthorize("@ss.hasPermi('performance:teamPerformance:list')")
//    @Log(title = "绩效评价获奖管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/teamPerformance")
//    public AjaxResult update(@Validated @RequestBody  PerTeamperformance prize) {
//        AjaxResult ajax = AjaxResult.success();
//        logger.debug("PerTeamperformance update is " + prize.toString());
//
//        Integer result = teamPerformanceService.updatePerTeamperformance(prize);
//
//        if (result > 0) {
//            return ajax;
//        } else {
//
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
//
//    }
//
//    @PreAuthorize("@ss.hasPermi('performance:teamPerformance:list')")
//    @Log(title = "绩效评价获奖管理", businessType = BusinessType.DELETE)
//    @DeleteMapping("/teamPerformance/{prizeids}")
//    public AjaxResult delete(@PathVariable Integer[] prizeids) {
//        AjaxResult ajax = AjaxResult.success();
//
//        Integer result = 0;
//        for (Integer prizeid : prizeids ) {
//            PerTeamperformance prize = teamPerformanceService.selectPerTeamperformanceById(prizeid);
//            result = teamPerformanceService.deletePerTeamperformance(prize);
//        }
//
//        if (result > 0) {
//            return ajax;
//        } else {
//
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
//
//    }

}
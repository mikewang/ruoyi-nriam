package com.ruoyi.web.controller.performance;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.service.TeamPerformanceService;
import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.service.PmTeamService;
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private PmTeamService pmTeamService;


    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/verifyteam/list")
    public TableDataInfo verifyTeamList(PerTeamperformance teamperformance) {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters is " + teamperformance.getTeamidlinktext() + "  "+ teamperformance.getPerformanceyear());

        startPage();

        List<PerTeamperformance> list = teamPerformanceService.selectPerTeamperformance(teamperformance);

        return getDataTable(list);
    }

    @Log(title = "绩效评价 团队考核管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/verifyteam/export")
    public AjaxResult exportVerifyTeam(PerTeamperformance teamperformance)
    {
        List<PerTeamperformance> list = teamPerformanceService.selectPerTeamperformance(teamperformance);
        ExcelUtil<PerTeamperformance> util = new ExcelUtil<PerTeamperformance>(PerTeamperformance.class);
        return util.exportExcel(list, "团队绩效考核表");
    }


    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/verifyteam/pointssum")
    public AjaxResult verifyTeamPointSum(PerTeamperformance teamperformance) {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters is " + teamperformance.getTeamidlinktext() + "  "+ teamperformance.getPerformanceyear());

        Integer points = teamPerformanceService.selectPerTeamperformancePointsSum(teamperformance);

        ajax.put(AjaxResult.DATA_TAG,points);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/verifyteam/confirmrequest")
    public AjaxResult getConfirmrequest(PerTeamperformance teamperformance) {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters is " + teamperformance.getTeamidlinktext() + "  "+ teamperformance.getPerformanceyear());

        PerConfirmqequest quest = new PerConfirmqequest();
        quest.setTeamid(teamperformance.getTeamid());
        quest.setYear(teamperformance.getPerformanceyear());
        PerConfirmqequest record = teamPerformanceService.selectPerConfirmRequestByTeamYear(quest);

        ajax.put(AjaxResult.DATA_TAG,record);

        return ajax;
    }

    @Log(title = "绩效评价 团队考核管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @PostMapping("/verifyteam/confirmrequest")
    public AjaxResult addConfirmrequest(@Validated @RequestBody PerTeamperformance teamperformance) {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters is " + teamperformance.getTeamidlinktext() + "  "+ teamperformance.getPerformanceyear());

        PerConfirmqequest quest = new PerConfirmqequest();
        quest.setTeamid(teamperformance.getTeamid());
        quest.setYear(teamperformance.getPerformanceyear());
        quest.setStatus("待确认");

        Integer result = teamPerformanceService.insertPerConfirmRequest(quest);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, quest.getRequestid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 团队考核管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @PutMapping("/verifyteam/verifypoints")
    public AjaxResult updateVerifyPoints(@Validated @RequestBody PerScoreschangelog scoreschangelog) {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("scoreschangelog update is " + scoreschangelog.toString());

        Integer userId = getCurrentLoginUserid();

        PerTeamperformance teamperformance = new  PerTeamperformance();
        teamperformance.setPerformanceid(scoreschangelog.getPerformanceid());
        teamperformance.setPoints(scoreschangelog.getNewscores());
        //修改分数
        ////记录日志
        scoreschangelog.setUserid(userId);
        scoreschangelog.setTime(DateUtils.dateTimeNow());
        //"分数由" + lab_OldScores.Text + "调整为" + txt_NewScores.Text;
        String  description = "分数由" + scoreschangelog.getOldscores() + "调整为" + scoreschangelog.getNewscores();
        scoreschangelog.setDescription(description);

        Integer result = teamPerformanceService.updatePerTeamperformancePoint(teamperformance,scoreschangelog);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, result);

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/verifyteam/verifypoints")
    public AjaxResult getVerifypoint( PerScoreschangelog scoreschangelog) {
        AjaxResult ajax = AjaxResult.success();

        List<PerScoreschangelog> list = teamPerformanceService.selectPerScoreschangelog(scoreschangelog);

        ajax.put(AjaxResult.DATA_TAG,list);

        return ajax;
    }

    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }

    @Log(title = "绩效评价 团队考核管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @DeleteMapping("/verifyteam/{performanceid}")
    public AjaxResult deleteTeamperformance(@PathVariable Integer performanceid) {
        AjaxResult ajax = AjaxResult.success();

        PerTeamperformance teamperformance = new  PerTeamperformance();
        teamperformance.setPerformanceid(performanceid);

        Integer result = teamPerformanceService.updatePerTeamperformanceDeletedById(teamperformance);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, result);

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }


    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @GetMapping("/viewteam/list")
    public TableDataInfo viewTeamList(PerTeamperformance teamperformance) {

        Integer userid = this.getCurrentLoginUserid();

        Boolean validTeamid = false;

        List<PmTeam> teamList = pmTeamService.selectTeamListOfAUserJoin(userid);

        if (teamList.size() > 0) {

            for (PmTeam team : teamList) {

                if (team.getTeamid() == teamperformance.getTeamid()) {

                    validTeamid = true;
                    break;
                }
            }

        }

        logger.debug("query parameters is " + teamperformance.getTeamidlinktext() + "  "+ teamperformance.getPerformanceyear());

        if (validTeamid) {

            startPage();

            List<PerTeamperformance> list = teamPerformanceService.selectPerTeamperformance(teamperformance);

            return getDataTable(list);

        }
        else {
            return getDataTable(new ArrayList<>());
        }

    }

    @Log(title = "绩效评价 团队考核管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('performance:viewteam:list')")
    @PutMapping("/viewteam/confirmrequest")
    public AjaxResult updateConfirmrequest(@Validated @RequestBody PerTeamperformance teamperformance) {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters is " + teamperformance.getTeamidlinktext() + "  "+ teamperformance.getPerformanceyear());

        teamperformance.setStatus("已确认");

        Integer result = teamPerformanceService.updatePerTeamperformanceStatus(teamperformance);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, result);

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:indicatortable:list')")
    @GetMapping("/indicatortable")
    public AjaxResult getIndictorTable() {
        AjaxResult ajax = AjaxResult.success();

        List<TeamPerformanceService.IndicatorTree> list = teamPerformanceService.selectPerIndictorTree();

        logger.debug("getIndictorTable is " + list.toString());
        if (list.size() > 0) {

            ajax.put(AjaxResult.DATA_TAG, list);

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 考核指标管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('performance:indicatortable:list')")
    @PostMapping("/indicatortable")
    public AjaxResult addIndictorTable(@Validated @RequestBody PerIndicator record) {
        AjaxResult ajax = AjaxResult.success();

        record.setIfdisplay(true);
        record.setIfdeleted(false);
        record.setOrdernumber(1);
        Integer result = teamPerformanceService.insertPerIndicator(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getIndicatorid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 考核指标管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('performance:indicatortable:list')")
    @PutMapping("/indicatortable")
    public AjaxResult updateIndictorTable(@Validated @RequestBody PerIndicator record) {
        AjaxResult ajax = AjaxResult.success();

        Integer result = teamPerformanceService.updatePerIndicator(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getIndicatorid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 考核指标管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('performance:verifyteam:list')")
    @DeleteMapping("/indicatortable/{indicatorid}")
    public AjaxResult deleteIndictorTable(@PathVariable Integer indicatorid) {
        AjaxResult ajax = AjaxResult.success();

        PerIndicator record = new PerIndicator();
        record.setIndicatorid(indicatorid);
        Integer result = teamPerformanceService.updatePerIndicatorDeletedById(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getIndicatorid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('performance:addscoreapply:list')")
    @GetMapping("/addscoreapply/list")
    public TableDataInfo getAddscoreapplyList(PerAddscoreapply record) {

        Integer userid = this.getCurrentLoginUserid();

        Boolean validTeamid = false;

        List<PmTeam> teamList = pmTeamService.selectTeamListOfAUserJoin(userid);

        if (teamList.size() > 0) {

            for (PmTeam team : teamList) {

                if (team.getTeamid() == record.getTeamid()) {

                    validTeamid = true;
                    break;
                }
            }

        }

        logger.debug("query parameters is " + record.getTeamidlinktext() + "  "+ record.getYear());

        if (validTeamid) {

            startPage();

            List<PerAddscoreapply> list = teamPerformanceService.selectAddscoreapply(record);

            return getDataTable(list);

        }
        else {
            return getDataTable(new ArrayList<>());
        }

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
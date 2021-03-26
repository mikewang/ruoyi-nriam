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
import com.ruoyi.performance.service.AddscoreapplyService;
import com.ruoyi.performance.service.FundreportService;
import com.ruoyi.performance.service.IncomereportService;
import com.ruoyi.performance.service.TeamPerformanceService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.PmTeam;
import com.ruoyi.project.service.PmTeamService;
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private AddscoreapplyService addscoreapplyService;

    @Resource
    private FundreportService fundreportService;

    @Resource
    private IncomereportService incomereportService;


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

            List<PerAddscoreapply> list = addscoreapplyService.selectAddscoreapply(record);

            return getDataTable(list);

        }
        else {
            return getDataTable(new ArrayList<>());
        }

    }

    @Log(title = "绩效评价 加分申请", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('performance:addscoreapply:list')")
    @PostMapping("/addscoreapply")
    public AjaxResult addAddscoreapply(@Validated @RequestBody PerAddscoreapply record) {
        AjaxResult ajax = AjaxResult.success();

        record.setApplyuserid(getCurrentLoginUserid());
        record.setApplytime(DateUtils.dateTimeNow());
        Integer result = addscoreapplyService.insertAddscoreapply(record);

        if (result > 0) {
            ajax.put(AjaxResult.DATA_TAG, record.getApplyid());
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 加分申请", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('performance:addscoreapply:list')")
    @PutMapping("/addscoreapply")
    public AjaxResult updateAddscoreapply(@Validated @RequestBody PerAddscoreapply record) {
        AjaxResult ajax = AjaxResult.success();

        if (record.getApplyuserid() != getCurrentLoginUserid() && record.getApplyuserid() != 1) {
            // 非本人，非管理员
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

        Integer result = addscoreapplyService.updateAddscoreapply(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getApplyid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 加分申请", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('performance:addscoreapply:list')")
    @DeleteMapping("/addscoreapply/{applyid}")
    public AjaxResult deleteAddscoreapply(@PathVariable Integer applyid) {
        AjaxResult ajax = AjaxResult.success();

        PerAddscoreapply record = addscoreapplyService.selectAddscoreapplyById(applyid);

        if (record.getApplyuserid() != getCurrentLoginUserid() && record.getApplyuserid() != 1) {
            // 非本人，非管理员
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

        Integer result = addscoreapplyService.updateAddscoreapplyDeletedById(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getApplyid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }


    @PreAuthorize("@ss.hasPermi('performance:applylisttoconfirm:list')")
    @GetMapping("/applylisttoconfirm/list")
    public TableDataInfo getApplylisttoconfirmList(PerAddscoreapply record) {

        Integer userid = this.getCurrentLoginUserid();

        logger.debug("query parameters is " + record.getTeamidlinktext() + "  "+ record.getYear());

        startPage();

        record.setApplystatus("待审核");

        List<PerAddscoreapply> list = addscoreapplyService.selectAddscoreapply(record);

        return getDataTable(list);

    }

    @Log(title = "绩效评价 加分申请", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('performance:applylisttoconfirm:list')")
    @PutMapping("/applylisttoconfirm")
    public AjaxResult confirmAddscoreapply(@Validated @RequestBody PerAddscoreapply record) {
        AjaxResult ajax = AjaxResult.success();

        if (record.getAuditResult().equals("1")) {
            record.setApplystatus("审核通过");
        }
        else {
            record.setApplystatus("审核不通过");
        }

        Integer result = addscoreapplyService.updateAddscoreapplyConfirm(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getApplyid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }



    @PreAuthorize("@ss.hasPermi('performance:perfundreport:list')")
    @GetMapping("/perfundreport/list")
    public TableDataInfo getFundreportList(AudProject record) {

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

        logger.debug("query parameters is " + record.getTeamname() + "  "+ record.getProjectyear());

        if (validTeamid) {

            startPage();

            List<AudProject> list = fundreportService.selectPorjectFundreportList(record);

            return getDataTable(list);

        }
        else {
            return getDataTable(new ArrayList<>());
        }

    }


    @Log(title = "绩效评价 经费到账情况填报", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('performance:addscoreapply:list')")
    @PostMapping("/perfundreport")
    public AjaxResult addFundreport(@Validated @RequestBody PerFund record) {
        AjaxResult ajax = AjaxResult.success();


        record.setStatus("待确认");
        Integer result = fundreportService.insertPerFund(record);

        if (result > 0) {
            ajax.put(AjaxResult.DATA_TAG, record.getFundid());
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @Log(title = "绩效评价 经费到账情况填报", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('performance:perfundreport:list')")
    @PutMapping("/perfundreport")
    public AjaxResult updateFundreport(@Validated @RequestBody PerFund record) {
        AjaxResult ajax = AjaxResult.success();

        Integer result = fundreportService.updatePerFundFundById(record);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, record.getFundid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('performance:perfundconfirm:list')")
    @GetMapping("/perfundconfirm/list")
    public TableDataInfo getFundconfirmList(AudProject record) {

        Integer userid = this.getCurrentLoginUserid();

        logger.debug("query parameters is " + record.getTeamname() + "  "+ record.getProjectyear());

        startPage();

        List<AudProject> list = fundreportService.selectPorjectFundreportList(record);

        return getDataTable(list);

    }

    @Log(title = "绩效评价 经费到账情况确认", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('performance:perfundconfirm:list')")
    @PutMapping("/perfundconfirm/{fundids}")
    public AjaxResult confirmFundreport(@PathVariable Integer[] fundids) {
        AjaxResult ajax = AjaxResult.success();

        List<Integer> ids = new ArrayList<>();

        Iterator iterator = Arrays.asList(fundids).iterator();

        while (iterator.hasNext()) {
            Integer s = (Integer) iterator.next();
            if (s == null) {
                logger.debug("iterator.next() null is ");
                // iterator.remove(); 不能删除 null
            }
            else {
                logger.debug("iterator.next() is " + s.toString());
                ids.add(s);
            }
        }

        logger.debug("confirmFundreport ids is " + ids.toString());

        if (ids.size() ==0) {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

        Integer result = fundreportService.confirmPerFundByIds(ids);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, result);

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }




    @PreAuthorize("@ss.hasPermi('performance:perincomereport:list')")
    @GetMapping("/perincomereport/list")
    public TableDataInfo getIncomereportList(PerIncome record) {

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

            List<PerIncome> list = incomereportService.selectPerIncome(record);

            return getDataTable(list);

        }
        else {
            return getDataTable(new ArrayList<>());
        }

    }


}
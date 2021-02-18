package com.ruoyi.web.controller.sheet;

import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.service.AppClientinfoService;
import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.service.AudSignpicService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SMSSender.Juhe;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.common.config.IGtPushConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.sheet.domain.*;
import com.ruoyi.sheet.service.AudSheetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/sheet")
public class AudSheetController extends BaseController {
    @Resource
    private AudSheetService sheetService;
    @Resource
    private AppClientinfoService clientinfoService;
    @Resource
    private AudSignpicService audSignpicService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping("/tijiaoren/list")
    public TableDataInfo tijiaorenList(AudSheet query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query  is " + query.toString());


        startPage();
        List<AudSheet> list = sheetService.selectSheetTijiaorenByUserid(userId);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping(value = { "/{sheetid}" })
    public AjaxResult getSheet(@PathVariable(value = "sheetid", required = false) Integer sheetid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(sheetid))
        {
            logger.debug("sheetid = " + sheetid.toString());
            AudSheet p = sheetService.selectSheetTijiaorenById(sheetid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, p);
            }
            else {
                ajax = AjaxResult.error("sheetid：" + sheetid.toString() + " 不存在");
            }

        }
        return ajax;
    }

    /**
     * 根据编号,获取 明细 详细信息
     */
    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping(value = { "/budgetpay/{sheetid}" })
    public AjaxResult getSheetDetail(@PathVariable(value = "sheetid", required = false) Integer sheetid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(sheetid))
        {
            logger.debug("sheetid = " + sheetid.toString());
            List<AudBudgetpay> plist = sheetService.selectSheetBudgetPayById(sheetid);
            if (plist != null) {
                ajax.put(AjaxResult.DATA_TAG, plist);
            }
            else {
                ajax = AjaxResult.error("sheetid：" + sheetid.toString() + " 不存在");
            }

        }
        return ajax;
    }

    /**
     * 根据 获取 协作单位，即供应商的列表
     */
    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
    @GetMapping(value = { "/supplier/list" })
    public AjaxResult getSheetSupplier(SrmSupplierinfo query)
    {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("SrmSupplierinfo = " + query.toString());
        query.setIfdeleted(false);
        List<SrmSupplierinfo> plist = sheetService.selectSupplierInfoList(query);
        if (plist != null) {
            ajax.put(AjaxResult.DATA_TAG, plist);
        }
        else {
            ajax = AjaxResult.error("SrmSupplierinfo：" + query.getSuppliername() + " 不存在");
        }
        return ajax;
    }


    /**
     * 根据项目号，供应商号，获取 历史记录
     */
    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping(value = { "/budgetpay/record" })
    public TableDataInfo getSheetBudgetpayRecord(AudBudgetpayRecordQuery query)
    {
        startPage();
        logger.debug("getSheetBudgetpayRecord query = " + query.toString());
        List<AudBudgetpay> list = sheetService.selectBudgetPayRecord(query);
      //  logger.debug("getSheetBudgetpayRecord list = " + list.toString());
        return getDataTable(list);
    }

    private String signpicFilename(String signpicName) {
        // 本地资源路径
        String result = "";
        String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload" +  "/signpic/";
        if (signpicName != null && signpicName.isEmpty() == false ) {
            String checkFilePath = RuoYiConfig.getUploadPath() + "/signpic/" + signpicName;
            File desc = new File(checkFilePath);
            if (desc.exists() == true) {
                String filePath = fileDirPath + signpicName;
                result = filePath;
                logger.debug("url is " + filePath);
            }
         }
        return result;
    }

    /**
     * 根据项目号，供应商号，获取 历史记录
     */
    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping(value = { "/audit/record" })
    public AjaxResult getSheetAuditRecord(AudSheetauditrecord record)
    {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("getSheetBudgetpayRecord record = " + record.toString());
        List<AudSheetauditrecord> list = sheetService.selectSheetauditRecord(record);

        for(AudSheetauditrecord s: list) {
            String result = this.signpicFilename(s.getSignpicName());
            s.setSignpicName(result);
        }
        logger.debug("getSheetBudgetpayRecord list = " + list.toString());
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @Log(title = "拨付单管理", businessType = BusinessType.INSERT)
    @PostMapping("/tijiaoren")
    public AjaxResult add(@Validated @RequestBody AudSheet sheet) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        sheet.setSheetuserid(userId.intValue());

        Integer status = sheet.getSheetstatus();
        logger.debug("sheet.getSheetstatus is " + status.toString());

        sheet.setSheetstatus(SheetStatus.XiangMuShenPi.getCode());
        logger.debug("AudSheet add is " + sheet.toString());
        sheet.setSheetuserid(175); // 测试用户
        sheet.setSheettime(DateUtils.dateTimeNow());
        sheet.setSheettype("拨付单");
        sheet.setRelatedcontractid(-1);
        sheet.setThispaytimes("");

        Integer result = sheetService.addSheetTijiaoren(sheet);

        if (result > 0) {

            return AjaxResult.success(" 提交审核成功");
        }
        else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('sheet:audit3:list')")
    @GetMapping("/audit3/list")
    public TableDataInfo audit3List() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        startPage();
        List<AudSheet> list = sheetService.selectSheetXiangmuByUserid(87L);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit3:list')")
    @Log(title = "外拨款项目负责人审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit3")
    public AjaxResult audit3Confirm(@Validated @RequestBody AudSheet sheet) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        sheet.setConfirmUserid(87); // 测试用户。

        logger.debug("audit3 sheet is " + sheet.toString());
        AjaxResult ajax = AjaxResult.success();

        AudSignpic s = audSignpicService.selectSignpicByUserId(sheet.getConfirmUserid());

        if (s == null || this.signpicFilename(s.getSignpicName()).equals("")) {
            return AjaxResult.error("您的签名图片尚未上传，无法审批！");
        }

        //记录审核结论
//        record.SheetID = int.Parse(Request["kid"].ToString());
//        record.SheetType = "拨付单";
//        record.AuditType = Request["t"].ToString();    //把当前的审批环节作为类型记录进去
//        record.AuditOpinion = AuditOpinion1.txt_Opinion.Text;
//        record.AuditResult = Convert.ToBoolean(int.Parse(AuditOpinion1.rbtnl_Result.SelectedValue));
//        record.AuditTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//        record.AuditUserID = int.Parse(Session["CurrentUserID"].ToString());

        AudSheetauditrecord record = new AudSheetauditrecord();
        record.setSheetid(sheet.getSheetid());
        record.setSheettype(sheet.getSheettype());
        record.setAudittype("3");
        record.setAuditopinion(sheet.getConfirmNote());
        record.setAudittime(DateUtils.dateTimeNow());
        record.setAudituserid(userId.intValue());

        if (sheet.getConfirmResult() == 1) {
            record.setAuditresult(true);
            sheet.setSheetstatus(SheetStatus.BuMenShenPi.getCode());
            sheetService.updateSheetAuditStatus(sheet, record);
        }
        else if (sheet.getConfirmResult() == 2) {
            record.setAuditresult(false);
            sheet.setSheetstatus(SheetStatus.NoPass.getCode());
            sheetService.updateSheetAuditStatus(sheet, record);
        }
        else {
            return AjaxResult.error("审批'" + sheet.getSheetcode() + "'失败，没有选择审批结果");
        }

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit4:list')")
    @GetMapping("/audit4/list")
    public TableDataInfo audit4List() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        startPage();
        List<AudSheet> list = sheetService.selectSheetBuMenByUserid(87L);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit4:list')")
    @Log(title = "外拨款部门负责人审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit4")
    public AjaxResult audit4Confirm(@Validated @RequestBody AudSheet sheet) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        sheet.setConfirmUserid(87); // 测试用户。

        logger.debug("audit3 sheet is " + sheet.toString());
        AjaxResult ajax = AjaxResult.success();

        AudSignpic s = audSignpicService.selectSignpicByUserId(sheet.getConfirmUserid());

        if (s == null || this.signpicFilename(s.getSignpicName()).equals("")) {
            return AjaxResult.error("您的签名图片尚未上传，无法审批！");
        }

        //记录审核结论
//        record.SheetID = int.Parse(Request["kid"].ToString());
//        record.SheetType = "拨付单";
//        record.AuditType = Request["t"].ToString();    //把当前的审批环节作为类型记录进去
//        record.AuditOpinion = AuditOpinion1.txt_Opinion.Text;
//        record.AuditResult = Convert.ToBoolean(int.Parse(AuditOpinion1.rbtnl_Result.SelectedValue));
//        record.AuditTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//        record.AuditUserID = int.Parse(Session["CurrentUserID"].ToString());

        AudSheetauditrecord record = new AudSheetauditrecord();
        record.setSheetid(sheet.getSheetid());
        record.setSheettype(sheet.getSheettype());
        record.setAudittype("4");
        record.setAuditopinion(sheet.getConfirmNote());
        record.setAudittime(DateUtils.dateTimeNow());
        record.setAudituserid(userId.intValue());

        if (sheet.getConfirmResult() == 1) {
            record.setAuditresult(true);
            sheet.setSheetstatus(SheetStatus.ChuShenPi.getCode());
            sheetService.updateSheetAuditStatus(sheet, record);
        }
        else if (sheet.getConfirmResult() == 2) {
            record.setAuditresult(false);
            sheet.setSheetstatus(SheetStatus.NoPass.getCode());
            sheetService.updateSheetAuditStatus(sheet, record);
        }
        else {
            return AjaxResult.error("审批'" + sheet.getSheetcode() + "'失败，没有选择审批结果");
        }

        return ajax;
    }
}
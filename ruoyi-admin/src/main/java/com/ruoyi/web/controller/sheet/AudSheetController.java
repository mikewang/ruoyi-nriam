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
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
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


    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }

    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping("/tijiaoren/list")
    public TableDataInfo tijiaorenList(AudSheet query) {

        logger.debug("query  is " + query.toString());

        Integer uid = getCurrentLoginUserid();
        uid = 175; // 测试用户

        startPage();
        List<AudSheet> list = sheetService.selectSheetTijiaorenByUserid(uid);

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
     * 根据编号 删除信息
     */
    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @DeleteMapping(value = { "/{sheetid}" })
    public AjaxResult deleteSheet(@PathVariable(value = "sheetid", required = false) Integer sheetid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(sheetid))
        {
            logger.debug("sheetid = " + sheetid.toString());
            AudSheet sheet = sheetService.selectSheetTijiaorenById(sheetid);
            if (sheet != null) {
                logger.debug("sheet is " + sheet.toString());
                Integer userid = getCurrentLoginUserid();
                userid = 175; // 测试用户
                if (sheet.getSheetuserid().equals(userid)) {
                    sheet.setSheetstatus(SheetStatus.YiZuoFei.getCode());
                    sheetService.updateSheetAuditStatus(sheet,null);
                }
                else {
                    ajax = AjaxResult.error("sheetid：" + sheetid.toString() + " 不存在");
                }
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
        logger.debug("getSheetAuditRecord 审批记录 = " + record.toString());
        List<AudSheetauditrecord> list = sheetService.selectSheetauditRecord(record);

        for(AudSheetauditrecord s: list) {
            String result = this.signpicFilename(s.getSignpicName());
            s.setSignpicName(result);
        }
        logger.debug("getSheetAuditRecord 审批记录 = " + list.toString());
        ajax.put(AjaxResult.DATA_TAG, list);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @Log(title = "拨付单管理", businessType = BusinessType.INSERT)
    @PostMapping("/tijiaoren")
    public AjaxResult add(@Validated @RequestBody AudSheet sheet) {

        Integer userid = getCurrentLoginUserid();

        sheet.setSheetuserid(userid);

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

    private AjaxResult auditConfirm( AudSheet sheet, Integer audittype) {
        AjaxResult ajax = AjaxResult.success();
        Integer userid = getCurrentLoginUserid();
        sheet.setConfirmUserid(userid);

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
        record.setAudittype(audittype.toString());
        record.setAuditopinion(sheet.getConfirmNote());
        record.setAudittime(DateUtils.dateTimeNow());
        record.setAudituserid(userid);

        if (sheet.getConfirmResult() == 1) {
            record.setAuditresult(true);
            if (audittype == 3) {
                sheet.setSheetstatus(SheetStatus.BuMenShenPi.getCode());
            }
            else  if (audittype == 4) {
                sheet.setSheetstatus(SheetStatus.ChuShenPi.getCode());
            }
            else  if (audittype == 5) {
                sheet.setSheetstatus(SheetStatus.FenGuanSuoShenPi.getCode());
            }
            else  if (audittype == 6) {
                int flag = sheet.getHejiZong().compareTo(BigDecimal.valueOf(50000L));
                if (flag >= 0) {
                    // //拨付单总金额大于5万，到所长审批
                    sheet.setSheetstatus(SheetStatus.SuoZhangShenPi.getCode());
                }
                else {
                    sheet.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
                }
            }
            else  if (audittype == 7) {
                sheet.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
            }

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

    @PreAuthorize("@ss.hasPermi('sheet:audit3:list')")
    @GetMapping("/audit3/list")
    public TableDataInfo audit3List() {
        Integer userid = getCurrentLoginUserid();
        startPage();
        List<AudSheet> list = sheetService.selectSheetXiangmuByUserid(userid);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit3:list')")
    @Log(title = "外拨款项目负责人审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit3")
    public AjaxResult audit3Confirm(@Validated @RequestBody AudSheet sheet) {

        logger.debug("audit3 sheet is " + sheet.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(sheet,3);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit4:list')")
    @GetMapping("/audit4/list")
    public TableDataInfo audit4List() {
        Integer userid = getCurrentLoginUserid();
        startPage();
        List<AudSheet> list = sheetService.selectSheetBuMenByUserid(userid);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit4:list')")
    @Log(title = "外拨款部门负责人审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit4")
    public AjaxResult audit4Confirm(@Validated @RequestBody AudSheet sheet) {
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(sheet,4);
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('sheet:audit5:list')")
    @GetMapping("/audit5/list")
    public TableDataInfo audit5List() {
        Integer userid = getCurrentLoginUserid();
        startPage();
        List<AudSheet> list = sheetService.selectSheetChuByUserid(userid);
        return getDataTable(list);
    }


    @PreAuthorize("@ss.hasPermi('sheet:audit5:list')")
    @Log(title = "外拨款分管处的负责人审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit5")
    public AjaxResult audit5Confirm(@Validated @RequestBody AudSheet sheet) {
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(sheet,5);
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('sheet:audit6:list')")
    @GetMapping("/audit6/list")
    public TableDataInfo audit6List() {
        Integer userid = getCurrentLoginUserid();
        startPage();
        List<AudSheet> list = sheetService.selectSheetFenguansuoByUserid(userid);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit6:list')")
    @Log(title = "外拨款分管所长审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit6")
    public AjaxResult audit6Confirm(@Validated @RequestBody AudSheet sheet) {
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(sheet,6);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit7:list')")
    @GetMapping("/audit7/list")
    public TableDataInfo audit7List() {
        Integer userid = getCurrentLoginUserid();
        startPage();
        List<AudSheet> list = sheetService.selectSheetSuozhangByUserid(userid);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sheet:audit7:list')")
    @Log(title = "外拨款所长审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit7")
    public AjaxResult audit7Confirm(@Validated @RequestBody AudSheet sheet) {
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(sheet,7);
        return ajax;
    }
}
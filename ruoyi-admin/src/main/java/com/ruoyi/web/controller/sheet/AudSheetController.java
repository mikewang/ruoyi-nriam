package com.ruoyi.web.controller.sheet;

import com.ruoyi.api.domain.AppClientinfo;
import com.ruoyi.api.service.AppClientinfoService;
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
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.push.PushMessageToApp;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.common.config.IGtPushConfig;
import com.ruoyi.framework.web.service.TokenService;
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

        // 本地资源路径

        String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload" +  "/signpic/";

        for(AudSheetauditrecord s: list) {
            if (s.getSignpicName() != null && s.getSignpicName().isEmpty() == false ) {
                String checkFilePath = RuoYiConfig.getUploadPath() + "/signpic/" + s.getSignpicName();
                logger.debug("checkFilePath is " + s.getSignpicName());
                File desc = new File(checkFilePath);
                if (desc.exists() == true) {
                    String filePath = fileDirPath + s.getSignpicName();
                    s.setSignpicName(filePath);
                    logger.debug("url is " + filePath);
                }
                else {
                    s.setSignpicName("");
                }
            }
            else {
                s.setSignpicName("");
            }
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
        sheet.setSheetuserid(175);
        sheet.setSheettime(DateUtils.dateTimeNow());
        sheet.setSheettype("拨付单");
        sheet.setRelatedcontractid(-1);
        sheet.setThispaytimes("");

        Integer result = sheetService.addSheetTijiaoren(sheet);

        if (result > 0) {

//            api.MsgManager.SendToAUser(am.ToUserID.ToString(), am.MessageTitle,
//                    "新的拨付单：" + code + "待审批。");
//
//            //发送短信
//            CommonFunc.SendSMS_ToAudit(am.ToUserID.ToString(),
//                    "#type#=拨付单&#name#=" + code);

            // 发送推送
            Integer userid = sheet.getProjectmanagerid();
            AppClientinfo client =  clientinfoService.selectAppClientinfoByUserid(userid);
            String clientId = client.getClientid();

            String msgTitle = "新的拨付单待审批";
            String msgBody = "新的拨付单：" + sheet.getSheetcode() + "待审批。";

//            PushMessageToApp.pushMessageToSingle(clientId, msgTitle, msgBody);

            return AjaxResult.success(" 提交审核成功");
        }
        else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }
}
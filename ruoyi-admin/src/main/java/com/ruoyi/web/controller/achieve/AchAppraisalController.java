package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchAppraisal;
import com.ruoyi.achieve.service.AchAppraisalService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/achieve/appraisal")
public class AchAppraisalController extends BaseController {
    @Resource
    private AchAppraisalService appraisalService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    private Long getCurrentLoginUserId(){
        LoginUser loginUser=tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId=loginUser.getUser().getUserId();

        userId=87L; // 测试用户 changchun 。
        return userId;
    }

    @PreAuthorize("@ss.hasPermi('achieve:appraisal:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchAppraisal query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchAppraisal> list = appraisalService.selectAchAppraisalList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:appraisal:list')")
    @GetMapping(value = { "/{appraisalid}" })
    public AjaxResult getAppraisal(@PathVariable(value = "appraisalid", required = false) Integer appraisalid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(appraisalid))
        {
            AchAppraisal p = appraisalService.selectAchAppraisalById(appraisalid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, appraisalService.selectAchAppraisalById(appraisalid));
            }
            else {
                ajax = AjaxResult.error("appraisalid：" + appraisalid.toString() + " 不存在");
            }

        }
        return ajax;
    }




    @PreAuthorize("@ss.hasPermi('achieve:appraisal:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchAppraisal appraisal) {

        Long userId = getCurrentLoginUserId();
        appraisal.setCreateuserid(userId.intValue());

        Integer status = appraisal.getStatus();
        logger.debug("AchAppraisal .getStatus is " + status.toString());


        appraisal.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = appraisalService.insertAchAppraisal(appraisal);

        if (rows > 0 ) {
            Integer appraisalid = appraisal.getAppraisalid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, appraisalid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:appraisal:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchAppraisal appraisal) {

        AchAppraisal undoAppraisal = appraisalService.selectAchAppraisalById(appraisal.getAppraisalid());


        Integer status = appraisal.getStatus();
        logger.debug("appraisal.getStatus is " + status.toString());
        logger.debug("updateAchAppraisal.  is " + appraisal.toString());
        Integer res = appraisalService.updateAchAppraisal(appraisal);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('achieve:toconfirm:list')")
    @Log(title = "成果审核", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm")
    public AjaxResult confirm(@Validated @RequestBody  AchAppraisal appraisal) {

        AchAppraisal undoAppraisal = appraisalService.selectAchAppraisalById(appraisal.getAppraisalid());
        logger.debug("undoAppraisal is " + undoAppraisal.toString());
        if (undoAppraisal.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + appraisal.getAppraisalname() + "' 失败，状态不对");
        }

        Integer status = appraisal.getStatus();
        logger.debug("appraisal.getStatus is " + status.toString());

        Integer res = appraisalService.confirmAchAppraisal(appraisal);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    /**
     * 根据编号获取审核详细信息。
     */
    @PreAuthorize("@ss.hasPermi('achieve:appraisal:list')")
    @GetMapping(value = { "/confirm/{appraisalid}/{status}" })
    public AjaxResult getAppraisalConfirm(@PathVariable(value = "appraisalid") Integer appraisalid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(appraisalid))
        {
            ajax.put(AjaxResult.DATA_TAG, appraisalService.selectApplyByTypeAndRelatedID(appraisalid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:appraisal:list')")
    @DeleteMapping(value = { "/{appraisalid}" })
    public AjaxResult removeAppraisal(@PathVariable(value = "appraisalid", required = false) Integer appraisalid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(appraisalid))
        {
            Integer rows = appraisalService.removeAchAppraisal(appraisalid);
            if (rows != 1) {
                ajax = AjaxResult.error("appraisalid：" + appraisalid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
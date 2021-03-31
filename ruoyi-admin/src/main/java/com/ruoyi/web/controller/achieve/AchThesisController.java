package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchThesis;
import com.ruoyi.achieve.service.AchThesisService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
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
@RequestMapping("/achieve/thesis")
public class AchThesisController extends BaseController {
    @Resource
    private AchThesisService thesisService;

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

    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchThesis query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchThesis> list = thesisService.selectAchThesisList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @GetMapping(value = { "/{thesisid}" })
    public AjaxResult getThesis(@PathVariable(value = "thesisid", required = false) Integer thesisid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(thesisid))
        {
            AchThesis p = thesisService.selectAchThesisById(thesisid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, thesisService.selectAchThesisById(thesisid));
            }
            else {
                ajax = AjaxResult.error("thesisid：" + thesisid.toString() + " 不存在");
            }

        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @GetMapping( "/unique")
    public AjaxResult getIfDuplicate(AchThesis thesis)
    {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters getThesisname is " + thesis.getThesisname());

        Integer thesisid = thesis.getThesisid();

        String thesisname = thesis.getThesisname();

        if (StringUtils.isNotNull(thesisname))
        {
            ajax.put(AjaxResult.DATA_TAG, thesisService.queryIfDuplicate(thesis));
        }
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchThesis thesis) {

        Long userId = getCurrentLoginUserId();

        thesis.setCreateuserid(userId.intValue());

        Integer status = thesis.getStatus();
        logger.debug("AchThesis .getStatus is " + status.toString());

        if (StringUtils.isNotEmpty(thesis.getThesisname())
                && UserConstants.NOT_UNIQUE.equals(thesisService.queryIfDuplicate(thesis)))
        {
            return AjaxResult.error("操作'" + thesis.getThesisname() + "'失败，论文名称已存在");
        }

        thesis.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = thesisService.insertAchThesis(thesis);

        if (rows > 0 ) {
            Integer thesisid = thesis.getThesisid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, thesisid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchThesis thesis) {

        AchThesis undoThesis = thesisService.selectAchThesisById(thesis.getThesisid());

        if (StringUtils.isNotEmpty(thesis.getThesisname()) &&undoThesis.getThesisname() != thesis.getThesisname()) {

            if (UserConstants.NOT_UNIQUE.equals(thesisService.queryIfDuplicate(thesis)))
            {
                return AjaxResult.error("操作'" + thesis.getThesisname() + "'失败，论文名称已存在");
            }
        }
        Integer status = thesis.getStatus();
        logger.debug("thesis.getStatus is " + status.toString());

        Integer res = thesisService.updateAchThesis(thesis);

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
    public AjaxResult confirm(@Validated @RequestBody  AchThesis thesis) {

        AchThesis undoThesis = thesisService.selectAchThesisById(thesis.getThesisid());
        logger.debug("undoThesis is " + undoThesis.toString());
        if (undoThesis.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + thesis.getThesisname() + "' 失败，状态不对");
        }

        Integer status = thesis.getStatus();
        logger.debug("thesis.getStatus is " + status.toString());

        Integer res = thesisService.confirmAchThesis(thesis);

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
    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @GetMapping(value = { "/confirm/{thesisid}/{status}" })
    public AjaxResult getThesisConfirm(@PathVariable(value = "thesisid") Integer thesisid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(thesisid))
        {
            ajax.put(AjaxResult.DATA_TAG, thesisService.selectApplyByTypeAndRelatedID(thesisid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:thesis:list')")
    @DeleteMapping(value = { "/{thesisid}" })
    public AjaxResult removeThesis(@PathVariable(value = "thesisid", required = false) Integer thesisid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(thesisid))
        {
            Integer rows = thesisService.removeAchThesis(thesisid);
            if (rows != 1) {
                ajax = AjaxResult.error("thesisid：" + thesisid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
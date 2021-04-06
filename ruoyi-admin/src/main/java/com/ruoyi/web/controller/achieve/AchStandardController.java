package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchStandard;
import com.ruoyi.achieve.service.AchStandardService;
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
@RequestMapping("/achieve/standard")
public class AchStandardController extends BaseController {
    @Resource
    private AchStandardService standardService;

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

    @PreAuthorize("@ss.hasPermi('achieve:standard:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchStandard query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchStandard> list = standardService.selectAchStandardList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:standard:list')")
    @GetMapping(value = { "/{standardid}" })
    public AjaxResult getStandard(@PathVariable(value = "standardid", required = false) Integer standardid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(standardid))
        {
            AchStandard p = standardService.selectAchStandardById(standardid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, standardService.selectAchStandardById(standardid));
            }
            else {
                ajax = AjaxResult.error("standardid：" + standardid.toString() + " 不存在");
            }

        }
        return ajax;
    }




    @PreAuthorize("@ss.hasPermi('achieve:standard:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchStandard standard) {

        Long userId = getCurrentLoginUserId();

        standard.setCreateuserid(userId.intValue());
        standard.setProjectid(-1);

        Integer status = standard.getStatus();
        logger.debug("AchStandard .getStatus is " + status.toString());


        standard.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = standardService.insertAchStandard(standard);

        if (rows > 0 ) {
            Integer standardid = standard.getStandardid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, standardid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:standard:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchStandard standard) {

        AchStandard undoStandard = standardService.selectAchStandardById(standard.getStandardid());


        Integer status = standard.getStatus();
        logger.debug("standard.getStatus is " + status.toString());

        Integer res = standardService.updateAchStandard(standard);

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
    public AjaxResult confirm(@Validated @RequestBody  AchStandard standard) {

        AchStandard undoStandard = standardService.selectAchStandardById(standard.getStandardid());
        logger.debug("undoStandard is " + undoStandard.toString());
        if (undoStandard.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + standard.getStandardname() + "' 失败，状态不对");
        }

        Integer status = standard.getStatus();
        logger.debug("standard.getStatus is " + status.toString());

        Integer res = standardService.confirmAchStandard(standard);

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
    @PreAuthorize("@ss.hasPermi('achieve:standard:list')")
    @GetMapping(value = { "/confirm/{standardid}/{status}" })
    public AjaxResult getStandardConfirm(@PathVariable(value = "standardid") Integer standardid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(standardid))
        {
            ajax.put(AjaxResult.DATA_TAG, standardService.selectApplyByTypeAndRelatedID(standardid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:standard:list')")
    @DeleteMapping(value = { "/{standardid}" })
    public AjaxResult removeStandard(@PathVariable(value = "standardid", required = false) Integer standardid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(standardid))
        {
            Integer rows = standardService.removeAchStandard(standardid);
            if (rows != 1) {
                ajax = AjaxResult.error("standardid：" + standardid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
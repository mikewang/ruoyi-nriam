package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchSoftware;
import com.ruoyi.achieve.service.AchSoftwareService;
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
@RequestMapping("/achieve/software")
public class AchSoftwareController extends BaseController {
    @Resource
    private AchSoftwareService softwareService;

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

    @PreAuthorize("@ss.hasPermi('achieve:software:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchSoftware query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchSoftware> list = softwareService.selectAchSoftwareList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:software:list')")
    @GetMapping(value = { "/{softwareid}" })
    public AjaxResult getSoftware(@PathVariable(value = "softwareid", required = false) Integer softwareid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(softwareid))
        {
            AchSoftware p = softwareService.selectAchSoftwareById(softwareid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, softwareService.selectAchSoftwareById(softwareid));
            }
            else {
                ajax = AjaxResult.error("softwareid：" + softwareid.toString() + " 不存在");
            }

        }
        return ajax;
    }




    @PreAuthorize("@ss.hasPermi('achieve:software:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchSoftware software) {

        Long userId = getCurrentLoginUserId();

        software.setCreateuserid(userId.intValue());
        software.setProjectid(-1);

        Integer status = software.getStatus();
        logger.debug("AchSoftware .getStatus is " + status.toString());


        software.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = softwareService.insertAchSoftware(software);

        if (rows > 0 ) {
            Integer softwareid = software.getSoftwareid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, softwareid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:software:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchSoftware software) {

        AchSoftware undoSoftware = softwareService.selectAchSoftwareById(software.getSoftwareid());


        Integer status = software.getStatus();
        logger.debug("software.getStatus is " + status.toString());

        Integer res = softwareService.updateAchSoftware(software);

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
    public AjaxResult confirm(@Validated @RequestBody  AchSoftware software) {

        AchSoftware undoSoftware = softwareService.selectAchSoftwareById(software.getSoftwareid());
        logger.debug("undoSoftware is " + undoSoftware.toString());
        if (undoSoftware.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + software.getSoftwarename() + "' 失败，状态不对");
        }

        Integer status = software.getStatus();
        logger.debug("software.getStatus is " + status.toString());

        Integer res = softwareService.confirmAchSoftware(software);

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
    @PreAuthorize("@ss.hasPermi('achieve:software:list')")
    @GetMapping(value = { "/confirm/{softwareid}/{status}" })
    public AjaxResult getSoftwareConfirm(@PathVariable(value = "softwareid") Integer softwareid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(softwareid))
        {
            ajax.put(AjaxResult.DATA_TAG, softwareService.selectApplyByTypeAndRelatedID(softwareid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:software:list')")
    @DeleteMapping(value = { "/{softwareid}" })
    public AjaxResult removeSoftware(@PathVariable(value = "softwareid", required = false) Integer softwareid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(softwareid))
        {
            Integer rows = softwareService.removeAchSoftware(softwareid);
            if (rows != 1) {
                ajax = AjaxResult.error("softwareid：" + softwareid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
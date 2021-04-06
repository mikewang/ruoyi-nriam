package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchTech;
import com.ruoyi.achieve.service.AchTechService;
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
@RequestMapping("/achieve/tech")
public class AchTechController extends BaseController {
    @Resource
    private AchTechService techService;

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

    @PreAuthorize("@ss.hasPermi('achieve:tech:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchTech query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchTech> list = techService.selectAchTechList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:tech:list')")
    @GetMapping(value = { "/{techid}" })
    public AjaxResult getTech(@PathVariable(value = "techid", required = false) Integer techid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(techid))
        {
            AchTech p = techService.selectAchTechById(techid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, techService.selectAchTechById(techid));
            }
            else {
                ajax = AjaxResult.error("techid：" + techid.toString() + " 不存在");
            }

        }
        return ajax;
    }




    @PreAuthorize("@ss.hasPermi('achieve:tech:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchTech tech) {

        Long userId = getCurrentLoginUserId();

        tech.setCreateuserid(userId.intValue());
        tech.setProjectid(-1);

        Integer status = tech.getStatus();
        logger.debug("AchTech .getStatus is " + status.toString());


        tech.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = techService.insertAchTech(tech);

        if (rows > 0 ) {
            Integer techid = tech.getTechid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, techid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:tech:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchTech tech) {

        AchTech undoTech = techService.selectAchTechById(tech.getTechid());


        Integer status = tech.getStatus();
        logger.debug("tech.getStatus is " + status.toString());
        tech.setProjectid(-1);
        Integer res = techService.updateAchTech(tech);

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
    public AjaxResult confirm(@Validated @RequestBody  AchTech tech) {

        AchTech undoTech = techService.selectAchTechById(tech.getTechid());
        logger.debug("undoTech is " + undoTech.toString());
        if (undoTech.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + tech.getTechname() + "' 失败，状态不对");
        }

        Integer status = tech.getStatus();
        logger.debug("tech.getStatus is " + status.toString());

        Integer res = techService.confirmAchTech(tech);

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
    @PreAuthorize("@ss.hasPermi('achieve:tech:list')")
    @GetMapping(value = { "/confirm/{techid}/{status}" })
    public AjaxResult getTechConfirm(@PathVariable(value = "techid") Integer techid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(techid))
        {
            ajax.put(AjaxResult.DATA_TAG, techService.selectApplyByTypeAndRelatedID(techid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:tech:list')")
    @DeleteMapping(value = { "/{techid}" })
    public AjaxResult removeTech(@PathVariable(value = "techid", required = false) Integer techid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(techid))
        {
            Integer rows = techService.removeAchTech(techid);
            if (rows != 1) {
                ajax = AjaxResult.error("techid：" + techid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
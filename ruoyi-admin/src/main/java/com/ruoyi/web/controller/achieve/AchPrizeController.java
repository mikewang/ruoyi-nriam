package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchPrize;
import com.ruoyi.achieve.service.AchPrizeService;
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
@RequestMapping("/achieve/prize")
public class AchPrizeController extends BaseController {
    @Resource
    private AchPrizeService prizeService;

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

    @PreAuthorize("@ss.hasPermi('achieve:prize:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchPrize query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchPrize> list = prizeService.selectAchPrizeList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:prize:list')")
    @GetMapping(value = { "/{prizeid}" })
    public AjaxResult getPrize(@PathVariable(value = "prizeid", required = false) Integer prizeid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(prizeid))
        {
            AchPrize p = prizeService.selectAchPrizeById(prizeid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, prizeService.selectAchPrizeById(prizeid));
            }
            else {
                ajax = AjaxResult.error("prizeid：" + prizeid.toString() + " 不存在");
            }

        }
        return ajax;
    }




    @PreAuthorize("@ss.hasPermi('achieve:prize:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchPrize prize) {

        Long userId = getCurrentLoginUserId();

        prize.setCreateuserid(userId.intValue());

        Integer status = prize.getStatus();
        logger.debug("AchPrize .getStatus is " + status.toString());


        prize.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = prizeService.insertAchPrize(prize);

        if (rows > 0 ) {
            Integer prizeid = prize.getPrizeid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, prizeid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:prize:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchPrize prize) {

        AchPrize undoPrize = prizeService.selectAchPrizeById(prize.getPrizeid());


        Integer status = prize.getStatus();
        logger.debug("prize.getStatus is " + status.toString());

        Integer res = prizeService.updateAchPrize(prize);

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
    public AjaxResult confirm(@Validated @RequestBody  AchPrize prize) {

        AchPrize undoPrize = prizeService.selectAchPrizeById(prize.getPrizeid());
        logger.debug("undoPrize is " + undoPrize.toString());
        if (undoPrize.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + prize.getPrizename() + "' 失败，状态不对");
        }

        Integer status = prize.getStatus();
        logger.debug("prize.getStatus is " + status.toString());

        Integer res = prizeService.confirmAchPrize(prize);

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
    @PreAuthorize("@ss.hasPermi('achieve:prize:list')")
    @GetMapping(value = { "/confirm/{prizeid}/{status}" })
    public AjaxResult getPrizeConfirm(@PathVariable(value = "prizeid") Integer prizeid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(prizeid))
        {
            ajax.put(AjaxResult.DATA_TAG, prizeService.selectApplyByTypeAndRelatedID(prizeid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:prize:list')")
    @DeleteMapping(value = { "/{prizeid}" })
    public AjaxResult removePrize(@PathVariable(value = "prizeid", required = false) Integer prizeid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(prizeid))
        {
            Integer rows = prizeService.removeAchPrize(prizeid);
            if (rows != 1) {
                ajax = AjaxResult.error("prizeid：" + prizeid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
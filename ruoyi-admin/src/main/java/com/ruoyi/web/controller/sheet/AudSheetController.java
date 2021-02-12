package com.ruoyi.web.controller.sheet;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.service.AchPatentService;
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
import com.ruoyi.sheet.domain.AudSheet;
import com.ruoyi.sheet.service.AudSheetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sheet/tijiaoren")
public class AudSheetController extends BaseController {
    @Resource
    private AudSheetService sheetService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('sheet:tijiaoren:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudSheet query) {
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
    public AjaxResult getPatent(@PathVariable(value = "sheetid", required = false) Integer sheetid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(sheetid))
        {
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


}
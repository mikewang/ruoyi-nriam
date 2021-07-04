package com.ruoyi.web.controller.sku;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ProjectColor;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.domain.AudProjectdoc;
import com.ruoyi.project.service.*;
import com.ruoyi.sku.domain.SkuInfo;
import com.ruoyi.sku.domain.SkuPhoto;
import com.ruoyi.sku.service.SkuService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sku")
public class SkuController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private SkuService skuService;


    private Long getCurrentLoginUserId() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

//        userId = 87L; // 测试用户 changchun 。
        return userId;
    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping("/sku/list")
    public TableDataInfo skuList(SkuInfo skuInfo) {

        startPage();

        List<SkuInfo> list = skuService.selectSkuList(skuInfo);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping(value = { "/sku/{skuId}" })
    public AjaxResult getSku(@PathVariable(value = "skuId", required = false) Long skuId)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(skuId))
        {
            ajax.put(AjaxResult.DATA_TAG, skuService.selectSkuInfoById(skuId));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.INSERT)
    @PostMapping("/sku")
    public AjaxResult add(@Validated @RequestBody SkuInfo skuInfo) {

        Long userId = getCurrentLoginUserId();

        skuInfo.setUserId(userId);
        skuInfo.setStatus(1);

        Long rows = skuService.insertSkuInfo(skuInfo);

        if (rows > 0 ) {
            Long skuId = skuInfo.getSkuId();


             AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, skuId);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.UPDATE)
    @PutMapping("/sku")
    public AjaxResult update(@Validated @RequestBody SkuInfo skuInfo) {

        Long userId = getCurrentLoginUserId();

        skuInfo.setUserId(userId);
        skuInfo.setStatus(1);

        Long rows = skuService.insertSkuInfo(skuInfo);

        if (rows > 0 ) {
            Long skuId = skuInfo.getSkuId();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, skuId);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

}
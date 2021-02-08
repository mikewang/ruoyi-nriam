package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;

import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.project.service.BasDocService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/bas/doc")
public class BasDocController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private BasDocService basDocService;


    @PreAuthorize("@ss.hasPermi('bas:doc:list')")
    @GetMapping("/list")
    public AjaxResult list(BasDoc query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        List<BasDoc> list = basDocService.selectBasDocList(query);
        logger.debug("selectProjectdocList is ", list.toString());
        ajax.put(AjaxResult.DATA_TAG, list);

        return ajax;
    }



    @PreAuthorize("@ss.hasPermi('bas:doc:add')")
    @Log(title = "基础文档管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BasDoc doc) {

        AjaxResult ajax = AjaxResult.success();
        basDocService.insertBasDoc(doc);
        return ajax;

    }

    @PreAuthorize("@ss.hasPermi('bas:doc:edit')")
    @Log(title = "基础文档管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  BasDoc doc) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;

    }


    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('bas:doc:remove')")
    @Log(title = "基础文档管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {

        return AjaxResult.success("");


    }

}
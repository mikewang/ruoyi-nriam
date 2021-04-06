package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchProduct;
import com.ruoyi.achieve.service.AchProductService;
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
@RequestMapping("/achieve/product")
public class AchProductController extends BaseController {
    @Resource
    private AchProductService productService;

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

    @PreAuthorize("@ss.hasPermi('achieve:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchProduct query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchProduct> list = productService.selectAchProductList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:product:list')")
    @GetMapping(value = { "/{productid}" })
    public AjaxResult getProduct(@PathVariable(value = "productid", required = false) Integer productid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(productid))
        {
            AchProduct p = productService.selectAchProductById(productid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, productService.selectAchProductById(productid));
            }
            else {
                ajax = AjaxResult.error("productid：" + productid.toString() + " 不存在");
            }

        }
        return ajax;
    }




    @PreAuthorize("@ss.hasPermi('achieve:product:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchProduct product) {

        Long userId = getCurrentLoginUserId();

        product.setCreateuserid(userId.intValue());
        product.setProjectid(-1);

        Integer status = product.getStatus();
        logger.debug("AchProduct .getStatus is " + status.toString());


        product.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = productService.insertAchProduct(product);

        if (rows > 0 ) {
            Integer productid = product.getProductid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, productid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:product:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchProduct product) {

        AchProduct undoProduct = productService.selectAchProductById(product.getProductid());


        Integer status = product.getStatus();
        logger.debug("product.getStatus is " + status.toString());

        Integer res = productService.updateAchProduct(product);

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
    public AjaxResult confirm(@Validated @RequestBody  AchProduct product) {

        AchProduct undoProduct = productService.selectAchProductById(product.getProductid());
        logger.debug("undoProduct is " + undoProduct.toString());
        if (undoProduct.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + product.getProductname() + "' 失败，状态不对");
        }

        Integer status = product.getStatus();
        logger.debug("product.getStatus is " + status.toString());

        Integer res = productService.confirmAchProduct(product);

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
    @PreAuthorize("@ss.hasPermi('achieve:product:list')")
    @GetMapping(value = { "/confirm/{productid}/{status}" })
    public AjaxResult getProductConfirm(@PathVariable(value = "productid") Integer productid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(productid))
        {
            ajax.put(AjaxResult.DATA_TAG, productService.selectApplyByTypeAndRelatedID(productid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:product:list')")
    @DeleteMapping(value = { "/{productid}" })
    public AjaxResult removeProduct(@PathVariable(value = "productid", required = false) Integer productid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(productid))
        {
            Integer rows = productService.removeAchProduct(productid);
            if (rows != 1) {
                ajax = AjaxResult.error("productid：" + productid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}
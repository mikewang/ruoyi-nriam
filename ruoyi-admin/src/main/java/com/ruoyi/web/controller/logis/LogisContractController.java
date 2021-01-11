package com.ruoyi.web.controller.logis;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.logis.domain.LogisContract;
import com.ruoyi.logis.service.LogisContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/logis/contract")
public class LogisContractController extends BaseController
{
    @Resource
    private LogisContractService logisContractService;

    @Resource
    private TokenService tokenService;

    @PreAuthorize("@ss.hasPermi('logis:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(LogisContract contract)
    {
        startPage();
        List<LogisContract> list = logisContractService.selectLogisContractList(contract);
        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('logis:contract:query')")
    @GetMapping(value = "/{contractId}")
    public AjaxResult getInfo(@PathVariable Long contractId)
    {
        return AjaxResult.success(logisContractService.selectLogisContractById(contractId));
    }

    /**
     * 新增合同
     */
    @PreAuthorize("@ss.hasPermi('logis:contract:add')")
    @Log(title = "合同管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody LogisContract contract)
    {
        System.out.println("add contract is " + contract.toString());
//        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
//        {
//            return AjaxResult.error("新增合同'" + dept.getDeptName() + "'失败，合同编号已存在");
//        }
//        else if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
//        {
//            return AjaxResult.error("新增合同'" + dept.getDeptName() + "'失败，合同名称已存在");
//        }
//        dept.setCreateBy(SecurityUtils.getUsername());

        contract.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());

        return toAjax(logisContractService.insertLogisContract(contract));
    }

    /**
     * 修改合同
     */
    @PreAuthorize("@ss.hasPermi('logis:contract:edit')")
    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody LogisContract contract)
    {
        System.out.println("update contract is " + contract.toString());
//        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
//        {
//            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
//        }
//        else if (dept.getParentId().equals(dept.getDeptId()))
//        {
//            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
//        }
//        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
//                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0)
//        {
//            return AjaxResult.error("该部门包含未停用的子部门！");
//        }
//        dept.setUpdateBy(SecurityUtils.getUsername());
        contract.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        return toAjax(logisContractService.updateLogisContract(contract));
    }

    /**
     * 合同文件上传
     */
    @Log(title = "合同文件上传", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("docfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String filepath = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
//            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
//            {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("imgUrl", avatar);
//                // 更新缓存用户头像
//                loginUser.getUser().setAvatar(avatar);
//                tokenService.setLoginUser(loginUser);
//                return ajax;
//            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }

    /**
     * 删除合同
     */
    @PreAuthorize("@ss.hasPermi('logis:contract:remove')")
    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{contractId}")
    public AjaxResult remove(@PathVariable Long contractId)
    {
//        if (deptService.hasChildByDeptId(deptId))
//        {
//            return AjaxResult.error("存在下级部门,不允许删除");
//        }
//        if (deptService.checkDeptExistUser(deptId))
//        {
//            return AjaxResult.error("部门存在用户,不允许删除");
//        }
        return toAjax(1);
    }
}

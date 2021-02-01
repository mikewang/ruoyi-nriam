package com.ruoyi.web.controller.logis;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
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
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.logis.domain.LogisContract;
import com.ruoyi.logis.service.LogisContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
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

    @Resource
    private ServerConfig serverConfig;

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
    @PreAuthorize("@ss.hasPermi('logis:contract:edit')")
    @Log(title = "合同文件上传", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws IOException
    {
        logger.debug("file getOriginalFilename is " + file.getOriginalFilename());
        try
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String originalFilename = file.getOriginalFilename();
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() + "/contract";
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.uploadOriginalFile(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("name", originalFilename);
            ajax.put("url", fileName);

            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage() + " 上传文件异常，请联系管理员");
        }
    }

    /**
     * 合同文件下载
     */
    @PreAuthorize("@ss.hasPermi('logis:contract:edit')")
    @Log(title = "合同文件下载", businessType = BusinessType.EXPORT)
    @GetMapping("/download")
    public void download(@RequestParam("file") String resource, HttpServletResponse response, HttpServletRequest request) throws IOException
    {
        try
        {
            logger.debug(resource);

            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            logger.debug("localPath is " + localPath);

            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            logger.debug("downloadPath is " + downloadPath);

//            File file = new File(downloadPath);
//            Files.copy(file.toPath(), response.getOutputStream());
//            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//            String contentDisposition = String.format("attachment; filename=%s", file.getName());
//            int fileSize = Long.valueOf(file.length()).intValue();
//            response.setContentType(mimeType);
//            response.setHeader("Content-Disposition", contentDisposition);
//            response.setContentLength(fileSize);
//
//            logger.debug("downloadPath fileSize is " + Long.valueOf(file.length()).toString());

            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());

        }
        catch (Exception e)
        {
            logger.error("下载文件失败", e);
        }
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

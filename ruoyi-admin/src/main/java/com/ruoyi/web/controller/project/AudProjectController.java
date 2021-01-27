package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.service.AudProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/project/zaiyan")
public class AudProjectController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private AudProjectService projectService;


    @PreAuthorize("@ss.hasPermi('project:zaiyan:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudProject project) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectAudProjectZaiYanList(project);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AudProject project) {

        return AjaxResult.success("");
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AudProject project) {
        return AjaxResult.success("");
    }


    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:zaiyan:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectids}")
    public AjaxResult remove(@PathVariable Integer[] projectids) {
//         logger.debug 只有一个参数，后面都不显示，但不报错。

        return AjaxResult.success("");
    }

    /**
     * 文件上传
     */
    @PreAuthorize("@ss.hasPermi('project:zaiyan:edit')")
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
            String filePath = RuoYiConfig.getUploadPath() + "/Doc";
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


}
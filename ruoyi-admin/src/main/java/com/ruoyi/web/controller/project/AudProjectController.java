package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.service.AudProjectService;
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('project:zaiyan:query')")
    @GetMapping(value = { "/", "/{projectId}" })
    public AjaxResult getProject(@PathVariable(value = "projectId", required = false) Integer projectId)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(projectId))
        {
            ajax.put(AjaxResult.DATA_TAG, projectService.selectProjectById(projectId));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:query')")
    @GetMapping( "/check")
    public AjaxResult getIfDuplicate(AudProject project)
    {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters getProjectid is " + project.getProjectid().toString() );
        logger.debug("query parameters getSubjectcode is " + project.getSubjectcode() );

        Integer projectid = project.getProjectid();

        String subjectcode = project.getSubjectcode();

        if (StringUtils.isNotNull(subjectcode))
        {
            ajax.put(AjaxResult.DATA_TAG, projectService.queryIfDuplicate(project));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AudProject project) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        project.setCreateuserid(Math.toIntExact(userId));

        Integer status = project.getStatus();
        logger.debug("project.getStatus is " + status.toString());

        if (StringUtils.isNotEmpty(project.getSubjectcode())
                && UserConstants.NOT_UNIQUE.equals(projectService.queryIfDuplicate(project)))
        {
            return AjaxResult.error("操作'" + project.getProjectname() + "'失败，项目经费编号\n已存在");
        }

        if (status == ProjectStatus.XinJianZhong.getCode()) {

        }
        else if (status == ProjectStatus.XinJianZhong.getCode()) {

        }
        else {
            return AjaxResult.error(" 状态异常，请联系管理员");
        }

        Integer projectid = projectService.insertProject(project);

        if (projectid > 0 ) {
            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, projectid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('project:zaiyan:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AudProject project) {

        AudProject undoProject = projectService.selectProjectById(project.getProjectid());

        if (StringUtils.isNotEmpty(project.getSubjectcode()) &&undoProject.getProjectid() != project.getProjectid()) {

            if (UserConstants.NOT_UNIQUE.equals(projectService.queryIfDuplicate(project)))
            {
                return AjaxResult.error("操作'" + project.getProjectname() + "'失败，项目经费编号\n已存在");
            }
        }


        Integer res = projectService.updateProject(project);


        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

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
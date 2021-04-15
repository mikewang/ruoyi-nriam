package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.MID;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
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
import com.ruoyi.project.domain.PmProjectjoinorganization;
import com.ruoyi.project.service.*;
import com.ruoyi.system.domain.SysUserMenu;
import io.swagger.models.auth.In;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class AudProjectController extends BaseController {

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private TokenService tokenService;

    @Resource
    private AudProjectService projectService;
    @Resource
    private PmProjectjoinorganizationService projectjoinorganizationService;

    @Resource
    private PmUplevelprojectService uplevelprojectService;

    @Resource
    private PMProjectMemberService projectMemberService;

    @Resource
    private AudProjectdocService projectdocService;

    @Resource
    private BasDocService basDocService;


    private Long getCurrentLoginUserId() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId;
    }

    ///Project/ProjectList.aspx 在研项目
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/project/list")
    public TableDataInfo zaiyanList(AudProject project) {

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectProjectNewList(project);

        //只对于admin和有"项目新建审核"权限的人显示执行期的提示
        Set<Long> useridSet = projectService.getConfirmUserIdList();

        logger.debug("getConfirmUserIdList  is " + useridSet.toString());

        for (AudProject record :list) {

            if (useridSet.contains(getCurrentLoginUserId())) {

                java.util.Date ended = DateUtils.getNowDate();

                try {
                    ended = DateUtils.parseDate(record.getProjectenddate());
                } catch (Exception e) {

                }

                long days = (ended.getTime() - DateUtils.getNowDate().getTime()) / (1000 * 24 * 60 * 60);


               // logger.debug(" days is " + Long.valueOf(days).toString());

                if (days < 0) {

                    record.setProjectDateRange(record.getProjectbegindate() + "至" + record.getProjectenddate() + "(已超 " + String.valueOf(Math.abs(days)) + "天)");
                    record.setProjectDateRangeColor(ProjectColor.Red.getCode());
                }
                else if (days < 90) {
                    record.setProjectDateRange(record.getProjectbegindate() + "至" + record.getProjectenddate() + "(剩余 " + String.valueOf(days) + "天)");
                    record.setProjectDateRangeColor(ProjectColor.Red.getCode());
                }
                else {

                }
            }

            List<Integer> statusList = new ArrayList<>();
            statusList.add(ProjectStatus.DaiQueRen.getCode());
            statusList.add(ProjectStatus.JieTiDaiQueRen.getCode());
            statusList.add(ProjectStatus.XinJianZhong.getCode());

            List<Integer> statusList2 = new ArrayList<>();
            statusList2.add(ProjectStatus.BuTongGuo.getCode());
            statusList2.add(ProjectStatus.JietiBuTongGuo.getCode());

            if (statusList.contains(record.getStatus()) || statusList2.contains(record.getStatus())) {
                if (statusList.contains(record.getStatus())) {
                    record.setProjectColor(ProjectColor.Red.getCode());
                }

                if (statusList2.contains(record.getStatus())) {
                    record.setProjectColor(ProjectColor.Green.getCode());
                }
            }

        }


        return getDataTable(list);
    }

    ///Project/ToConfirmProjectList.aspx 项目新建审核
    @PreAuthorize("@ss.hasPermi('project:toconfirm:list')")
    @GetMapping("/toconfirm/list")
    public TableDataInfo toconfirmList(AudProject project) {

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectProjectToconfirmList(project);

        return getDataTable(list);
    }


    ///Project/ToAcceptanceConfirmList.aspx 项目验收审核
    @PreAuthorize("@ss.hasPermi('project:toacceptanceconfirm:list')")
    @GetMapping("/toacceptanceconfirm/list")
    public TableDataInfo toacceptanceconfirmList(AudProject project) {

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectProjectToacceptanceconfirmList(project);

        return getDataTable(list);
    }


    ///Project/FinishedProjectList.aspx 项目已完成
    @PreAuthorize("@ss.hasPermi('project:finished:list')")
    @GetMapping("/finished/list")
    public TableDataInfo finishedList(AudProject project) {

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectProjectTofinishedList(project);

        return getDataTable(list);
    }


    ///Project/ToAddAcceptanceList.aspx 项目已完成,等待补充验收材料。
    @PreAuthorize("@ss.hasPermi('project:toaddacceptance:list')")
    @GetMapping("/toaddacceptance/list")
    public TableDataInfo toaddacceptanceList(AudProject project) {

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectProjectToaddacceptanceList(project);

        return getDataTable(list);
    }

    //外拨款管理模块使用
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/aftersetup/list")
    public TableDataInfo aftersetupList(AudProject project) {

        if (project.getProjectname() == "") {
            project.setProjectname("%");
        }

        logger.debug("query parameters is " + project.getProjectyear());

        startPage();

        List<AudProject> list = projectService.selectProjectAfterSetup(project);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/aftersetup/query")
    public AjaxResult queryAftersetup(AudProject project) {
        AjaxResult ajax = AjaxResult.success();

        if (project.getProjectname() == "") {
            project.setProjectname("%");
        }

        List<AudProject> list = projectService.selectProjectAfterSetup(project);

        ajax.put(AjaxResult.DATA_TAG, list);

        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping(value = { "/{projectid}" })
    public AjaxResult getProject(@PathVariable(value = "projectid", required = false) Integer projectid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(projectid))
        {
            ajax.put(AjaxResult.DATA_TAG, projectService.selectProjectById(projectid));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping( "/unique")
    public AjaxResult getIfDuplicate(AudProject project)
    {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters getSubjectcode is " + project.getSubjectcode() );

        Integer projectid = project.getProjectid();

        String subjectcode = project.getSubjectcode();

        if (StringUtils.isNotNull(subjectcode))
        {
            ajax.put(AjaxResult.DATA_TAG, projectService.queryIfDuplicate(project));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AudProject project) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        project.setCreateuserid(userId.intValue());

        Integer status = project.getStatus();
        logger.debug("project.getStatus is " + status.toString());

        if (StringUtils.isNotEmpty(project.getSubjectcode())
                && UserConstants.NOT_UNIQUE.equals(projectService.queryIfDuplicate(project)))
        {
            return AjaxResult.error("操作'" + project.getProjectname() + "'失败，项目经费编号已存在");
        }

        project.setStatus(ProjectStatus.XinJianZhong.getCode());
        Integer rows = projectService.insertProject(project);

        if (rows > 0 ) {
            Integer projectid = project.getProjectid();

             AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, projectid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AudProject project) {

        AudProject undoProject = projectService.selectProjectById(project.getProjectid());

        if (StringUtils.isNotEmpty(project.getSubjectcode()) &&undoProject.getSubjectcode() != project.getSubjectcode()) {

            if (UserConstants.NOT_UNIQUE.equals(projectService.queryIfDuplicate(project)))
            {
                return AjaxResult.error("操作'" + project.getProjectname() + "'失败，项目经费编号已存在");
            }
        }
        Integer status = project.getStatus();
        logger.debug("project.getStatus is " + status.toString());

        Integer res = projectService.updateProject(project);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping( "/xinjianzhong")
    public AjaxResult updateStatus(@Validated @RequestBody  AudProject project) {

        Integer res = projectService.xinjianzhongProject(project);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/normal/list")
    public TableDataInfo normalList(AudProject project) {

        logger.debug("query parameters is " + project.toString());

        startPage();

        List<AudProject> list = projectService.selectProjectList(project);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/normal/query")
    public AjaxResult queryNormal(AudProject project) {

        logger.debug("query parameters is " + project.getProjectyear());

        AjaxResult ajax = AjaxResult.success();

        List<AudProject> list = projectService.selectProjectList(project);

        ajax.put(AjaxResult.DATA_TAG, list);

        return ajax;
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectids}")
    public AjaxResult remove(@PathVariable Integer[] projectids) {
//         logger.debug 只有一个参数，后面都不显示，但不报错。

        return AjaxResult.success("");
    }




    /**
     * 文件上传
     */
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
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

            String yyyymm = DateUtils.dateTimeNow("yyyyMM");
            String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");

            filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.uploadOriginalFile(filePath, file);
            String doctype = FileTypeUtils.getFileType(fileName);
            String url = serverConfig.getUrl() + fileName;

            BasDoc doc = new BasDoc();
            doc.setDocname(originalFilename);
            String relativepath = StringUtils.trimstart(filePath,RuoYiConfig.getProfile());
            relativepath = StringUtils.trimend(relativepath,"/");

            doc.setRelativepath(relativepath);
            doc.setDoctype(doctype);

            logger.debug("relativepath is " + filePath);
            logger.debug("originalFilename is " + originalFilename);
            logger.debug("doctype is " + doctype);

            Integer docid = basDocService.insertBasDoc(doc);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("name", originalFilename);
            ajax.put("url", docid);
            ajax.put("fileUrl", fileName);

            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage() + " 上传文件异常，请联系管理员");
        }
    }

    /**
     * 文件下载
     */
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目文件下载", businessType = BusinessType.EXPORT)
    @GetMapping("/download")
    public void download(@RequestParam("file") Integer fileid, HttpServletResponse response, HttpServletRequest request) throws IOException
    {
        try
        {
            BasDoc doc = basDocService.selectBasDocById(fileid);

            String resource = doc.getRelativepath() + "/" + doc.getDocname();

            logger.debug("resource is " + resource);

            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            logger.debug("localPath is " + localPath);

            // 数据库资源地址
            String downloadPath = localPath + resource;
            logger.debug("downloadPath is " + downloadPath);

            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, doc.getDocname());
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            logger.error("下载文件失败", e);
        }
    }

    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping("/doc/list")
    public AjaxResult doclist(AudProjectdoc query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        Integer projectid = query.getProjectid();
        String doctype = query.getDoctype();

        if (projectid != null) {
            List<AudProjectdoc> list = projectdocService.selectProjectdocList(query);
            logger.debug("selectProjectdocList is ", list.toString());
            ajax.put(AjaxResult.DATA_TAG, list);
        }

        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping("/doc")
    public AjaxResult addDoc(@Validated @RequestBody AudProjectdoc project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;


    }


    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping("/doc")
    public AjaxResult editDoc(@Validated @RequestBody  AudProjectdoc project) {

        AjaxResult ajax = AjaxResult.success();
        return ajax;

    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/doc/{projectids}")
    public AjaxResult removeDoc(@PathVariable Integer[] projectids) {

        return AjaxResult.success("");
    }


    @PreAuthorize("@ss.hasPermi('project:toconfirm:list')")
    @Log(title = "项目新建审核", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm")
    public AjaxResult confirm(@Validated @RequestBody  AudProject project) {

        if (project.getConfirmSubjectcode() != project.getSubjectcode()) {
            project.setSubjectcode(project.getConfirmSubjectcode());

            AudProject undoProject = projectService.selectProjectById(project.getProjectid());

            if (StringUtils.isNotEmpty(project.getSubjectcode()) &&undoProject.getSubjectcode() != project.getSubjectcode()) {

                if (UserConstants.NOT_UNIQUE.equals(projectService.queryIfDuplicate(project)))
                {
                    return AjaxResult.error("操作'" + project.getProjectname() + "'失败，项目经费编号已存在");
                }
            }
        }

        logger.debug("project is ",project.toString());

        Integer res = projectService.confirmProject(project);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    /**
     * 根据编号获取审核详细信息 或 验收审核详细信息。
     */
    @PreAuthorize("@ss.hasPermi('project:project:list')")
    @GetMapping(value = { "/confirm/{projectid}/{status}" })
    public AjaxResult getProjectConfirm(@PathVariable(value = "projectid") Integer projectid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(projectid))
        {
            ajax.put(AjaxResult.DATA_TAG, projectService.selectApplyByTypeAndRelatedID(projectid, status));
        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('project:toacceptanceconfirm:list')")
    @Log(title = "项目验收审核", businessType = BusinessType.UPDATE)
    @PutMapping("/acceptanceconfirm")
    public AjaxResult acceptanceconfirm(@Validated @RequestBody  AudProject project) {

        logger.debug("project is ",project.toString());

        Integer res = projectService.acceptanceconfirm(project);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }



}
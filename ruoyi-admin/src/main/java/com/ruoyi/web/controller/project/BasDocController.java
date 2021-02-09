package com.ruoyi.web.controller.project;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;

import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.project.service.BasDocService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    /**
     * 文件上传
     */
    @PreAuthorize("@ss.hasPermi('bas:doc:add')")
    @Log(title = "文件上传", businessType = BusinessType.IMPORT)
    @PostMapping("/upload/{type}")
    public AjaxResult upload(@PathVariable Integer type, @RequestParam("file") MultipartFile file) throws IOException
    {
        logger.debug("file getOriginalFilename is " + file.getOriginalFilename());
        try
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String originalFilename = file.getOriginalFilename();
            // 上传文件路径
            String filePath = "/Others";
            if (type == 1) {
                filePath = "/Doc";
                String yyyymm = DateUtils.dateTimeNow("yyyyMM");
                String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");

                filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;
            }
            else if (type == 2) {
                filePath = "/Achieve";
                String yyyymm = DateUtils.dateTimeNow("yyyyMM");
                String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");

                filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;
            }
            else if (type == 3) {
                filePath = "/signpic";
            }

            filePath = RuoYiConfig.getUploadPath() + filePath;

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
    @PreAuthorize("@ss.hasPermi('bas:doc:list')")
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

}
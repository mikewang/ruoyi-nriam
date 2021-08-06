package com.ruoyi.web.controller.sku;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.ImageConverter;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.service.BasDocService;
import com.ruoyi.sku.domain.SkuExport;
import com.ruoyi.sku.domain.SkuFile;
import com.ruoyi.sku.domain.SkuInfo;
import com.ruoyi.sku.domain.SkuPhoto;
import com.ruoyi.sku.service.SkuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private SkuService skuService;

    @Resource
    private BasDocService basDocService;

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    private Long getCurrentLoginUserId() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

//        userId = 87L; // 测试用户 changchun 。
        return userId;
    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping("/sku/list")
    public TableDataInfo skuList(SkuInfo skuInfo) {

        logger.debug("skuList is begin ");

        if (skuInfo.getDocidList() == null || skuInfo.getDocidList().size() == 0) {

            if (skuInfo.getPhotoSizeValues() == null || skuInfo.getPhotoSizeValues().size() == 0) {
                startPage();
                List<SkuInfo> skuInfos = skuService.selectSkuListWithFile(skuInfo);
                return getDataTable(skuInfos);
            } else {
                startPage();
                List<SkuInfo> skuInfos = skuService.selectSkuListWithFileByPhotoSizeValue(skuInfo);
                return getDataTable(skuInfos);
            }
        } else {

            skuInfo.setSkuNames(new ArrayList<>());

            for (Integer docid : skuInfo.getDocidList()) {

                BasDoc basDoc = basDocService.selectBasDocById(docid);

                String fileName = RuoYiConfig.getProfile() + basDoc.getRelativepath() + "/" + basDoc.getDocname();

                logger.debug("read execel file is " + fileName);


                List<ExcelDataVO> rows = ExcelReader.readExcel(fileName);

                for (ExcelDataVO vo : rows) {

                    logger.debug(vo.getSkuName());

                    if (vo.getSkuName() != null) {
                        skuInfo.getSkuNames().add(vo.getSkuName().trim());
                    }
                }
            }

            if (skuInfo.getPhotoSizeValues() == null || skuInfo.getPhotoSizeValues().size() == 0) {
                startPage();
                List<SkuInfo> skuInfos = skuService.batchSelectSkuListWithFiles(skuInfo);
                return getDataTable(skuInfos);
            } else {
                startPage();
                List<SkuInfo> skuInfos = skuService.batchSelectSkuListWithFilesByPhotoSizeValue(skuInfo);
                return getDataTable(skuInfos);
            }
        }

    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping(value = {"/sku/{skuId}"})
    public AjaxResult getSku(@PathVariable(value = "skuId", required = true) Long skuId) {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(skuId)) {

            SkuInfo skuInfo = skuService.selectSkuInfoWithFilesById(skuId);

            // 本地资源路径
            String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload";

            for (SkuFile ff : skuInfo.getPhotoFileList()) {
                String url = fileDirPath + ff.getRelativepath() + "/" + ff.getFileName();
                logger.debug("getSku file url is " + url);
                ff.setUrl(url);
            }

            ajax.put(AjaxResult.DATA_TAG, skuInfo);
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

        Long rows = skuService.insertSkuInfoWithFiles(skuInfo);

        if (rows > 0) {
            Long skuId = skuInfo.getSkuId();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, skuId);
            return ajax;
        } else {
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

        Long rows = skuService.updateSkuInfoWithFiles(skuInfo);

        if (rows > 0) {
            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, 1);
            return ajax;
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/sku/{skuIds}")
    public AjaxResult remove(@PathVariable Long[] skuIds) {

        Integer rows = skuService.deleteSkuInfoBySkuIds(skuIds);

        if (rows > 0) {
            return AjaxResult.success("");
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping("/sku/unique")
    public AjaxResult getIfDuplicate(SkuInfo skuInfo) {
        AjaxResult ajax = AjaxResult.success();

        String skuName = skuInfo.getSkuName();

        if (StringUtils.isNotNull(skuName)) {
            ajax.put(AjaxResult.DATA_TAG, skuService.queryIfDuplicate(skuInfo));
        }
        return ajax;
    }


    /**
     * 文件上传
     */
//    @PreAuthorize("@ss.hasPermi('bas:doc:add')")
    @Log(title = "拓力图片上传", businessType = BusinessType.IMPORT)
    @PostMapping("/upload/{photoSizeValue}")
    public AjaxResult upload(@PathVariable String photoSizeValue, @RequestParam("file") MultipartFile file) throws IOException {
        logger.debug("file getOriginalFilename is " + file.getOriginalFilename());
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String originalFilename = file.getOriginalFilename();
            // 上传文件路径
            String relativepath = "/TuoliPhoto";
            String yyyymm = DateUtils.dateTimeNow("yyyyMM");
            String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");

            relativepath = relativepath + "/" + yyyymm + "/" + ddHHmmss;


            String filePath = RuoYiConfig.getUploadPath() + relativepath;

            // 上传并返回新文件名称
            String fileName = FileUploadUtils.uploadOriginalFile(filePath, file);
            String filetype = FileTypeUtils.getFileType(fileName);

            SkuFile skuFile = new SkuFile();
            skuFile.setFileName(originalFilename);
            skuFile.setFileType(filetype);
            skuFile.setPhotoSizeValue(photoSizeValue);
            skuFile.setCreated(new Date());

            skuFile.setRelativepath(relativepath);

            logger.debug("relativepath is " + relativepath);
            logger.debug("originalFilename is " + originalFilename);
            logger.debug("filetype is " + filetype);
            logger.debug("photoSizeValue is " + photoSizeValue);

            Long fileId = skuService.insertSkuFile(skuFile);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", originalFilename);
            ajax.put("relativepath", relativepath);
            ajax.put("fileId", fileId);

            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage() + " 上传图片文件异常，请联系管理员");
        }
    }


    /**
     * 文件下载
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.EXPORT)
    @GetMapping("/sku/exportbase64")
    public void exportSkuBase64(SkuInfo skuInfo, HttpServletResponse response) throws IOException {
        logger.debug("exportSku is " + skuInfo.toString());
        try {

            //文件路径
            String basicPath = RuoYiConfig.getDownloadPath() + "/Export";

            String yyyymmdd = DateUtils.dateTimeNow("yyyyMMdd");
            String HHmmss = DateUtils.dateTimeNow("HHmmss");

            basicPath = basicPath + "/" + yyyymmdd + "/" + HHmmss;

            String exportfilePath = basicPath + "/" + getCurrentLoginUserId().toString();

            Files.createDirectories(Paths.get(exportfilePath));


            if (skuInfo.getDocidList() == null || skuInfo.getDocidList().size() == 0) {
                skuInfo.setSkuNames(new ArrayList<>());
            } else {
                skuInfo.setSkuNames(new ArrayList<>());

                for (Integer docid : skuInfo.getDocidList()) {

                    BasDoc basDoc = basDocService.selectBasDocById(docid);

                    String fileName = RuoYiConfig.getProfile() + basDoc.getRelativepath() + "/" + basDoc.getDocname();

                    logger.debug("read execel file is " + fileName);


                    List<ExcelDataVO> rows = ExcelReader.readExcel(fileName);

                    for (ExcelDataVO vo : rows) {

                        logger.debug(vo.getSkuName());

                        if (vo.getSkuName() != null) {
                            skuInfo.getSkuNames().add(vo.getSkuName().trim());
                        }
                    }
                }
            }

            // export select
            if (skuInfo.getPhotoSizeValues() == null || skuInfo.getPhotoSizeValues().size() == 0) {

                List<SkuInfo> skuInfos = skuService.exportSkuList(skuInfo);

                for (SkuInfo skuInfo1 : skuInfos) {

                    for (SkuPhoto photo : skuInfo1.getPhotoList()) {

                        if (photo.getPhotoText() == null) {
                            continue;
                        }

                        Files.createDirectories(Paths.get(exportfilePath + "/" + photo.getPhotoSizeLabel()));

                        String base64String = photo.getPhotoText();

                        String[] parts = base64String.split(",");
                        String imageString = parts[1];
                        String fileformatter = parts[0];

                        String ext = fileformatter.split("/")[1].split(";")[0];

                        String fileName = skuInfo1.getSkuId().toString() + "_" + photo.getPhotoSizeValue() + "." + ext;
                        fileName = skuInfo1.getSkuName() + "." + ext;

                        String imagefileName = exportfilePath + "/" + photo.getPhotoSizeLabel() + "/" + fileName;

                        ImageConverter.ConvertBase64ToImageFile(imageString, imagefileName);
                    }

                }
            } else {

                logger.info("photo size here " + skuInfo.getPhotoSizeValues().toString());

                List<SkuInfo> skuInfos = skuService.exportSkuListByPhotoSizeValue(skuInfo);

                for (SkuInfo skuInfo1 : skuInfos) {

                    for (SkuPhoto photo : skuInfo1.getPhotoList()) {

                        if (photo.getPhotoText() == null) {
                            continue;
                        }

                        if (skuInfo.getPhotoSizeValues().contains(photo.getPhotoSizeValue()) == false) {
                            continue;
                        }

                        Files.createDirectories(Paths.get(exportfilePath + "/" + photo.getPhotoSizeLabel()));

                        String base64String = photo.getPhotoText();

                        String[] parts = base64String.split(",");
                        String imageString = parts[1];
                        String fileformatter = parts[0];

                        String ext = fileformatter.split("/")[1].split(";")[0];

                        String fileName = skuInfo1.getSkuId().toString() + "_" + photo.getPhotoSizeValue() + "." + ext;
                        fileName = skuInfo1.getSkuName() + "." + ext;

                        String imagefileName = exportfilePath + "/" + photo.getPhotoSizeLabel() + "/" + fileName;

                        ImageConverter.ConvertBase64ToImageFile(imageString, imagefileName);
                    }
                }
            }


            String downloadFile = basicPath + "/" + getCurrentLoginUserId().toString() + ".zip";

            ImageConverter.pack(exportfilePath, downloadFile);

            try {
                // path是指欲下载的文件的路径。
                File file = new File(downloadFile);
                // 取得文件名。
                String filename = file.getName();
                logger.debug("download filename is " + filename);
                // 取得文件的后缀名。
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(downloadFile));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();

                SkuExport export = new SkuExport();
                export.setExportPath(exportfilePath);
                export.setStatus(1);
                export.setExportTime(new Date());
                export.setUserId(getCurrentLoginUserId());
                skuService.insertSkuExport(export);

            } catch (Exception e) {
                logger.error("读取文件标签失败", e);
            }

        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }

    }

    @PreAuthorize("@ss.hasPermi('sku:export:list')")
    @Log(title = "导出记录", businessType = BusinessType.EXPORT)
    @GetMapping("/downloadBase64/{exportIds}")
    public void downloadSkuBase64Export(@PathVariable Long[] exportIds, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {

            List<Long> ids = Arrays.asList(exportIds);

            SkuExport export = skuService.selectSkuExportById(ids.get(0));
            //文件路径
            String downloadFile = export.getExportPath() + ".zip";
            logger.debug("downloadFile is " + downloadFile);

            try {
                // path是指欲下载的文件的路径。
                File file = new File(downloadFile);
                // 取得文件名。
                String filename = file.getName();
                logger.debug("download filename is " + filename);
                // 取得文件的后缀名。
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(downloadFile));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();

            } catch (Exception e) {
                logger.error("读取文件标签失败", e);
            }

        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }

    }


    @PreAuthorize("@ss.hasPermi('sku:export:list')")
    @GetMapping("/export/list")
    public TableDataInfo skuExportList(SkuExport skuExport) {

        logger.debug("skuExportList is " + skuExport.toString());
        startPage();

        List<SkuExport> list = skuService.selectSkuExport(skuExport);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sku:export:list')")
    @Log(title = "导出记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/export/{exportIds}")
    public AjaxResult removeExport(@PathVariable Long[] exportIds) throws IOException {

        for (Long id : exportIds) {

            SkuExport export = skuService.selectSkuExportById(id);
            logger.debug("delete disk file at " + export.getExportPath() + "/" + export.getUserId().toString() + ".zip");

            FileUtils.deleteFile(export.getExportPath() + ".zip");

            FileUtils.deleteDirectory(new File(export.getExportPath()));

        }

        Integer rows = skuService.deleteSkuExportByExportIds(exportIds);

        if (rows > 0) {
            return AjaxResult.success("");
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    /**
     * 文件下载
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.EXPORT)
    @GetMapping("/sku/export")
    public void exportSku(SkuInfo querySku, HttpServletResponse response) throws IOException {
        logger.debug("exportSku is " + querySku.toString());
        try {

            //文件路径
            String basicPath = RuoYiConfig.getDownloadPath() + "/Export";

            String yyyymmdd = DateUtils.dateTimeNow("yyyyMMdd");
            String HHmmss = DateUtils.dateTimeNow("HHmmss");

            basicPath = basicPath + "/" + yyyymmdd + "/" + HHmmss;

            String exportfilePath = basicPath + "/" + getCurrentLoginUserId().toString();

            Files.createDirectories(Paths.get(exportfilePath));


            if (querySku.getDocidList() == null || querySku.getDocidList().size() == 0) {
                querySku.setSkuNames(new ArrayList<>());
            } else {
                querySku.setSkuNames(new ArrayList<>());

                for (Integer docid : querySku.getDocidList()) {

                    BasDoc basDoc = basDocService.selectBasDocById(docid);

                    String fileName = RuoYiConfig.getProfile() + basDoc.getRelativepath() + "/" + basDoc.getDocname();

                    logger.debug("read execel file is " + fileName);


                    List<ExcelDataVO> rows = ExcelReader.readExcel(fileName);

                    for (ExcelDataVO vo : rows) {

                        logger.debug(vo.getSkuName());

                        if (vo.getSkuName() != null) {
                            querySku.getSkuNames().add(vo.getSkuName().trim());
                        }
                    }
                }
            }

            // export select
            if (querySku.getPhotoSizeValues() == null || querySku.getPhotoSizeValues().size() == 0) {

                List<SkuInfo> skuInfos = skuService.exportSkuListWithFiles(querySku);

                for (SkuInfo skuInfo1 : skuInfos) {

                    for (SkuFile file : skuInfo1.getPhotoFileList()) {

                        if (file.getFileName() == null) {
                            continue;
                        }

                        Files.createDirectories(Paths.get(exportfilePath + "/" + file.getPhotoSizeLabel()));

                        String ext = file.getFileType();

                        String fileName = skuInfo1.getSkuName() + "." + ext;

                        String imagefileName = exportfilePath + "/" + file.getPhotoSizeLabel() + "/" + fileName;

                        File dest = FileUtils.getFile(imagefileName);

                        String sourcefilePath = RuoYiConfig.getUploadPath() + file.getRelativepath() + "/" + file.getFileName();
                        File source = FileUtils.getFile(sourcefilePath);

                        FileUtils.copyFile(source, dest);

                    }

                }
            } else {
                List<SkuInfo> skuInfos = skuService.exportSkuListWithFilesByPhotoSizeValue(querySku);

                for (SkuInfo skuInfo1 : skuInfos) {

                    for (SkuFile file : skuInfo1.getPhotoFileList()) {

                        if (file.getFileName() == null) {
                            continue;
                        }

                        if (querySku.getPhotoSizeValues().contains(file.getPhotoSizeValue()) == false) {
                            continue;
                        }
                        Files.createDirectories(Paths.get(exportfilePath + "/" + file.getPhotoSizeLabel()));

                        String ext = file.getFileType();

                        String fileName = skuInfo1.getSkuName() + "." + ext;

                        String imagefileName = exportfilePath + "/" + file.getPhotoSizeLabel() + "/" + fileName;

                        File dest = FileUtils.getFile(imagefileName);

                        String sourcefilePath = RuoYiConfig.getUploadPath() + file.getRelativepath() + "/" + file.getFileName();
                        File source = FileUtils.getFile(sourcefilePath);

                        FileUtils.copyFile(source, dest);

                    }
                }
            }

            String downloadFilePath = basicPath + "/" + getCurrentLoginUserId().toString() + ".zip";

            ImageConverter.pack(exportfilePath, downloadFilePath);

            SkuExport export = new SkuExport();
            export.setExportPath(exportfilePath);
            export.setStatus(1);
            export.setExportTime(new Date());
            export.setUserId(getCurrentLoginUserId());
            skuService.insertSkuExport(export);

            // 开始下载（断点下载）
            downloadFileDuandian(downloadFilePath);


        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }
    }

    private void downloadFileDuandian(String downloadFilePath) {
        try {

            File downloadFile = new File(downloadFilePath);

            ServletContext context = request.getServletContext();
            // get MIME type of the file
            String mimeType = context.getMimeType(downloadFilePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            // response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
            long downloadSize = downloadFile.length();
            long fromPos = 0, toPos = 0;
            if (request.getHeader("Range") == null) {
                response.setHeader("Content-Length", downloadSize + "");
            } else {
                // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String range = request.getHeader("Range");
                String bytes = range.replaceAll("bytes=", "");
                String[] ary = bytes.split("-");
                fromPos = Long.parseLong(ary[0]);
                if (ary.length == 2) {
                    toPos = Long.parseLong(ary[1]);
                }
                int size;
                if (toPos > fromPos) {
                    size = (int) (toPos - fromPos);
                } else {
                    size = (int) (downloadSize - fromPos);
                }
                response.setHeader("Content-Length", size + "");
                downloadSize = size;
            }
            // Copy the stream to the response's output stream.
            RandomAccessFile in = null;
            OutputStream out = null;
            try {
                in = new RandomAccessFile(downloadFile, "rw");
                // 设置下载起始位置
                if (fromPos > 0) {
                    in.seek(fromPos);
                }
                // 缓冲区大小
                int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
                byte[] buffer = new byte[bufLen];
                int num;
                int count = 0; // 当前写到客户端的大小
                out = response.getOutputStream();
                while ((num = in.read(buffer)) != -1) {
                    out.write(buffer, 0, num);
                    count += num;
                    //处理最后一段，计算不满缓冲区的大小
                    if (downloadSize - count < bufLen) {
                        bufLen = (int) (downloadSize-count);
                        if(bufLen==0){
                            break;
                        }
                        buffer = new byte[bufLen];
                    }
                }
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != out) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(String downloadFilePath){
        try {
            // path是指欲下载的文件的路径。
            File file = new File(downloadFilePath);
            // 取得文件名。
            String filename = file.getName();
            logger.debug("download filename is " + filename);
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(downloadFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            // 解析断点续传相关信息
            response.setHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();

        } catch (Exception e) {
            logger.error("读取文件标签失败", e);
        }

    }

    @PreAuthorize("@ss.hasPermi('sku:export:list')")
    @Log(title = "导出记录", businessType = BusinessType.EXPORT)
    @GetMapping("/download/{exportIds}")
    public void downloadSkuExport(@PathVariable Long[] exportIds) throws IOException {
        try {

            List<Long> ids = Arrays.asList(exportIds);

            SkuExport export = skuService.selectSkuExportById(ids.get(0));
            //文件路径
            String downloadFilePath = export.getExportPath() + ".zip";
            logger.debug("downloadFile is " + downloadFilePath);

            downloadFileDuandian(downloadFilePath);

        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }

    }

}


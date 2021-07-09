package com.ruoyi.web.controller.sku;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.ImageConverter;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.service.BasDocService;
import com.ruoyi.sku.domain.SkuExport;
import com.ruoyi.sku.domain.SkuInfo;
import com.ruoyi.sku.domain.SkuPhoto;
import com.ruoyi.sku.service.SkuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

//    @Resource
//    private HttpServletResponse response;

    private Long getCurrentLoginUserId() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

//        userId = 87L; // 测试用户 changchun 。
        return userId;
    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping("/sku/list")
    public TableDataInfo skuList(SkuInfo skuInfo) {

        logger.debug("skuList is begin " );

        if (skuInfo.getDocidList() == null || skuInfo.getDocidList().size() == 0) {

            if (skuInfo.getPhotoSizeValues() == null || skuInfo.getPhotoSizeValues().size() == 0) {
                startPage();
                List<SkuInfo> skuInfos = skuService.selectSkuList(skuInfo);
                return getDataTable(skuInfos);
            }
            else {
                startPage();
                List<SkuInfo> skuInfos = skuService.selectSkuListByPhotoSizeValue(skuInfo);
                return getDataTable(skuInfos);
            }
        }
        else {

            skuInfo.setSkuNames(new ArrayList<>());

            for (Integer docid : skuInfo.getDocidList()) {

                BasDoc basDoc = basDocService.selectBasDocById(docid);

                String fileName = RuoYiConfig.getProfile() + basDoc.getRelativepath() + "/" + basDoc.getDocname();

                logger.debug("read execel file is " +  fileName);


                List<ExcelDataVO> rows  =  ExcelReader.readExcel(fileName);

                for (ExcelDataVO vo : rows) {

                    logger.debug(vo.getSkuName());

                    if (vo.getSkuName() != null) {
                        skuInfo.getSkuNames().add(vo.getSkuName().trim());
                    }
                }
            }

            if (skuInfo.getPhotoSizeValues() == null || skuInfo.getPhotoSizeValues().size() == 0) {
                startPage();
                List<SkuInfo> skuInfos = skuService.batchSelectSkuList(skuInfo);
                return getDataTable(skuInfos);
            }
            else {
                startPage();
                List<SkuInfo> skuInfos = skuService.batchSelectSkuListByPhotoSizeValue(skuInfo);
                return getDataTable(skuInfos);
            }
        }

    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping(value = {"/sku/{skuId}"})
    public AjaxResult getSku(@PathVariable(value = "skuId", required = false) Long skuId) {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(skuId)) {
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

        Long rows = skuService.updateSkuInfo(skuInfo);

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
     * 文件下载
     */
    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.EXPORT)
    @GetMapping("/sku/export")
    public void exportSku(SkuInfo skuInfo, HttpServletResponse response) throws IOException {
        logger.debug("exportSku is " + skuInfo.toString());
        try {

            //文件路径
            String basicPath = RuoYiConfig.getDownloadPath() + "/Export";

            String yyyymmdd = DateUtils.dateTimeNow("yyyyMMdd");
            String HHmmss = DateUtils.dateTimeNow("HHmmss");

            basicPath = basicPath + "/" + yyyymmdd + "/" + HHmmss ;

            String exportfilePath = basicPath + "/" + getCurrentLoginUserId().toString();

            Files.createDirectories(Paths.get(exportfilePath));


            if (skuInfo.getDocidList() == null || skuInfo.getDocidList().size() == 0) {
                skuInfo.setSkuNames(new ArrayList<>());
            }
            else {
                skuInfo.setSkuNames(new ArrayList<>());

                for (Integer docid : skuInfo.getDocidList()) {

                    BasDoc basDoc = basDocService.selectBasDocById(docid);

                    String fileName = RuoYiConfig.getProfile() + basDoc.getRelativepath() + "/" + basDoc.getDocname();

                    logger.debug("read execel file is " +  fileName);


                    List<ExcelDataVO> rows  =  ExcelReader.readExcel(fileName);

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
    @GetMapping("/download/{exportIds}")
    public void downloadSkuExport(@PathVariable Long[] exportIds, HttpServletResponse response, HttpServletRequest request) throws IOException {
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
            logger.debug("delete disk file at " + export.getExportPath()+"/"+export.getUserId().toString()+".zip");

            FileUtils.deleteFile(export.getExportPath()+".zip");

            FileUtils.deleteDirectory(new File(export.getExportPath()));

        }

        Integer rows = skuService.deleteSkuExportByExportIds(exportIds);

        if (rows > 0) {
            return AjaxResult.success("");
        } else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


}
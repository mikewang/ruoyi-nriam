package com.ruoyi.web.controller.sku;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.ImageConverter;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.sku.domain.SkuInfo;
import com.ruoyi.sku.service.SkuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private SkuService skuService;

    private Long getCurrentLoginUserId() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

//        userId = 87L; // 测试用户 changchun 。
        return userId;
    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @GetMapping("/sku/list")
    public TableDataInfo skuList(SkuInfo skuInfo) {

        startPage();

        List<SkuInfo> list = skuService.selectSkuList(skuInfo);

        return getDataTable(list);
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
    @GetMapping("/export2/{skuIds}")
    public void exportSku(@PathVariable Long[] skuIds, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {

            //文件路径
            String filePath = RuoYiConfig.getDownloadPath() + "Export";

//            String yyyymm = DateUtils.dateTimeNow("yyyyMM");
//            String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");
//
//            filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;


            String fileName = "a.jpeg";

            SkuInfo skuInfo = skuService.selectSkuInfoById(Long.valueOf(1005));

            String base64String = skuInfo.getPhotoList().get(0).getPhotoText();

            String downloadPath = filePath + "/" + fileName;
            logger.debug("downloadpath is " + downloadPath);

            // tokenize the data
            String[] parts = base64String.split(",");
            String imageString = parts[1];

            ImageConverter.ConvertBase64ToImageFile(imageString, downloadPath);

            // 读到流中
            InputStream inStream = new FileInputStream(downloadPath);// 文件的存放路径
            // 设置输出的格式
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            try {
                while ((len = inStream.read(b)) > 0)
                    response.getOutputStream().write(b, 0, len);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("下载文件失败 " + e.toString());
        }

    }

    @PreAuthorize("@ss.hasPermi('sku:sku:list')")
    @Log(title = "SKU管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export/{skuIds}")
    public void exportSku2(@PathVariable Long[] skuIds, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {

            //文件路径
            String filePath = RuoYiConfig.getDownloadPath() + "/Export";

//            String yyyymm = DateUtils.dateTimeNow("yyyyMM");
//            String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");
//
//            filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;

            String fileName = "a.jpg";

            SkuInfo skuInfo = skuService.selectSkuInfoById(Long.valueOf(1005));

            String base64String = skuInfo.getPhotoList().get(0).getPhotoText();

            String[] parts = base64String.split(",");
            String imageString = parts[1];

            String downloadPath = filePath + "/" + fileName;
            ImageConverter.ConvertBase64ToImageFile(imageString, downloadPath);


            try {
                // path是指欲下载的文件的路径。
                File file = new File(downloadPath);
                // 取得文件名。
                String filename = file.getName();
                // 取得文件的后缀名。
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(downloadPath));
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


}
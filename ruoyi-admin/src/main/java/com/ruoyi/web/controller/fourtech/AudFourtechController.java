package com.ruoyi.web.controller.fourtech;

import com.ruoyi.api.service.AppClientinfoService;
import com.ruoyi.audit.domain.AudSheetauditrecord;
import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.service.AudSheetauditrecordService;
import com.ruoyi.audit.service.AudSignpicService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SheetStatus;
import com.ruoyi.common.utils.ConvertUpMoney;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.fourtech.domain.AudFourtech;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.service.BasDocService;
import com.ruoyi.fourtech.service.AudFourtechService;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fourtech")
public class AudFourtechController extends BaseController {
    @Resource
    private AudFourtechService fourtechService;
    @Resource
    private AppClientinfoService clientinfoService;
    @Resource
    private AudSignpicService audSignpicService;

    @Resource
    private AudSheetauditrecordService sheetauditrecordService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;


    private Integer getCurrentLoginUserid() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        userId = 87L; // 测试用户 changchun 。
        return userId.intValue();
    }

    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @GetMapping("/tijiaoren/list")
    public TableDataInfo tijiaorenList(AudFourtech query) {

        logger.debug("tijiaorenList query  is " + query.toString());

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);

        startPage();
        List<AudFourtech> list = fourtechService.selectFourtechTijiaoren(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @GetMapping(value = {"/{fourtechid}"})
    public AjaxResult getFourtech (@PathVariable(value = "fourtechid", required = false) Integer fourtechid) {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(fourtechid)) {
            logger.debug("fourtechid = " + fourtechid.toString());
            AudFourtech p = fourtechService.selectFourtechById(fourtechid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, p);
            } else {
                ajax = AjaxResult.error("fourtechid：" + fourtechid.toString() + " 不存在");
            }

        }
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @Log(title = "四技合同管理", businessType = BusinessType.INSERT)
    @PostMapping("/tijiaoren")
    public AjaxResult add(@Validated @RequestBody AudFourtech contract) {

        AjaxResult ajax = AjaxResult.success();

        Integer userid = getCurrentLoginUserid();

        contract.setSheetuserid(userid);
        contract.setSheettime(DateUtils.dateTimeNow());

        Integer status = contract.getSheetstatus();
        logger.debug("AudFourtech .getSheetstatus is " + status.toString());

        contract.setSheetstatus(SheetStatus.XinJianZhong.getCode());
        logger.debug("AudFourtech add is " + contract.toString());

        contract.setDaxie(ConvertUpMoney.toChinese(contract.getFourtechmoney().toString()));

        Integer result = fourtechService.addFourtechTijiaoren(contract);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, contract.getFourtechid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @Log(title = "四技合同管理", businessType = BusinessType.UPDATE)
    @PutMapping("/tijiaoren")
    public AjaxResult update(@Validated @RequestBody AudFourtech contract) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("AudFourtech update is " + contract.toString());

        contract.setDaxie(ConvertUpMoney.toChinese(contract.getFourtechmoney().toString()));
        Integer result = fourtechService.updateFourtechTijiaoren(contract);

        if (result > 0) {
            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @GetMapping(value = {"/doc/list/{fourtechid}"})
    public AjaxResult getFourtechDocList (@PathVariable(value = "fourtechid", required = false) Integer fourtechid) {
        logger.debug("doclist is " + fourtechid.toString());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        BasDoc query = new BasDoc();
        query.setRelatedid(fourtechid);
        query.setAttachtotype("四技合同");

        List<BasDoc> list = fourtechService.selectFourtechDocList(query);
        logger.debug("selectFourtechDocList is ", list.toString());
        ajax.put(AjaxResult.DATA_TAG, list);

        return ajax;
    }

    /**
     * 文件上传
     */
    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
    @PostMapping("/doc/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file, @RequestParam("contractid") Integer contractid, @RequestParam("name") String name) throws IOException {
        logger.debug("file getOriginalFilename is " + file.getOriginalFilename() + " name is " + name);
        try {
            String originalFilename = file.getOriginalFilename();
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() + "/Doc";

            String yyyymm = DateUtils.dateTimeNow("yyyyMM");
            String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");

            filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;

            // 上传完成 并返回新文件名称
            String pathFileName = FileUploadUtils.uploadOriginalFile(filePath, file);

            String url = serverConfig.getUrl() + pathFileName;

            String docType = FileTypeUtils.getFileType(pathFileName);

            BasDoc doc = new BasDoc();
            doc.setDocname(originalFilename);
            String relativepath = StringUtils.trimstart(filePath, RuoYiConfig.getProfile());
            relativepath = StringUtils.trimend(relativepath, "/");

            doc.setRelativepath(relativepath);
            doc.setAttachtotype("四技合同");
            doc.setRelatedid(contractid);
            doc.setDoctype("四技合同");

            logger.debug("relativepath is " + filePath);
            logger.debug("originalFilename is " + originalFilename);
            logger.debug("attachToType is " + "合同文本，这个字段 不用来关联，有单独的关联表");
            logger.debug("docType is " + docType);

            Integer docid = fourtechService.mergeFourtechDoc(contractid, doc);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("name", originalFilename);
            ajax.put("url", docid);
            ajax.put("fileUrl", pathFileName);

            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage() + " 上传文件异常，请联系管理员");
        }
    }

    /**
     * 文件下载
     */
    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @Log(title = "模板文件下载", businessType = BusinessType.EXPORT)
    @GetMapping("/template/download")
    public void download(@RequestParam("contractid") Integer contractid, HttpServletResponse response, HttpServletRequest request) throws IOException
    {
        try
        {
            String filename = "";

            AudFourtech contract = fourtechService.selectFourtechById(contractid);

            filename = contract.getFourtechtype();

            // 本地资源路径
            String localPath = RuoYiConfig.getDownloadPath();
            logger.debug("localPath is " + localPath);

            String resource = "Contract/Template/";

            filename = filename + ".doc";
            resource = resource + filename;

            resource = localPath + resource;

            if (!FileUtils.checkAllowDownload(resource) || !FileUtils.getFile(resource).exists())
            {
                throw new Exception(StringUtils.format("合同文件模板({})非法，不允许下载或不存在。 ", resource));
            }

            // 数据库资源地址
            String downloadPath = resource;
            logger.debug("downloadPath is " + downloadPath);

            File f = FileUtils.getFile(downloadPath);

            //File f = new File(downloadPath);
            if(f.exists() && !f.isDirectory()) {
                logger.debug("downloadPath file is existed. " + downloadPath);
            }
            else {
                logger.debug("downloadPath file is not  existed. " + downloadPath);
            }

            String path = downloadPath;
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            logger.debug("downloadName is " + downloadName);


            try {
                String buffer = "";

                if (path.endsWith(".doc")){
                    InputStream is = new FileInputStream(new File(path));

                    HWPFDocument document = new HWPFDocument(is);

                    for (Integer i = 0; i < document.getBookmarks().getBookmarksCount(); i++) {
                        Bookmark bookmark =  document.getBookmarks().getBookmark(i);

                        logger.debug("书签" + (i+1) + "的名称是：" + bookmark.getName());
                        logger.debug("开始位置：" + bookmark.getStart());
                        logger.debug("结束位置：" + bookmark.getEnd());

                    }

                    Map<String,String> dataMap = new HashMap<String,String >();
                    dataMap.put("title", contract.getFourtechname());

                    if (contract.getFourtechcode() != null) {
                        dataMap.put("contractcode", contract.getFourtechcode());
                    }
                    else {
                        dataMap.put("contractcode", "");
                    }

                    dataMap.put("fengmian_projectName", contract.getFourtechname());
                    dataMap.put("fengmian_coperationUnit", contract.getCoperationunit());
                    dataMap.put("projectname", contract.getFourtechname());
                    // 代替图片，方法不确认，待确认。


                    logger.debug("dataMap is " + dataMap.toString());

                    for (String  key : dataMap.keySet()) {
                        for(int i = 0; i < document.getBookmarks().getBookmarksCount(); i++){
                            Bookmark bookmark = document.getBookmarks().getBookmark(i);
                            if(bookmark.getName().equals(key)){
                                logger.debug("替代书签" + (i+1) + "的名称 key 是：" + bookmark.getName());
                                Range range = new Range(bookmark.getStart(),bookmark.getEnd(),document);
                                range.insertBefore(dataMap.get(key));
                                //range.insertAfter(dataMap.get(key));
                                break;
                            }
                        }
                    }

                    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                    FileUtils.setAttachmentResponseHeader(response, contract.getFourtechname());
                    document.write(response.getOutputStream());

                    logger.debug("此文件 bookmarks count = " + document.getBookmarks().getBookmarksCount());

                }else if (path.endsWith(".docx")){
                    // 没有处理 docx格式的文件，。
                    FileInputStream fs = new FileInputStream(new File(path));
                    XWPFDocument xdoc = new XWPFDocument(fs);
                    XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                    buffer = extractor.getText();
                } else {
                    logger.debug("此文件不是word文件！" + downloadPath);
                }
            }
            catch (Exception e) {
                logger.error("读取文件标签失败", e);
            }



            //response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            //FileUtils.setAttachmentResponseHeader(response, downloadName);
            //FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            logger.error("下载文件失败", e);
        }
    }


    @PreAuthorize("@ss.hasPermi('fourtech:tijiaoren:list')")
    @Log(title = "四技合同管理", businessType = BusinessType.UPDATE)
    @PutMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody AudFourtech contract) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("AudContract 提交审批 is " + contract.toString());

        if (contract.getContractdocList().size() == 0) {

            return AjaxResult.error(" 还没有上传合同正文，无法提交审核");
        }

        contract.setSheetstatus(SheetStatus.ChuShen.getCode());

        Integer result = fourtechService.updateFourtechStatus(contract, null);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, contract.getFourtechid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    private String signpicFilename(String signpicName) {
        // 本地资源路径
        String result = "";
        String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload" + "/signpic/";
        if (signpicName != null && signpicName.isEmpty() == false) {
            String checkFilePath = RuoYiConfig.getUploadPath() + "/signpic/" + signpicName;
            File desc = new File(checkFilePath);
            if (desc.exists() == true) {
                String filePath = fileDirPath + signpicName;
                result = filePath;
                logger.debug("url is " + filePath);
            }
        }
        return result;
    }

    private AjaxResult auditConfirm(AudFourtech sheet, Integer audittype) {
        AjaxResult ajax = AjaxResult.success();
        Integer userid = getCurrentLoginUserid();
        sheet.setConfirmUserid(userid);

        AudSignpic s = audSignpicService.selectSignpicByUserId(sheet.getConfirmUserid());

        if (s == null || this.signpicFilename(s.getSignpicName()).equals("")) {
            return AjaxResult.error("您的签名图片尚未上传，无法审批！");
        }

        //记录审核结论   /////////////////////////记录审核结论，消除待办事项////////////////

        AudSheetauditrecord record = new AudSheetauditrecord();
        record.setSheetid(sheet.getFourtechid());
        record.setSheettype("四技合同"); // 名称为 合同。
        record.setAudittype(audittype.toString());
        record.setAuditopinion(sheet.getConfirmNote());
        record.setAudittime(DateUtils.dateTimeNow());
        record.setAudituserid(userid);

        if (sheet.getConfirmResult() == 1) {
            record.setAuditresult(true);
            if (audittype == 2) {
                sheet.setAudittype(audittype.toString());
                sheet.setSheetstatus(SheetStatus.XiangMuShenPi.getCode());
            }
            else if (audittype == 3) {
                sheet.setAudittype(audittype.toString());
                sheet.setSheetstatus(SheetStatus.BuMenShenPi.getCode());
            } else if (audittype == 4) {
                sheet.setAudittype(audittype.toString());
                sheet.setSheetstatus(SheetStatus.ChuShenPi.getCode());
            } else if (audittype == 5) {
                sheet.setAudittype(audittype.toString());
                sheet.setSheetstatus(SheetStatus.SuoZhangShenPi.getCode());
            } else if (audittype == 7) {
                sheet.setAudittype(audittype.toString());

                sheet.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
            }

            fourtechService.updateFourtechStatus(sheet, record);
        } else if (sheet.getConfirmResult() == 2) {
            record.setAuditresult(false);
            sheet.setSheetstatus(SheetStatus.NoPass.getCode());
            fourtechService.updateFourtechStatus(sheet, record);
        } else {
            return AjaxResult.error("审批'" + sheet.getFourtechname() + "'失败，没有选择审批结果");
        }


        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit2:list')")
    @GetMapping("/audit2/list")
    public TableDataInfo chushenList(AudFourtech query) {

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);
        logger.debug("chushenList list  is " + query.toString());
        startPage();
        List<AudFourtech> list = fourtechService.selectFourtechChushen(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit2:list')")
    @Log(title = "四技合同成果转换处审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit2")
    public AjaxResult chushenConfirm(@Validated @RequestBody AudFourtech contract) {

        logger.debug("audit2 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
         ajax = auditConfirm(contract, 2);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit3:list')")
    @GetMapping("/audit3/list")
    public TableDataInfo xiangmuList(AudFourtech query) {

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);

        logger.debug("chushenList list  is " + query.toString());
        startPage();

        List<AudFourtech> list = fourtechService.selectFourtechXiangmu(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit3:list')")
    @Log(title = "四技合同项目审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit3")
    public AjaxResult xiangmuConfirm(@Validated @RequestBody AudFourtech contract) {

        logger.debug("audit3 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 3);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit4:list')")
    @GetMapping("/audit4/list")
    public TableDataInfo bumenList(AudFourtech query) {

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);
        logger.debug("bumenList list  is " + query.toString());
        startPage();
        List<AudFourtech> list = fourtechService.selectFourtechBumen(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit4:list')")
    @Log(title = "四技合同部门审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit4")
    public AjaxResult bumenConfirm(@Validated @RequestBody AudFourtech contract) {

        logger.debug("audit4 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 4);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit5:list')")
    @GetMapping("/audit5/list")
    public TableDataInfo chuList(AudFourtech query) {

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);
        logger.debug("chuList list  is " + query.toString());
        startPage();
        List<AudFourtech> list = fourtechService.selectFourtechChu(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit5:list')")
    @Log(title = "四技合同分管处审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit5")
    public AjaxResult chuConfirm(@Validated @RequestBody AudFourtech contract) {

        logger.debug("audit5 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 5);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit7:list')")
    @GetMapping("/audit7/list")
    public TableDataInfo suozhangList(AudFourtech query) {

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);
        logger.debug("suozhangList list  is " + query.toString());
        startPage();
        List<AudFourtech> list = fourtechService.selectFourtechSuozhang(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:audit7:list')")
    @Log(title = "四技合同所长审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit7")
    public AjaxResult suozhangConfirm(@Validated @RequestBody AudFourtech contract) {

        logger.debug("audit7 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 7);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('fourtech:myaudit:list')")
    @GetMapping("/myaudit/list")
    public TableDataInfo myauditrecordList() {

        Integer uid = getCurrentLoginUserid();

        startPage();

        List<AudFourtech> list = fourtechService.selectFourtechAuditedByUserid(uid);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:codefourtech:list')")
    @GetMapping("/ShenPiWanCheng/list")
    public TableDataInfo ShenPiWanChengList(AudFourtech query) {

        Integer uid = getCurrentLoginUserid();

        startPage();

        List<AudFourtech> list = fourtechService.selectFourtechShenPiWanCheng(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('fourtech:codefourtech:list')")
    @Log(title = "四技合同填写合同编号", businessType = BusinessType.UPDATE)
    @PutMapping("/codefourtech")
    public AjaxResult updateCodefourtech(@Validated @RequestBody AudFourtech contract) {

        logger.debug("updateCodefourtech contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();

        Integer result = fourtechService.updateFourtechCode(contract);

        if (result ==1 ) {
            return  ajax;
        }
        else {
            return AjaxResult.error("操作失败");
        }
    }


}
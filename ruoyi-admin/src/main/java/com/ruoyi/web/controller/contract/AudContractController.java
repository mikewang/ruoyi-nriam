package com.ruoyi.web.controller.contract;

import com.ruoyi.api.service.AppClientinfoService;
import com.ruoyi.audit.service.AudSignpicService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
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
import com.ruoyi.contract.domain.AudContract;
import com.ruoyi.contract.service.AudContractService;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProject;
import com.ruoyi.project.service.AudProjectService;
import com.ruoyi.project.service.BasDocService;
import com.ruoyi.system.service.ISysDictDataService;
import io.swagger.models.auth.In;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Bookmarks;
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
@RequestMapping("/contract")
public class AudContractController extends BaseController {
    @Resource
    private AudContractService contractService;

    @Resource
    private AppClientinfoService clientinfoService;
    @Resource
    private AudSignpicService audSignpicService;

    @Resource
    private BasDocService basDocService;

    @Resource
    private ISysDictDataService dictDataService;

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

    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @GetMapping("/tijiaoren/list")
    public TableDataInfo tijiaorenList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("tijiaorenList query  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractTijiaoren(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @Log(title = "合同管理", businessType = BusinessType.INSERT)
    @PostMapping("/tijiaoren")
    public AjaxResult add(@Validated @RequestBody AudContract contract) {

        AjaxResult ajax = AjaxResult.success();

        Integer userid = getCurrentLoginUserid();

        contract.setContractuserid(userid);
        contract.setContracttime(DateUtils.dateTimeNow());

        contract.setPayedtimes(0);

        Integer status = contract.getSheetstatus();
        logger.debug("sheet.getSheetstatus is " + status.toString());

        contract.setSheetstatus(SheetStatus.XinJianZhong.getCode());
        logger.debug("AudContract add is " + contract.toString());

        contract.setDaxie(ConvertUpMoney.toChinese(contract.getContractmoney().toString()));
        contract.setOrganizationid(contract.getProjectinfo().getOrganizationid());

        Integer result = contractService.addContractTijiaoren(contract);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, contract.getContractid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @Log(title = "合同管理", businessType = BusinessType.INSERT)
    @PutMapping("/tijiaoren")
    public AjaxResult update(@Validated @RequestBody AudContract contract) {
        AjaxResult ajax = AjaxResult.success();

//        Integer userid = getCurrentLoginUserid();
//
//        contract.setContractuserid(userid);
//        contract.setContracttime(DateUtils.dateTimeNow());
//
//        Integer status = contract.getSheetstatus();
//        logger.debug("sheet.getSheetstatus is " + status.toString());
//
//        contract.setSheetstatus(SheetStatus.XinJianZhong.getCode());
//        logger.debug("AudContract add is " + contract.toString());


        contract.setOrganizationid(contract.getProjectinfo().getOrganizationid());
        contract.setDaxie(ConvertUpMoney.toChinese(contract.getContractmoney().toString()));
        Integer result = contractService.updateContractTijiaoren(contract);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, contract.getContractid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    /**
     * 文件上传
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
    @PostMapping("/contractdoc/upload")
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
            String fileName = FileUploadUtils.uploadOriginalFile(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            String docType = FileTypeUtils.getFileType(fileName);

            BasDoc doc = new BasDoc();
            doc.setDocname(originalFilename);
            String relativepath = StringUtils.trimstart(filePath, RuoYiConfig.getProfile());
            relativepath = StringUtils.trimend(relativepath, "/");

            doc.setRelativepath(relativepath);
            doc.setAttachtotype("");
            doc.setDoctype(docType);

            logger.debug("relativepath is " + filePath);
            logger.debug("originalFilename is " + originalFilename);
            logger.debug("attachToType is " + "合同文本，这个字段 不用来关联，有单独的关联表");
            logger.debug("docType is " + docType);

            Integer docid = contractService.mergeContractDoc(contractid, doc);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("name", originalFilename);
            ajax.put("url", docid);
            ajax.put("fileUrl", fileName);

            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage() + " 上传文件异常，请联系管理员");
        }
    }

    /**
     * 文件下载
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @Log(title = "模板文件下载", businessType = BusinessType.EXPORT)
    @GetMapping("/template/download")
    public void download(@RequestParam("contractid") Integer contractid, HttpServletResponse response, HttpServletRequest request) throws IOException
    {
        try
        {
            String filename = "";

            AudContract contract = contractService.selectContractById(contractid);

            filename = contract.getContracttypelinktext();

//            SysDictData data = new SysDictData();
//            data.setDictValue(fileid.toString());
//            data.setDictType("合同类型");
//            List<SysDictData> list = dictDataService.selectDictDataList(data);
//            for (SysDictData contractData : list) {
//                filename = contractData.getDictLabel()+".doc";
//            }

            String resource = request.getSession().getServletContext().getRealPath("/doc/Contract/Template/");

            logger.debug("resource is " + resource);

            resource = "Contract/Template/";

            filename = filename + ".doc";
            resource = resource + filename;

            // 本地资源路径
            String localPath = RuoYiConfig.getDownloadPath();
            logger.debug("localPath is " + localPath);

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
                    dataMap.put("title", contract.getContractname());

                    if (contract.getContractcode() != null) {
                        dataMap.put("contractcode", contract.getContractcode());
                    }
                    else {
                        dataMap.put("contractcode", "");
                    }

                    dataMap.put("suppliername_cover", contract.getSupplieridlinktext());
                    dataMap.put("projecttype", contract.getProjectinfo().getProjectTypeLinkText());
                    dataMap.put("projectname", contract.getProjectinfo().getProjectname());
                    dataMap.put("suppliername_mudi", contract.getSupplierinfo().getSuppliername());
                    dataMap.put("suppliername_qianyan", contract.getSupplierinfo().getSuppliername());
                    dataMap.put("suppliername_gaizhang", contract.getSupplierinfo().getSuppliername());
                    dataMap.put("bankname", contract.getSupplierinfo().getBankname());
                    dataMap.put("banknumber", contract.getSupplierinfo().getBanknumber());

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
                     FileUtils.setAttachmentResponseHeader(response, contract.getContractname());
                    document.write(response.getOutputStream());

                    logger.debug("此文件 bookmarks count = " + document.getBookmarks().getBookmarksCount());

                }else if (path.endsWith(".docx")){
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

    /**
     * 根据编号获取详细信息
     */
//    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
//    @GetMapping(value = {"/{contractid}"})
//    public AjaxResult getSheet(@PathVariable(value = "contractid", required = false) Integer contractid) {
//        AjaxResult ajax = AjaxResult.success();
//
//        if (StringUtils.isNotNull(contractid)) {
//            logger.debug("contractid = " + contractid.toString());
//            AudFourtech p = contractService.selectFourtechById(contractid);
//            if (p != null) {
//                ajax.put(AjaxResult.DATA_TAG, p);
//            } else {
//                ajax = AjaxResult.error("contractid：" + contractid.toString() + " 不存在");
//            }
//
//        }
//        return ajax;
//    }

//    /**
//     * 根据编号,获取 明细 详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
//    @GetMapping(value = {"/budgetpay/{contractid}"})
//    public AjaxResult getSheetDetail(@PathVariable(value = "contractid", required = false) Integer contractid) {
//        AjaxResult ajax = AjaxResult.success();
//
//        if (StringUtils.isNotNull(contractid)) {
//            logger.debug("contractid = " + contractid.toString());
//            List<AudBudgetpay> plist = contractService.selectSheetBudgetPayById(contractid);
//            if (plist != null) {
//                ajax.put(AjaxResult.DATA_TAG, plist);
//            } else {
//                ajax = AjaxResult.error("contractid：" + contractid.toString() + " 不存在");
//            }
//
//        }
//        return ajax;
//    }

//    /**
//     * 根据编号 删除信息
//     */
//    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
//    @DeleteMapping(value = {"/{contractid}"})
//    public AjaxResult deleteSheet(@PathVariable(value = "contractid", required = false) Integer contractid) {
//        AjaxResult ajax = AjaxResult.success();
//
//        if (StringUtils.isNotNull(contractid)) {
//            logger.debug("contractid = " + contractid.toString());
//            AudFourtech sheet = contractService.selectSheetTijiaorenById(contractid);
//            if (sheet != null) {
//                logger.debug("sheet is " + sheet.toString());
//                Integer userid = getCurrentLoginUserid();
//                if (sheet.getSheetuserid().equals(userid)) {
//                    sheet.setSheetstatus(SheetStatus.YiZuoFei.getCode());
//                    contractService.updateSheetAuditStatus(sheet, null);
//                } else {
//                    ajax = AjaxResult.error("contractid：" + contractid.toString() + " 不存在");
//                }
//            } else {
//                ajax = AjaxResult.error("contractid：" + contractid.toString() + " 不存在");
//            }
//
//        }
//        return ajax;
//    }
//
//
//    /**
//     * 根据项目号，供应商号，获取 历史记录
//     */
//    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
//    @GetMapping(value = {"/budgetpay/record"})
//    public TableDataInfo getSheetBudgetpayRecord(AudBudgetpayRecordQuery query) {
//        startPage();
//        logger.debug("getSheetBudgetpayRecord query = " + query.toString());
//        List<AudBudgetpay> list = contractService.selectBudgetPayRecord(query);
//        //  logger.debug("getSheetBudgetpayRecord list = " + list.toString());
//        return getDataTable(list);
//    }
//
//    private String signpicFilename(String signpicName) {
//        // 本地资源路径
//        String result = "";
//        String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload" + "/signpic/";
//        if (signpicName != null && signpicName.isEmpty() == false) {
//            String checkFilePath = RuoYiConfig.getUploadPath() + "/signpic/" + signpicName;
//            File desc = new File(checkFilePath);
//            if (desc.exists() == true) {
//                String filePath = fileDirPath + signpicName;
//                result = filePath;
//                logger.debug("url is " + filePath);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 根据项目号，供应商号，获取 历史记录
//     */
//    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
//    @GetMapping(value = {"/audit/record"})
//    public AjaxResult getSheetAuditRecord(AudFourtechauditrecord record) {
//        AjaxResult ajax = AjaxResult.success();
//        logger.debug("getSheetAuditRecord 审批记录 = " + record.toString());
//        List<AudFourtechauditrecord> list = contractService.selectSheetauditRecord(record);
//
//        for (AudFourtechauditrecord s : list) {
//            String result = this.signpicFilename(s.getSignpicName());
//            s.setSignpicName(result);
//        }
//        logger.debug("getSheetAuditRecord 审批记录 = " + list.toString());
//        ajax.put(AjaxResult.DATA_TAG, list);
//        return ajax;
//    }
//

//
//    private AjaxResult auditConfirm(AudFourtech sheet, Integer audittype) {
//        AjaxResult ajax = AjaxResult.success();
//        Integer userid = getCurrentLoginUserid();
//        sheet.setConfirmUserid(userid);
//
//        AudSignpic s = audSignpicService.selectSignpicByUserId(sheet.getConfirmUserid());
//
//        if (s == null || this.signpicFilename(s.getSignpicName()).equals("")) {
//            return AjaxResult.error("您的签名图片尚未上传，无法审批！");
//        }
//
//        //记录审核结论
////        record.SheetID = int.Parse(Request["kid"].ToString());
////        record.SheetType = "拨付单";
////        record.AuditType = Request["t"].ToString();    //把当前的审批环节作为类型记录进去
////        record.AuditOpinion = AuditOpinion1.txt_Opinion.Text;
////        record.AuditResult = Convert.ToBoolean(int.Parse(AuditOpinion1.rbtnl_Result.SelectedValue));
////        record.AuditTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
////        record.AuditUserID = int.Parse(Session["CurrentUserID"].ToString());
//
//        AudFourtechauditrecord record = new AudFourtechauditrecord();
//        record.setSheetid(sheet.getSheetid());
//        record.setSheettype(sheet.getSheettype());
//        record.setAudittype(audittype.toString());
//        record.setAuditopinion(sheet.getConfirmNote());
//        record.setAudittime(DateUtils.dateTimeNow());
//        record.setAudituserid(userid);
//
//        if (sheet.getConfirmResult() == 1) {
//            record.setAuditresult(true);
//            if (audittype == 3) {
//                sheet.setAudittype(audittype.toString());
//                sheet.setSheetstatus(SheetStatus.BuMenShenPi.getCode());
//            } else if (audittype == 4) {
//                sheet.setAudittype(audittype.toString());
//                sheet.setSheetstatus(SheetStatus.ChuShenPi.getCode());
//            } else if (audittype == 5) {
//                sheet.setAudittype(audittype.toString());
//                sheet.setSheetstatus(SheetStatus.FenGuanSuoShenPi.getCode());
//            } else if (audittype == 6) {
//                sheet.setAudittype(audittype.toString());
//                int flag = sheet.getHejiZong().compareTo(BigDecimal.valueOf(50000L));
//                if (flag >= 0) {
//                    // //拨付单总金额大于5万，到所长审批
//                    sheet.setSheetstatus(SheetStatus.SuoZhangShenPi.getCode());
//                } else {
//                    sheet.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
//                }
//            } else if (audittype == 7) {
//                sheet.setAudittype(audittype.toString());
//
//                sheet.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
//            }
//
//            contractService.updateSheetAuditStatus(sheet, record);
//        } else if (sheet.getConfirmResult() == 2) {
//            record.setAuditresult(false);
//            sheet.setSheetstatus(SheetStatus.NoPass.getCode());
//            contractService.updateSheetAuditStatus(sheet, record);
//        } else {
//            return AjaxResult.error("审批'" + sheet.getSheetcode() + "'失败，没有选择审批结果");
//        }
//
//
//        return ajax;
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit3:list')")
//    @GetMapping("/audit3/list")
//    public TableDataInfo audit3List() {
//        Integer userid = getCurrentLoginUserid();
//        startPage();
//        List<AudFourtech> list = contractService.selectSheetXiangmuByUserid(userid);
//        return getDataTable(list);
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit3:list')")
//    @Log(title = "外拨款项目负责人审批", businessType = BusinessType.UPDATE)
//    @PutMapping("/audit3")
//    public AjaxResult audit3Confirm(@Validated @RequestBody AudFourtech sheet) {
//
//        logger.debug("audit3 sheet is " + sheet.toString());
//        AjaxResult ajax = AjaxResult.success();
//        ajax = auditConfirm(sheet, 3);
//
//        return ajax;
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit4:list')")
//    @GetMapping("/audit4/list")
//    public TableDataInfo audit4List() {
//        Integer userid = getCurrentLoginUserid();
//        startPage();
//        List<AudFourtech> list = contractService.selectSheetBuMenByUserid(userid);
//        return getDataTable(list);
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit4:list')")
//    @Log(title = "外拨款部门负责人审批", businessType = BusinessType.UPDATE)
//    @PutMapping("/audit4")
//    public AjaxResult audit4Confirm(@Validated @RequestBody AudFourtech sheet) {
//        AjaxResult ajax = AjaxResult.success();
//        ajax = auditConfirm(sheet, 4);
//        return ajax;
//    }
//
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit5:list')")
//    @GetMapping("/audit5/list")
//    public TableDataInfo audit5List() {
//        Integer userid = getCurrentLoginUserid();
//        startPage();
//        List<AudFourtech> list = contractService.selectSheetChuByUserid(userid);
//        return getDataTable(list);
//    }
//
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit5:list')")
//    @Log(title = "外拨款分管处的负责人审批", businessType = BusinessType.UPDATE)
//    @PutMapping("/audit5")
//    public AjaxResult audit5Confirm(@Validated @RequestBody AudFourtech sheet) {
//        AjaxResult ajax = AjaxResult.success();
//        ajax = auditConfirm(sheet, 5);
//        return ajax;
//    }
//
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit6:list')")
//    @GetMapping("/audit6/list")
//    public TableDataInfo audit6List() {
//        Integer userid = getCurrentLoginUserid();
//        startPage();
//        List<AudFourtech> list = contractService.selectSheetFenguansuoByUserid(userid);
//        return getDataTable(list);
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit6:list')")
//    @Log(title = "外拨款分管所长审批", businessType = BusinessType.UPDATE)
//    @PutMapping("/audit6")
//    public AjaxResult audit6Confirm(@Validated @RequestBody AudFourtech sheet) {
//        AjaxResult ajax = AjaxResult.success();
//        ajax = auditConfirm(sheet, 6);
//        return ajax;
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit7:list')")
//    @GetMapping("/audit7/list")
//    public TableDataInfo audit7List() {
//        Integer userid = getCurrentLoginUserid();
//        startPage();
//        List<AudFourtech> list = contractService.selectSheetSuozhangByUserid(userid);
//        return getDataTable(list);
//    }
//
//    @PreAuthorize("@ss.hasPermi('sheet:audit7:list')")
//    @Log(title = "外拨款所长审批", businessType = BusinessType.UPDATE)
//    @PutMapping("/audit7")
//    public AjaxResult audit7Confirm(@Validated @RequestBody AudFourtech sheet) {
//        AjaxResult ajax = AjaxResult.success();
//        ajax = auditConfirm(sheet, 7);
//        return ajax;
//    }
//
//
//    /**
//     * 文件上传
//     */
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
//    @PostMapping("/supplier/upload")
//    public AjaxResult upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("attachToType") String attachToType, @RequestParam("docType") String docType) throws IOException {
//        logger.debug("file getOriginalFilename is " + file.getOriginalFilename() + " name is " + name);
//        try {
//            String originalFilename = file.getOriginalFilename();
//            // 上传文件路径
//            String filePath = RuoYiConfig.getUploadPath() + "/Doc";
//
//            String yyyymm = DateUtils.dateTimeNow("yyyyMM");
//            String ddHHmmss = DateUtils.dateTimeNow("ddHHmmss");
//
//            filePath = filePath + "/" + yyyymm + "/" + ddHHmmss;
//
//            // 上传并返回新文件名称
//            String fileName = FileUploadUtils.uploadOriginalFile(filePath, file);
//            String url = serverConfig.getUrl() + fileName;
//
//            BasDoc doc = new BasDoc();
//            doc.setDocname(originalFilename);
//            String relativepath = StringUtils.trimstart(filePath, RuoYiConfig.getProfile());
//            relativepath = StringUtils.trimend(relativepath, "/");
//
//            doc.setRelativepath(relativepath);
//            doc.setAttachtotype(attachToType);
//            doc.setDoctype(docType);
//
//            logger.debug("relativepath is " + filePath);
//            logger.debug("originalFilename is " + originalFilename);
//            logger.debug("attachToType is " + attachToType);
//            logger.debug("doctype is " + docType);
//
//            Integer docid = basDocService.insertBasDoc(doc);
//
//            AjaxResult ajax = AjaxResult.success();
//            ajax.put("name", originalFilename);
//            ajax.put("url", docid);
//            ajax.put("fileUrl", fileName);
//
//            return ajax;
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage() + " 上传文件异常，请联系管理员");
//        }
//    }
//
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @Log(title = "协作单位管理", businessType = BusinessType.INSERT)
//    @PostMapping("/supplier")
//    public AjaxResult addSupplier(@Validated @RequestBody SrmSupplierinfo supplier) {
//
//        Integer userid = getCurrentLoginUserid();
//        supplier.setCreateuserid(userid);
//
//        supplier.setIfaudited(false);
//        supplier.setIfdeleted(false);
//
//        String organizationcode = supplier.getOrganizationcode();
//
//        SrmSupplierinfo ss = contractService.selectSupplierInfoByOrganizationcode(organizationcode);
//
//        if (ss != null) {
//            return AjaxResult.error("组织代码重复，操作失败，请联系管理员");
//        }
//
//
//        Integer result = contractService.addSupplierinfo(supplier);
//
//        if (result > 0) {
//
//            return AjaxResult.success(" 提交成功");
//        } else {
//
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
//
//    }
//
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @Log(title = "协作单位管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/supplier")
//    public AjaxResult updateSupplier(@Validated @RequestBody SrmSupplierinfo supplier) {
//
//        logger.debug("updateSupplier is " + supplier.toString());
//
//        String organizationcode = supplier.getOrganizationcode();
//
//        SrmSupplierinfo ss = contractService.selectSupplierInfoByOrganizationcode(organizationcode);
//
//        if (ss != null && ss.getSupplierid().equals(supplier.getSupplierid()) == false) {
//            return AjaxResult.error("组织代码重复，操作失败，请联系管理员");
//        }
//
//        Integer result = contractService.updateSupplierinfo(supplier);
//
//        if (result > 0) {
//
//            return AjaxResult.success(" 提交成功");
//        } else {
//
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
//
//    }
//
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @Log(title = "协作单位管理", businessType = BusinessType.DELETE)
//    @DeleteMapping("/supplier/{supplierids}")
//    public AjaxResult deleteSupplier(@PathVariable Integer[] supplierids) {
//
//        logger.debug("deleteSupplier is " + supplierids.toString());
//
//        List<Integer> ids = new ArrayList<>();
//        for (Integer id : supplierids) {
//            ids.add(id);
//        }
//
//        Integer result = contractService.deleteSupplierinfo(ids);
//
//        if (result > 0) {
//
//            return AjaxResult.success(" 删除成功");
//        } else {
//
//            return AjaxResult.error(" 操作失败，请联系管理员");
//        }
//
//    }
//
//
//    /**
//     * 根据 获取 协作单位，即供应商的列表
//     */
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @GetMapping(value = {"/supplier/filter"})
//    public AjaxResult getSheetSupplier(SrmSupplierinfo query) {
//        AjaxResult ajax = AjaxResult.success();
//        logger.debug("SrmSupplierinfo = " + query.toString());
//        query.setIfdeleted(false);
//        List<SrmSupplierinfo> plist = contractService.selectSupplierInfoList(query);
//        if (plist != null) {
//            ajax.put(AjaxResult.DATA_TAG, plist);
//        } else {
//            ajax = AjaxResult.error("SrmSupplierinfo：" + query.getSuppliername() + " 不存在");
//        }
//        return ajax;
//    }
//
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @GetMapping("/supplier/list")
//    public TableDataInfo supplierList(SrmSupplierinfo query) {
//        logger.debug("query  is " + query.toString());
//
//        startPage();
//        query.setIfdeleted(false);
//        List<SrmSupplierinfo> list = contractService.selectSupplierInfoList(query);
//
//        return getDataTable(list);
//    }
//
//    /**
//     * 根据 获取 协作单位，即供应商的列表
//     */
//    @PreAuthorize("@ss.hasPermi('srm:supplier:list')")
//    @GetMapping(value = {"/supplier/{supplierid}", "/supplier/"})
//    public AjaxResult getSheetSupplierById(@PathVariable(value = "supplierid", required = false) Integer supplierid) {
//        AjaxResult ajax = AjaxResult.success();
//
//        Integer userid = getCurrentLoginUserid();
//
//        SrmSupplierinfo supplierinfo;
//
//        if (StringUtils.isNotNull(supplierid)) {
//            supplierinfo = contractService.selectSupplierInfoById(supplierid);
//
//            BasDoc record = new BasDoc();
//            record.setAttachtotype("协作单位");
//            record.setDoctype("营业执照");
//            record.setRelatedid(supplierinfo.getSupplierid());
//
//            for (BasDoc doc : basDocService.selectBasDocList(record)) {
//                String filename = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + doc.getRelativepath() + "/" +doc.getDocname();
//                supplierinfo.setOrgImgId(doc.getDocid());
//                supplierinfo.setOrgImgIdText(filename);
//            }
//            record.setDoctype("其它附件");
//
//            List<DocFile> list = new ArrayList<>();
//
//            for (BasDoc doc : basDocService.selectBasDocList(record)) {
//                String filename = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + doc.getRelativepath() + "/" +doc.getDocname();
//                DocFile map = new DocFile();
//                map.setName(doc.getDocname());
//                map.setUrl(doc.getDocid());
//                list.add(map);
//            }
//
//            supplierinfo.setDocList(list);
//
//            logger.debug("supplierinfo getDocList is " + supplierinfo.getDocList());
//
//        } else {
//            supplierinfo = new SrmSupplierinfo();
//            supplierinfo.setCreateuserid(userid);
//        }
//        ajax.put(AjaxResult.DATA_TAG, supplierinfo);
//
//        return ajax;
//    }
//


}
package com.ruoyi.web.controller.contract;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruoyi.audit.domain.AudSheet;
import com.ruoyi.audit.domain.AudSheetauditrecord;
import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.service.AudSheetService;
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
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.contract.domain.AudContract;
import com.ruoyi.contract.domain.AudContractdoc;
import com.ruoyi.contract.service.AudContractService;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;



@RestController
@RequestMapping("/contract")
public class AudContractController extends BaseController {
    @Resource
    private AudContractService contractService;

    @Resource
    private AudSheetService sheetService;

    @Resource
    private AudSignpicService audSignpicService;


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


    @PreAuthorize("@ss.hasPermi('audit:audit3:list')")
    @GetMapping("/audit3/list")
    public TableDataInfo xiangmuList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("xiangmu list  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractXiangmu(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('audit:audit3:list')")
    @Log(title = "合同项目负责人审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit3")
    public AjaxResult xiangmuConfirm(@Validated @RequestBody AudContract contract) throws IOException, WriterException {

        logger.debug("audit3 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 3);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('audit:audit4:list')")
    @GetMapping("/audit4/list")
    public TableDataInfo bumenList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("audit4 list  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractBumen(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('audit:audit4:list')")
    @Log(title = "合同部门审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit4")
    public AjaxResult bumenConfirm(@Validated @RequestBody AudContract contract) throws IOException, WriterException {

        logger.debug("audit4 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 4);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('audit:audit5:list')")
    @GetMapping("/audit5/list")
    public TableDataInfo chuList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("audit5 list  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractChu(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('audit:audit5:list')")
    @Log(title = "合同分管处审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit5")
    public AjaxResult chuConfirm(@Validated @RequestBody AudContract contract) throws IOException, WriterException {

        logger.debug("audit5 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 5);

        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('audit:audit6:list')")
    @GetMapping("/audit6/list")
    public TableDataInfo fenguansuoList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("audit6 list  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractFenguansuo(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('audit:audit6:list')")
    @Log(title = "合同分管所审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit6")
    public AjaxResult fengguansuoConfirm(@Validated @RequestBody AudContract contract) throws IOException, WriterException {

        logger.debug("audit6 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 6);

        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('audit:audit7:list')")
    @GetMapping("/audit7/list")
    public TableDataInfo suoList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("audit7 list  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractSuo(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('audit:audit7:list')")
    @Log(title = "合同所审批", businessType = BusinessType.UPDATE)
    @PutMapping("/audit7")
    public AjaxResult suoConfirm(@Validated @RequestBody AudContract contract) throws IOException, WriterException {

        logger.debug("audit7 contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 7);

        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('contract:applydelete:list')")
    @GetMapping("/applydelete/list")
    public TableDataInfo applydeleteList(AudContract query) {

        Integer uid = getCurrentLoginUserid();
        query.setContractuserid(uid);
        logger.debug("applydelete list  is " + query.toString());
        startPage();
        List<AudContract> list = contractService.selectContractApplyDelete(query);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('contract:applydelete:list')")
    @Log(title = "合同作废审批", businessType = BusinessType.UPDATE)
    @PutMapping("/applydelete")
    public AjaxResult applydeleteConfirm(@Validated @RequestBody AudContract contract) throws IOException, WriterException {

        logger.debug("applydelete Confirm contract is " + contract.toString());
        AjaxResult ajax = AjaxResult.success();
        ajax = auditConfirm(contract, 9);

        return ajax;
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

    private AjaxResult auditConfirm(AudContract contract, Integer audittype) throws IOException, WriterException {
        AjaxResult ajax = AjaxResult.success();
        Integer userid = getCurrentLoginUserid();
        contract.setConfirmUserid(userid);

        AudSignpic s = audSignpicService.selectSignpicByUserId(contract.getConfirmUserid());

        if (s == null || this.signpicFilename(s.getSignpicName()).equals("")) {
            return AjaxResult.error("您的签名图片尚未上传，无法审批！");
        }

        //记录审核结论   /////////////////////////记录审核结论，消除待办事项////////////////

        //记录审核结论
//        SheetAuditRecord record = new SheetAuditRecord();
//        record.SheetID = int.Parse(Request["kid"].ToString());
//        record.SheetType = "合同";
//        record.AuditType = Request["t"].ToString();    //把当前的审批环节作为类型记录进去
//        record.AuditOpinion = AuditOpinion1.txt_Opinion.Text;
//        record.AuditResult = Convert.ToBoolean(int.Parse(AuditOpinion1.rbtnl_Result.SelectedValue));
//        record.AuditTime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
//        record.AuditUserID = int.Parse(Session["CurrentUserID"].ToString());
//
//        ISheetAuditRecordManager irm = SheetAuditRecordManager.GetInstance();
//        irm.AddNew(record);

        AudSheetauditrecord record = new AudSheetauditrecord();
        record.setSheetid(contract.getContractid());
        record.setSheettype("合同"); // 名称为 合同。
        record.setAudittype(audittype.toString());
        record.setAuditopinion(contract.getConfirmNote());
        record.setAudittime(DateUtils.dateTimeNow());
        record.setAudituserid(userid);

        if (contract.getConfirmResult() == 1) {
            record.setAuditresult(true);
            if (audittype == 3) {
                contract.setAudittype(audittype.toString());
                contract.setSheetstatus(SheetStatus.BuMenShenPi.getCode());
            } else if (audittype == 4) {
                contract.setAudittype(audittype.toString());
                contract.setSheetstatus(SheetStatus.ChuShenPi.getCode());
            } else if (audittype == 5) {
                contract.setAudittype(audittype.toString());
                contract.setSheetstatus(SheetStatus.FenGuanSuoShenPi.getCode());
            } else if (audittype == 6) {
                contract.setAudittype(audittype.toString());
                int flag = contract.getContractmoney().compareTo(BigDecimal.valueOf(50000L));
                if (flag >= 0) {
                    // //拨付单总金额大于5万，到所长审批
                    contract.setSheetstatus(SheetStatus.SuoZhangShenPi.getCode());
                } else {
                    contract.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
                    //合同审批通过后，要改写合同正文文档，加上签名，加上二维码
                    shenpiwanchengProcessDoc(contract);
                }
            } else if (audittype == 7) {
                contract.setAudittype(audittype.toString());

                contract.setSheetstatus(SheetStatus.ShenPiWanCheng.getCode());
                //合同审批通过后，要改写合同正文文档，加上签名，加上二维码
                shenpiwanchengProcessDoc(contract);
            }

            contractService.updateAudContractStatus(contract, record);
        } else if (contract.getConfirmResult() == 2) {
            record.setAuditresult(false);
            contract.setSheetstatus(SheetStatus.NoPass.getCode());
            contractService.updateAudContractStatus(contract, record);
        } else {
            return AjaxResult.error("审批'" + contract.getContractname() + "'失败，没有选择审批结果");
        }

        return ajax;
    }

    //合同审批通过后，要改写合同正文文档，加上签名，加上二维码
    private void shenpiwanchengProcessDoc(AudContract contract) throws IOException, WriterException {

        String toEncodeString = contract.getContractcode() + "|" + contract.getContractmoney().toString() + "|" + contract.getSupplieridlinktext() + "|" + DateUtils.dateTimeNow("yyyyMMdd");

        String cryptLinkString = Md5Utils.hash(toEncodeString + Constants.HttpKey);

        toEncodeString = toEncodeString + "|" + cryptLinkString;

        String filePath = DateUtils.dateTimeNow("yyyyMMddHHmmssSSS") + ".jpg";

        filePath = RuoYiConfig.getProfile() + File.separator + "temp" + File.separator + filePath ;

        logger.debug("filePath is " + filePath);

        //Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

        //generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        //invoking the user-defined method that creates the QR code
        generateQRcode(toEncodeString, filePath, charset, hashMap, 200, 200);//increase or decrease height and width accodingly



    }

    private void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
        //the BitMatrix class represents the 2D matrix of bits
        //MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.

        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);

        MatrixToImageWriter.writeToPath(matrix,"jpg", Paths.get(path));

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
    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
    @PutMapping("/tijiaoren")
    public AjaxResult update(@Validated @RequestBody AudContract contract) {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("AudContract update is " + contract.toString());

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
            ajax.put("fileUrl", pathFileName);

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
    public void download(@RequestParam("contractid") Integer contractid, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
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

            //String resource = request.getSession().getServletContext().getRealPath("/doc/Contract/Template/");

            //logger.debug("resource is " + resource);

            // 本地资源路径
            String localPath = RuoYiConfig.getDownloadPath();
            logger.debug("localPath is " + localPath);

            String resource = "Contract/Template/";

            filename = filename + ".doc";
            resource = resource + filename;

            resource = localPath + resource;

            if (!FileUtils.checkAllowDownload(resource) || !FileUtils.getFile(resource).exists()) {
                throw new Exception(StringUtils.format("合同文件模板({})非法，不允许下载或不存在。 ", resource));
            }

            // 数据库资源地址
            String downloadPath = resource;
            logger.debug("downloadPath is " + downloadPath);

            File f = FileUtils.getFile(downloadPath);

            //File f = new File(downloadPath);
            if (f.exists() && !f.isDirectory()) {
                logger.debug("downloadPath file is existed. " + downloadPath);
            } else {
                logger.debug("downloadPath file is not  existed. " + downloadPath);
            }

            String path = downloadPath;
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            logger.debug("downloadName is " + downloadName);


            try {
                String buffer = "";

                if (path.endsWith(".doc")) {
                    InputStream is = new FileInputStream(new File(path));

                    HWPFDocument document = new HWPFDocument(is);

                    for (Integer i = 0; i < document.getBookmarks().getBookmarksCount(); i++) {
                        Bookmark bookmark = document.getBookmarks().getBookmark(i);

                        logger.debug("书签" + (i + 1) + "的名称是：" + bookmark.getName());
                        logger.debug("开始位置：" + bookmark.getStart());
                        logger.debug("结束位置：" + bookmark.getEnd());

                    }

                    Map<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("title", contract.getContractname());

                    if (contract.getContractcode() != null) {
                        dataMap.put("contractcode", contract.getContractcode());
                    } else {
                        dataMap.put("contractcode", "");
                    }

                    dataMap.put("suppliername_cover", contract.getSupplieridlinktext());
                    dataMap.put("projecttype", contract.getProjectinfo().getProjecttypelinktext());
                    dataMap.put("projectname", contract.getProjectinfo().getProjectname());
                    dataMap.put("suppliername_mudi", contract.getSupplierinfo().getSuppliername());
                    dataMap.put("suppliername_qianyan", contract.getSupplierinfo().getSuppliername());
                    dataMap.put("suppliername_gaizhang", contract.getSupplierinfo().getSuppliername());
                    dataMap.put("bankname", contract.getSupplierinfo().getBankname());
                    dataMap.put("banknumber", contract.getSupplierinfo().getBanknumber());

                    logger.debug("dataMap is " + dataMap.toString());

                    for (String key : dataMap.keySet()) {
                        for (int i = 0; i < document.getBookmarks().getBookmarksCount(); i++) {
                            Bookmark bookmark = document.getBookmarks().getBookmark(i);
                            if (bookmark.getName().equals(key)) {
                                logger.debug("替代书签" + (i + 1) + "的名称 key 是：" + bookmark.getName());
                                Range range = new Range(bookmark.getStart(), bookmark.getEnd(), document);
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

                } else if (path.endsWith(".docx")) {
                    FileInputStream fs = new FileInputStream(new File(path));
                    XWPFDocument xdoc = new XWPFDocument(fs);
                    XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                    buffer = extractor.getText();
                } else {
                    logger.debug("此文件不是word文件！" + downloadPath);
                }
            } catch (Exception e) {
                logger.error("读取文件标签失败", e);
            }

            //response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            //FileUtils.setAttachmentResponseHeader(response, downloadName);
            //FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }
    }

    /**
     * 文件列表
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @GetMapping("/doc/list")
    public AjaxResult doclist(AudContractdoc query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        AjaxResult ajax = AjaxResult.success();

        Integer contractid = query.getContractid();

        if (contractid != null) {
            List<AudContractdoc> list = contractService.selectAudContractdocList(query);
            logger.debug("selectAudContractdocList is ", list.toString());
            ajax.put(AjaxResult.DATA_TAG, list);
        }

        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @GetMapping(value = {"/{contractid}"})
    public AjaxResult getContract(@PathVariable(value = "contractid", required = false) Integer contractid) throws IOException, WriterException {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(contractid)) {
            logger.debug("getContract contractid = " + contractid.toString());
            AudContract p = contractService.selectContractById(contractid);
            if (p != null) {
                shenpiwanchengProcessDoc(p);
                ajax.put(AjaxResult.DATA_TAG, p);
            } else {
                ajax = AjaxResult.error("contractid：" + contractid.toString() + " 不存在");
            }

        }
        return ajax;
    }

    public String parseToHTML(String path) throws IOException, SAXException, TikaException {
        ContentHandler handler = new ToXMLContentHandler();

        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try (InputStream stream = new FileInputStream(new File(path));) {
            parser.parse(stream, handler, metadata);
            return handler.toString();
        }
    }

    public String parse2HTML(String path) throws IOException, ParserConfigurationException, TransformerException {
        InputStream input = new FileInputStream(path);
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
//        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
//            public String savePicture(byte[] content, PictureType pictureType,
//                                      String suggestedName, float widthInches, float heightInches) {
//                return suggestedName;
//            }
//        });
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                String type = pictureType.name();
                return "data:image/" + type + ";base64," + new String(Base64.encodeBase64(content));
            }

        });

        wordToHtmlConverter.processDocument(wordDocument);
//        List pics = wordDocument.getPicturesTable().getAllPictures();
//        if (pics != null) {
//            for (int i = 0; i < pics.size(); i++) {
//                Picture pic = (Picture) pics.get(i);
//                try {
//                    pic.writeImageContent(new FileOutputStream(path
//                            + pic.suggestFullFileName()));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        String content = new String(outStream.toByteArray());
        return content;
        //org.apache.commons.io.FileUtils.write(new File(path, "1.html"), content, "utf-8");
    }


    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
    @PutMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody AudContract contract) throws IOException, TikaException, SAXException, TransformerException, ParserConfigurationException {
        AjaxResult ajax = AjaxResult.success();
        logger.debug("AudContract 提交审批 is " + contract.toString());

        if (contract.getContractdocList().size() == 0) {

            return AjaxResult.error(" 还没有上传合同正文，无法提交审核");
        }

        AudContractdoc query = new AudContractdoc();
        query.setContractid(contract.getContractid());

        List<AudContractdoc> contractdocList = contractService.selectAudContractdocList(query);

        for (AudContractdoc doc : contractdocList) {
            String filePath = RuoYiConfig.getProfile() + doc.getRelativepath() + "/" + doc.getDocname();
            ;

            String htmlfileName = doc.getDocname().replaceFirst(doc.getDoctype(), "html");
            String htmlfilePath = RuoYiConfig.getProfile() + doc.getRelativepath() + "/" + htmlfileName;

            String htmlstr = parse2HTML(filePath);

            logger.debug("文档word转成html " + htmlstr);

            Files.write(Paths.get(htmlfilePath), htmlstr.getBytes());

        }


        contract.setSheetstatus(SheetStatus.XiangMuShenPi.getCode());

        Integer result = contractService.updateAudContractStatus(contract);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, contract.getContractid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }


    /**
     * 根据合同编号获取 合同拨付单
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @GetMapping(value = {"/paysheetlist/{contractid}"})
    public AjaxResult getPaysheetList(@PathVariable(value = "contractid", required = false) Integer contractid) {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(contractid)) {
            logger.debug("getContract contractid = " + contractid.toString());
            List<AudSheet> p = contractService.selectContractPaySheetByContractid(contractid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, p);
            } else {
                ajax = AjaxResult.error("contractid：" + contractid.toString() + " 不存在");
            }

        }
        return ajax;
    }

    /**
     * 根据拨付单编号获取 合同拨付单
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @GetMapping(value = {"/paysheet/{sheetid}"})
    public AjaxResult getPaysheet(@PathVariable(value = "sheetid", required = false) Integer sheetid) {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(sheetid)) {
            logger.debug("getContract paysheet sheetid = " + sheetid.toString());
            AudSheet p = sheetService.selectSheetTijiaorenById(sheetid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, p);
            } else {
                ajax = AjaxResult.error("sheetid：" + sheetid.toString() + " 不存在");
            }

        }
        return ajax;
    }


    /**
     * 根据合同编号获取 合同拨付单
     */
    @PreAuthorize("@ss.hasPermi('contract:tijiaoren:list')")
    @GetMapping(value = {"/doc/{contractid}"})
    public AjaxResult getContractdocHtml(@PathVariable(value = "contractid", required = false) Integer contractid) throws IOException {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(contractid)) {
            logger.debug("getContract contractid = " + contractid.toString());

            AudContractdoc query = new AudContractdoc();
            query.setContractid(contractid);

            List<AudContractdoc> contractdocList = contractService.selectAudContractdocList(query);

            String html = "";

            for (AudContractdoc doc : contractdocList) {
                String htmlfileName = doc.getDocname().replaceFirst(doc.getDoctype(), "html");
                String htmlfilePath = RuoYiConfig.getProfile() + doc.getRelativepath() + "/" + htmlfileName;

                html = new String(Files.readAllBytes(Paths.get(htmlfilePath)));

            }

            if (html.equals("")) {
                ajax = AjaxResult.error("合同文本为空。");
            } else {
                ajax.put(AjaxResult.DATA_TAG, html);

            }

        }
        return ajax;
    }


}
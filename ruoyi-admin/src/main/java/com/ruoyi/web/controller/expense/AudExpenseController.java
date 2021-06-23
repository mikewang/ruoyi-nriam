package com.ruoyi.web.controller.expense;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruoyi.audit.domain.AudApply;
import com.ruoyi.audit.domain.AudSheet;
import com.ruoyi.audit.domain.AudSheetauditrecord;
import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.service.AudApplyService;
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
import com.ruoyi.expense.domain.AudExpense;
import com.ruoyi.expense.service.AudExpenseService;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.service.BasDocService;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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


@RestController
@RequestMapping("/expense")
public class AudExpenseController extends BaseController {

    @Resource
    private AudExpenseService expenseService;

    @Resource
    private AudContractService contractService;

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
    public TableDataInfo tijiaorenList(AudExpense query) {

        Integer uid = getCurrentLoginUserid();
        query.setSheetuserid(uid);
        logger.debug("tijiaorenList query  is " + query.toString());
        startPage();
        List<AudExpense> list = expenseService.selectExpenseTijiaoren(query);

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

    private AjaxResult auditConfirm(AudContract contract, Integer audittype) throws IOException, WriterException {
        AjaxResult ajax = AjaxResult.success();
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('expense:tijiaoren:list')")
    @Log(title = "小额经费管理", businessType = BusinessType.INSERT)
    @PostMapping("/tijiaoren")
    public AjaxResult add(@Validated @RequestBody AudExpense expense) {

        AjaxResult ajax = AjaxResult.success();

        Integer userid = getCurrentLoginUserid();

        expense.setSheetuserid(userid);
        expense.setSheettime(DateUtils.dateTimeNow());

        Integer status = expense.getSheetstatus();
        logger.debug("sheet.getSheetstatus is " + status.toString());

        expense.setSheetstatus(SheetStatus.XinJianZhong.getCode());
        logger.debug("AudExpense add is " + expense.toString());

        expense.setDaxie(ConvertUpMoney.toChinese(expense.getMoney().toString()));
        expense.setOrganizationid(expense.getProjectinfo().getOrganizationid());

        Integer result = expenseService.addExpenseTijiaoren(expense);

        if (result > 0) {

            ajax.put(AjaxResult.DATA_TAG, expense.getExpensesheetid());

            return ajax;
        } else {

            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }



    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('expense:tijiaoren:list')")
    @GetMapping(value = {"/{expensesheetid}"})
    public AjaxResult getExpense(@PathVariable(value = "expensesheetid", required = false) Integer expensesheetid) throws IOException, WriterException {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(expensesheetid)) {
            logger.debug("getExpense expensesheetid = " + expensesheetid.toString());

            AudExpense p = expenseService.selectExpenseById(expensesheetid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, p);
            } else {
                ajax = AjaxResult.error("expensesheetid：" + expensesheetid.toString() + " 不存在");
            }

        }
        return ajax;
    }
}
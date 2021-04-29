package com.ruoyi.web.controller.contract;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.ruoyi.common.config.RuoYiConfig;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


import com.ruoyi.common.utils.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;

import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.w3c.dom.Node;




public class WordUtil {

    public static String tempFilePath = "d:\\tmp\\";

    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "w:bookmarkStart";
    public static final String BOOKMARK_END_TAG = "w:bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";


    public static void main(String[] args) throws Exception{

        String filapath="D:\\test.docx";
        String filapath_dest="D:\\test.pdf";
        Word2007ToHtml(filapath,filapath_dest, "D:\\tmp");

//        OPCPackage pack = POIXMLDocument.openPackage(filapath);
//        XWPFDocument document = new XWPFDocument(pack);
//        XHTMLOptions options = XHTMLOptions.create();
//        //图片转base64
////        options.setImageManager(new Base64EmbedImgManager());
//        // 转换htm11
//        OutputStream out = new FileOutputStream(new File(filapath_dest));
//
//        XHTMLConverter.getInstance().convert(document, out, options);


//        System.out.println("xxx");

    }



    /**
     * 2007版本word转换成html
     * @throws IOException
     */
    public static void Word2007ToHtml(String fileName, String htmFile, String imageFile) throws IOException {

        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Sorry File does not Exists!");
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {
//                try {
//                    //1:加载文档到XWPFDocument
//                    InputStream in = new FileInputStream(f);
//                    XWPFDocument document = new XWPFDocument(in);
//                    //2：加载图片到指定文件夹
//                    File imgFile = new File(imageFile);
//                    XHTMLOptions options = XHTMLOptions.create();
//
//                    Base64ImageExtractor imageExtractor = new Base64ImageExtractor();
//                    options.setExtractor(imageExtractor);
//                    options.URIResolver(imageExtractor);
//
//
//                    //3：转换XWPFDocument to XHTML
//                    OutputStream out = new FileOutputStream(new File(htmFile));
//                    ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
//                    IXWPFConverter<XHTMLOptions> converter = XHTMLConverter.getInstance();
//                    converter.convert(document, htmlStream, options);
//                    System.out.println(htmlStream.toString());
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                try {
                    //1:加载文档到XWPFDocument
                    InputStream in = new FileInputStream(f);
                    XWPFDocument document = new XWPFDocument(in);

                     PdfOptions pdfOptions = PdfOptions.create();
                    // 输出路径
                    OutputStream  outStream = new FileOutputStream(htmFile);
                    // 调用转换
                    PdfConverter.getInstance().convert(document,outStream,pdfOptions);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {

                }

            } else {
                System.out.println("Enter only MS Office 2007+ files");
            }
        }
    }

    public void testmain() throws IOException, InvalidFormatException {
        String filapath="D:\\产品购销合同.docx";
        String destpath="D:\\产品购销合同_dest.docx";

        Map<String,Object> erweicode = new HashMap<String, Object>();
        erweicode.put("width", 80);
        erweicode.put("height", 80);
        erweicode.put("type", "png");
        erweicode.put("content", "d:\\20210426163654699.png");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("erweima", erweicode);
        map.put("title", "I hava a pen");
        map.put("contractcode", "I contractcode");


        OPCPackage pack = POIXMLDocument.openPackage(filapath);
        XWPFDocument document = new XWPFDocument(pack);

        /**
         * 对段落中的标记进行替换
         */
        List<XWPFParagraph> parasList = document.getParagraphs();
        replaceInAllParagraphs(parasList, map);
//
//        /**
//         * 对表格中的标记进行替换
//         */
//        List<XWPFTable> tables = document.getTables();
//        replaceInTables(tables, map);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(destpath);
            document.write(outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换所有段落中的标记
     *
     * @param xwpfParagraphList
     * @param params
     */
    public static void replaceInAllParagraphs(List<XWPFParagraph> xwpfParagraphList, Map<String, Object> params) throws IOException, InvalidFormatException {
        for (XWPFParagraph paragraph : xwpfParagraphList) {
            //Here you have your paragraph;
            CTP ctp = paragraph.getCTP();
            // Get all bookmarks and loop through them
            List<CTBookmark> bookmarks = ctp.getBookmarkStartList();

            for(CTBookmark bookmark : bookmarks)
            {

                System.out.println("paragraph bookmark name "+bookmark.getName());

                Object data = params.get(bookmark.getName());
                if (data != null) {

                    if (data instanceof String) {
                        XWPFRun run = paragraph.createRun();
                        run.setText(data.toString());

                        Node firstNode = bookmark.getDomNode();
                        Node nextNode = firstNode.getNextSibling();
                        while (nextNode != null) {
                            String nodeName = nextNode.getNodeName();
                            if (nodeName.equals(BOOKMARK_END_TAG)) {
                                break;
                            }
                            Node delNode = nextNode;
                            nextNode = nextNode.getNextSibling();
                            ctp.getDomNode().removeChild(delNode);
                        }

                        if (nextNode == null) {
                            ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), firstNode);
                        } else {
                            ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
                        }
                    }
                    else if (data instanceof Map) {

                        String imgurl = (String)((Map<?, ?>) data).get("content");
                        String type = (String)((Map<?, ?>) data).get("type");
                        int width = (Integer) ((Map<?, ?>) data).get("width");
                        int height = (Integer) ((Map<?, ?>) data).get("height");

                        InputStream picIs = new FileInputStream(new File(imgurl));
                        if(picIs != null){

                            XWPFRun run = paragraph.createRun();

                            run.addPicture(picIs,XWPFDocument.PICTURE_TYPE_PNG,imgurl, Units.toEMU(width), Units.toEMU(height));

                            System.out.println("image added now.");
                        }
                    }

                }

            }


        }
    }


    public static void createPicture(String blipId, int id, int width, int height, XWPFParagraph paragraph)
    {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        //String blipId = getAllPictures().get(id).getPackageRelationship().getId();

//        CTInline inline = createParagraph().createRun().getCTR().addNewDrawing().addNewInline();
        //给段落插入图片
        CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();

        String picXml = "" +
                "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "         <pic:nvPicPr>" +
                "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +
                "            <pic:cNvPicPr/>" +
                "         </pic:nvPicPr>" +
                "         <pic:blipFill>" +
                "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                "            <a:stretch>" +
                "               <a:fillRect/>" +
                "            </a:stretch>" +
                "         </pic:blipFill>" +
                "         <pic:spPr>" +
                "            <a:xfrm>" +
                "               <a:off x=\"0\" y=\"0\"/>" +
                "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +
                "            </a:xfrm>" +
                "            <a:prstGeom prst=\"rect\">" +
                "               <a:avLst/>" +
                "            </a:prstGeom>" +
                "         </pic:spPr>" +
                "      </pic:pic>" +
                "   </a:graphicData>" +
                "</a:graphic>";

        //CTGraphicalObjectData graphicData = inline.addNewGraphic().addNewGraphicData();
        XmlToken xmlToken = null;
        try
        {
            xmlToken = XmlToken.Factory.parse(picXml);
        }
        catch(XmlException xe)
        {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        //graphicData.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }


    /**
     * 替换段落中的字符串
     *
     * @param xwpfParagraph
     * @param oldString
     * @param newString
     */
    public static void replaceInParagraph(XWPFParagraph xwpfParagraph, String oldString, String newString) {
        Map<String, Integer> pos_map = findSubRunPosInParagraph(xwpfParagraph, oldString);
        if (pos_map != null) {
            System.out.println("start_pos:" + pos_map.get("start_pos"));
            System.out.println("end_pos:" + pos_map.get("end_pos"));

            List<XWPFRun> runs = xwpfParagraph.getRuns();
            XWPFRun modelRun = runs.get(pos_map.get("end_pos"));
            XWPFRun xwpfRun = xwpfParagraph.insertNewRun(pos_map.get("end_pos") + 1);
            xwpfRun.setText(newString);
            System.out.println("字体大小：" + modelRun.getFontSize());
            if (modelRun.getFontSize() != -1) xwpfRun.setFontSize(modelRun.getFontSize());//默认值是五号字体，但五号字体getFontSize()时，返回-1
            xwpfRun.setFontFamily(modelRun.getFontFamily());
            for (int i = pos_map.get("end_pos"); i >= pos_map.get("start_pos"); i--) {
                System.out.println("remove run pos in :" + i);
                xwpfParagraph.removeRun(i);
            }
        }
    }


    /**
     * 找到段落中子串的起始XWPFRun下标和终止XWPFRun的下标
     *
     * @param xwpfParagraph
     * @param substring
     * @return
     */
    public static Map<String, Integer> findSubRunPosInParagraph(XWPFParagraph xwpfParagraph, String substring) {

        List<XWPFRun> runs = xwpfParagraph.getRuns();
        int start_pos = 0;
        int end_pos = 0;
        String subtemp = "";
        for (int i = 0; i < runs.size(); i++) {
            subtemp = "";
            start_pos = i;
            for (int j = i; j < runs.size(); j++) {
                if (runs.get(j).getText(runs.get(j).getTextPosition()) == null) continue;
                subtemp += runs.get(j).getText(runs.get(j).getTextPosition());
                if (subtemp.equals(substring)) {
                    end_pos = j;
                    Map<String, Integer> map = new HashMap<>();
                    map.put("start_pos", start_pos);
                    map.put("end_pos", end_pos);
                    return map;
                }
            }
        }
        return null;
    }
    /**
     * 替换所有的表格
     *
     * @param xwpfTableList
     * @param params
     */
//    public static void replaceInTables(List<XWPFTable> xwpfTableList, Map<String, String> params) {
//        for (XWPFTable table : xwpfTableList) {
//            replaceInTable(table, params);
//
//        }
//    }

    /**
     * 替换一个表格中的所有行
     *
     * @param xwpfTable
     * @param params
     */
//    public static void replaceInTable(XWPFTable xwpfTable, Map<String, String> params) {
//        List<XWPFTableRow> rows = xwpfTable.getRows();
//        replaceInRows(rows, params);
//    }


    /**
     * 替换表格中的一行
     *
     * @param rows
     * @param params
     */
//    public static void replaceInRows(List<XWPFTableRow> rows, Map<String, String> params) {
//        for (int i = 0; i < rows.size(); i++) {
//            XWPFTableRow row = rows.get(i);
//            replaceInCells(row.getTableCells(), params);
//        }
//    }

    /**
     * 替换一行中所有的单元格
     *
     * @param xwpfTableCellList
     * @param params
     */
//    public static void replaceInCells(List<XWPFTableCell> xwpfTableCellList, Map<String, String> params) {
//        for (XWPFTableCell cell : xwpfTableCellList) {
//            replaceInCell(cell, params);
//        }
//    }

    /**
     * 替换表格中每一行中的每一个单元格中的所有段落
     *
     * @param cell
     * @param params
     */
//    public static void replaceInCell(XWPFTableCell cell, Map<String, Object> params) {
//        List<XWPFParagraph> cellParagraphs = cell.getParagraphs();
//        replaceInAllParagraphs(cellParagraphs, params);
//    }

    /**
     * 替换word中的自定义字符串以及图片（适用于word2003+ 版本）
     *
     * 注：2003版本word不支持替换图片，2007版本以上可以替换图片
     *
     * @param filePath
     * @param param
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    public static String replaceAndGenerateWord(String filePath, Map<String, Object> param, String fileName, HttpServletRequest request, HttpServletResponse response){
        String[] sp = filePath.split("\\.");
        //判断文件是否有后缀名
        if(sp.length > 0){
            try{
                //处理docx文档 2007-2013
                if(sp[sp.length - 1].equalsIgnoreCase("docx")){
                    CustomXWPFDocument document = null;
                    OPCPackage pack = POIXMLDocument.openPackage(filePath);
                    document = new CustomXWPFDocument(pack);
                    if (param != null && param.size() > 0) {
                        //处理段落
                        List<XWPFParagraph> paragraphList = document.getParagraphs();

                        for (XWPFParagraph paragraph : paragraphList)
                        {
                            //Here you have your paragraph;
                            CTP ctp = paragraph.getCTP();
                            // Get all bookmarks and loop through them
                            List<CTBookmark> bookmarks = ctp.getBookmarkStartList();
                            for(CTBookmark bookmark : bookmarks)
                            {
                                String paraText = paragraph.getText();
                                System.out.println("paraText = "+paraText +" for bookmark name "+bookmark.getName());
                                String replacementText = replaceBookmarkedPara(paraText, "haha");
                                removeAllRuns(paragraph);
                                insertReplacementRuns(paragraph, replacementText);
                            }
                        }

                        processParagraphs(paragraphList, param, document);
                        //处理表格
                        Iterator<XWPFTable> it = document.getTablesIterator();
                        while (it.hasNext()) {
                            XWPFTable table = it.next();
                            List<XWPFTableRow> rows = table.getRows();
                            for (XWPFTableRow row : rows) {
                                List<XWPFTableCell> cells = row.getTableCells();
                                for (XWPFTableCell cell : cells) {
                                    List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
                                    processParagraphs(paragraphListTable, param, document);
                                }
                            }
                        }
                        createDir(tempFilePath);
                        System.out.println("tempfile is " + tempFilePath.concat(fileName));
                        FileOutputStream fos = new FileOutputStream(new File(tempFilePath.concat(fileName)));
                        document.write(fos);
                        fos.flush();
                        fos.close();
                       // doExport(fileName, tempFilePath.concat(fileName), request, response);
                        return tempFilePath.concat(fileName);
                    }
                    //处理doc文档 97-2003
                }else if(sp[sp.length - 1].equalsIgnoreCase("doc")){
                    HWPFDocument document = null;
                    document = new HWPFDocument(new FileInputStream(filePath));
                    Range range = document.getRange();
                    for (Map.Entry<String, Object> entry : param.entrySet()) {
                        Object value = entry.getValue();
                        if(value instanceof String){
                            range.replaceText(entry.getKey(), entry.getValue().toString());
                        }else if(value instanceof Map){
                            //TODO word2003暂时不能处理图片
                        }
                    }
                    createDir(tempFilePath);
                    FileOutputStream fos = new FileOutputStream(new File(tempFilePath.concat(fileName)));
                    document.write(fos);
                    fos.flush();
                    fos.close();
                    doExport(fileName, tempFilePath.concat(fileName), request, response);
                    return tempFilePath.concat(fileName);
                }
            }catch(Exception e){
                return "fail";
            }
        }else{
            return "fail";
        }
        return "success";
    }

    /**
     * 处理段落
     * @param paragraphList
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,CustomXWPFDocument doc) throws InvalidFormatException, FileNotFoundException, FileNotFoundException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        if(paragraphList != null && paragraphList.size() > 0){
            //首选循环段落
            for(XWPFParagraph paragraph:paragraphList){
                //获取段落的text
                boolean needDel = false;
                String text = paragraph.getText();
                if(text != null){
                    for (Map.Entry<String, Object> entry : param.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        //替换
                        if(value instanceof String){
                            String text2 = text.replace(key, value.toString());
                            if(!text2.equals(text)){
                                needDel = true;
                            }
                            text = text2;
                        }else if(value instanceof Map){
                            if(text.indexOf(key) != -1){
                                //特殊处理图片
                                int length = paragraph.getRuns().size();
                                //将原有的Run去掉
                                if (length > 0) {
                                    for (int i = (length - 1); i >= 0; i--) {
                                        paragraph.removeRun(i);
                                    }
                                }
                                String imgurl = (String)((Map<?, ?>) value).get("content");
                                String type = (String)((Map<?, ?>) value).get("type");
                                int width = (Integer) ((Map<?, ?>) value).get("width");
                                int height = (Integer) ((Map<?, ?>) value).get("height");
                                String blipId = doc.addPictureData(new FileInputStream(new File(imgurl)), getPictureType(type));
                                doc.createPicture(blipId,doc.getNextPicNameNumber(getPictureType(type)), width, height,paragraph);
                            }
                        }
                    }
                }
                int length = paragraph.getRuns().size();
                //将原有的Run去掉（原因是paragraph将XWPFRun分割成了一个乱七八糟的数组，例：${1}$，这个获取出来的是[$,{,1,},$],不能满足我们替换的要求，这里进行特殊处理）
                if(needDel){
                    if (length > 0) {
                        for (int i = (length - 1); i >= 0; i--) {
                            paragraph.removeRun(i);
                        }
                        //在段落里面插入我们替换过后的文本
                        XWPFRun newRun = paragraph.insertNewRun(0);
                        newRun.setText(text, 0);
                        paragraph.addRun(newRun);
                    }
                }
            }
        }
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    /**
     * 导出
     *
     * @param fileName
     * @param filePath
     * @param request
     * @param response
     */
    public static void doExport(String fileName, String filePath, HttpServletRequest request, HttpServletResponse response){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File file = null;
//        HttpServletResponse response = (HttpServletResponse)RpcContext.getContext().getResponse(HttpServletResponse.class);
        try {
            file = new File(filePath);
//        HttpServletRequest request = (HttpServletRequest)RpcContext.getContext().getRequest(HttpServletRequest.class);
            request.setCharacterEncoding("UTF-8");
            String agent = request.getHeader("User-Agent").toUpperCase();
            if ((agent.indexOf("MSIE") > 0) || ((agent.indexOf("RV") != -1) && (agent.indexOf("FIREFOX") == -1)))
                fileName = URLEncoder.encode(fileName, "UTF-8");
            else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
                bos.write(buff, 0, bytesRead);
        }
        catch (Exception e) {
//          System.out.println("导出文件失败！");
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                file.delete();
            } catch (Exception e) {
//            LOGGER.error("导出文件关闭流出错！", e);
            }
        }
    }

    /**
     * 创建目录
     * @param basePath
     */
    public static void createDir(String basePath)
    {
        File file = new File(basePath);
        if (!file.exists())
            file.mkdirs();
    }

    private static String replaceBookmarkedPara(String input, String bookmarkTxt) {
        char[] tmp = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        int bookmarkedCharCount = 0;
        for (int i = 0 ; i < tmp.length ; i++) {
            int asciiCode = tmp[i];
            if (asciiCode == 8194) {
                bookmarkedCharCount ++;
                if (bookmarkedCharCount == 5) {
                    sb.append(bookmarkTxt);
                }
            }
            else {
                sb.append(tmp[i]);
            }
        }
        return sb.toString();
    }

    private static void removeAllRuns(XWPFParagraph paragraph) {
        int size = paragraph.getRuns().size();
        for (int i = 0; i < size; i++) {
            paragraph.removeRun(0);
        }
    }

    private static void insertReplacementRuns(XWPFParagraph paragraph, String replacedText) {
        String[] replacementTextSplitOnCarriageReturn = StringUtils.split(replacedText, "\n");

        for (int j = 0; j < replacementTextSplitOnCarriageReturn.length; j++) {
            String part = replacementTextSplitOnCarriageReturn[j];

            XWPFRun newRun = paragraph.insertNewRun(j);
            newRun.setText(part);

            if (j+1 < replacementTextSplitOnCarriageReturn.length) {
                newRun.addCarriageReturn();
            }
        }
    }

}



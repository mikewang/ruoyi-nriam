package com.ruoyi.web.controller.audit;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * word 转换成html
 */
public class TestWordToHtml {

    public static  final String STORAGEPATH="D://";
    public static  final String IP="127.0.0.1";
    public static  final String PORT="8080";
    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException {
        TestWordToHtml wt=new TestWordToHtml();
        wt.Word2007ToHtml("test技术服务.docx");

    }

    /**
     * 2003版本word转换成html
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public void Word2003ToHtml(String fileName) throws IOException, TransformerException, ParserConfigurationException {

        final String imagepath = STORAGEPATH+"fileImage/";//解析时候如果doc文件中有图片  图片会保存在此路径
        final String strRanString=getRandomNum();
        String filepath =STORAGEPATH;
        String htmlName =fileName.substring(0, fileName.indexOf("."))+ "2003.html";
        final String file = filepath + fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        //设置图片存放的位置
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                File imgPath = new File(imagepath);
                if(!imgPath.exists()){//图片目录不存在则创建
                    imgPath.mkdirs();
                }

                File file = new File(imagepath +strRanString+suggestedName);
                try {
                    OutputStream os = new FileOutputStream(file);
                    os.write(content);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return  "http://"+IP+":"+PORT+"//uploadFile/fileImage/"+strRanString+suggestedName;
                // return imagepath +strRanString+suggestedName;
            }
        });

        //解析word文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();

        File htmlFile = new File(filepath +strRanString+htmlName);
        OutputStream outStream = new FileOutputStream(htmlFile);


        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");

        serializer.transform(domSource, streamResult);
        outStream.close();

        System.out.println("生成html文件路径:"+ "http://"+IP+":"+PORT+"//uploadFile/"+strRanString+htmlName);
    }

    /**
     * 2007版本word转换成html
     * @throws IOException
     */
    public void Word2007ToHtml(String fileName) throws IOException {

        final String strRanString=getRandomNum();

        String filepath = STORAGEPATH+strRanString;
        String htmlName =fileName.substring(0, fileName.indexOf("."))+ "2007.html";
        File f = new File(STORAGEPATH+fileName);
        if (!f.exists()) {
            System.out.println("Sorry File does not Exists!");
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {
                try {
                    // 1) 加载word文档生成 XWPFDocument对象
                    InputStream in = new FileInputStream(f);
                    XWPFDocument document = new XWPFDocument(in);

                    // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
                    File imageFolderFile = new File(filepath);
                    XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
                    options.setExtractor(new FileImageExtractor(imageFolderFile));
                    options.URIResolver(new IURIResolver() {
                        public String resolve(String uri) {
                            //http://192.168.30.222:8010//uploadFile/....
                            return "http://"+IP+":"+PORT+"//uploadFile/"+strRanString +"/"+ uri;
                        }
                    });

                    options.setIgnoreStylesIfUnused(false);
                    options.setFragment(true);

                    // 3) 将 XWPFDocument转换成XHTML
                    OutputStream out = new FileOutputStream(new File(filepath + htmlName));
                    IXWPFConverter<XHTMLOptions> converter = XHTMLConverter.getInstance();
                    converter.convert(document,out, options);
                    //XHTMLConverter.getInstance().convert(document, out, options);
                    System.out.println("html路径:"+"http://"+IP+":"+PORT+"//uploadFile/"+strRanString+htmlName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Enter only MS Office 2007+ files");
            }
        }
    }

    /**
     *功能说明:生成时间戳
     *创建人:zsq
     *创建时间:2019年12月7日 下午2:37:09
     *
     */
    public static String getRandomNum(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str=sdf.format(dt);
        return str;
    }

}
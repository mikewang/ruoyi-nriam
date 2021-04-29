package com.ruoyi.web.controller.audit;

import java.io.*;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordtoHtml07 {

    public static void word07ToHtml(String fileName ,String imageFile , String htmFile) throws IOException{
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("sorry file does not exists");
        }else{
            if (f.getName().endsWith(".docx")|| f.getName().endsWith(".DOCX") || f.getName().endsWith(".doc")) {
                //1:加载文档到XWPFDocument
                InputStream in = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(in);
                //2：加载图片到指定文件夹
                File imgFile = new File(imageFile);
                XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imgFile));
                options.setExtractor(new FileImageExtractor(imgFile));

                //3：转换XWPFDocument to XHTML
                OutputStream out = new FileOutputStream(new File(htmFile));
                XHTMLConverter.getInstance().convert(document, out, options);
            }else{
                System.out.println("Enter only MS Office 2007+ files");
            }
        }
    }
    public static void main(String args[]) {
        try {
            word07ToHtml("D:/test技术服务.docx","D:/tmp","D:/test技术服务.htm");

//            String  path = "D://test技术服务.docx";
//            InputStream in= new FileInputStream(new File(path));
//            XWPFDocument document = new XWPFDocument(in);
//
//            XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(new File("D://")));
//
//            OutputStream out = new ByteArrayOutputStream();
//
//            XHTMLConverter.getInstance().convert(document, out, options);
//            String html=out.toString();
//            System.out.println(html);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
package com.ruoyi.web.controller.contract;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GenerateQRCode {
    //static function that creates QR Code
    public static void generateQRcode(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException {
    //the BitMatrix class represents the 2D matrix of bits
    //MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.

        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);

        MatrixToImageWriter.writeToPath(matrix,"jpg", Paths.get(path));

    }

    //main() method
    public static void main(String args[]) throws WriterException, IOException, NotFoundException {
//data that we want to store in the QR code
        String str = "THE HABIT OF PERSISTENCE IS THE HABIT OF VICTORY.";
//path where we want to get QR Code
        String filePath = DateUtils.dateTimeNow("yyyyMMddHHmmssSSS") + ".jpg";

        filePath = RuoYiConfig.getProfile() + File.separator + "temp" +File.separator + filePath ;


//Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//invoking the user-defined method that creates the QR code
        generateQRcode(str, filePath, charset, hashMap, 200, 200);//increase or decrease height and width accodingly
//prints if the QR code is generated
        System.out.println("QR Code created successfully.");
    }
}
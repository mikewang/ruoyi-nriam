package com.ruoyi.web.controller.contract;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.poi.xwpf.converter.core.IImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;

import java.io.IOException;

public class Base64ImageExtractor {

    private byte[] picture;

    public void extract(String imagePath, byte[] imageData) throws IOException {
        this.picture = imageData;
    }

    private static final String EMBED_IMG_SRC_PREFIX = "data:;base64,";


    public String resolve(String uri) {
        StringBuilder sb = new StringBuilder(picture.length + EMBED_IMG_SRC_PREFIX.length())
                .append(EMBED_IMG_SRC_PREFIX)
                .append(Base64Utility.encode(picture));
        return sb.toString();
    }
}
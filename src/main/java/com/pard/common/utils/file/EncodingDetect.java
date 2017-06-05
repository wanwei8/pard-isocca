package com.pard.common.utils.file;

import java.io.File;

/**
 * 自动获取文件的编码
 * <p>
 * Created by wawe on 17/5/31.
 */
public class EncodingDetect {
    /**
     * 得到文件的编码
     *
     * @param filePath 文件路径
     * @return 文件的编码
     */
    public static String getJavaEncode(String filePath) {
        BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(new File(filePath))];
        return fileCode;
    }
}

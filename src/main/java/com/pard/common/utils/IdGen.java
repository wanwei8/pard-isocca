package com.pard.common.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * <p>
 * Created by wawe on 17/6/3.
 */
public class IdGen {
    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getNewId() {
        long times = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date(times);

        String id = sdf.format(date);

        String haomiao = String.valueOf(System.nanoTime());
        id += haomiao.substring(haomiao.length() - 6, haomiao.length());

        String guid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);

        id += guid;
        return id;
    }

    public static String adminId(int len) {
        return StringUtils.repeat("0", len);
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return EncodeUtils.encodeBase62(randomBytes);
    }
}

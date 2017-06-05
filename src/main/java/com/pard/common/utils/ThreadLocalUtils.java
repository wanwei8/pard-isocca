package com.pard.common.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by wawe on 17/6/3.
 */
public class ThreadLocalUtils {
    private static final ThreadLocal<Map<String, Object>> local = ThreadLocal.withInitial(() -> Maps.newHashMap());

    public static <T> T put(String key, T value) {
        local.get().put(key, value);
        return value;
    }

    public static void remove(String key) {
        local.get().remove(key);
    }

    public static void clear() {
        local.remove();
    }

    public static <T> T get(String key) {
        return (T) local.get().get(key);
    }
}

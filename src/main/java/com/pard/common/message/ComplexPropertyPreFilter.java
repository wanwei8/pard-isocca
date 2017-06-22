package com.pard.common.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

/**
 * Created by wawe on 17/6/20.
 *
 * @Comment: fastjson 针对类型的属性过滤器（可以跨层级）
 */
public class ComplexPropertyPreFilter implements PropertyPreFilter {

    static {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    private Map<Class<?>, Set<String>> includes = Maps.newHashMap();
    private Map<Class<?>, Set<String>> excludes = Maps.newHashMap();

    public ComplexPropertyPreFilter() {

    }

    public ComplexPropertyPreFilter(Map<Class<?>, Set<String>> includes) {
        super();
        this.includes = includes;
    }

    public static boolean isHave(Set<String> strs, String s) {
        for (String item : strs) {
            if (item.equals(s)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) return true;

        Class<?> clazz = source.getClass();

        for (Map.Entry<Class<?>, Set<String>> item : this.excludes.entrySet()) {
            if (item.getKey().isAssignableFrom(clazz)) {
                Set<String> strs = item.getValue();

                if (isHave(strs, name)) {
                    return false;
                }
            }
        }

        if (this.includes.isEmpty()) return true;

        for (Map.Entry<Class<?>, Set<String>> item : this.includes.entrySet()) {
            int level = 0;
            SerialContext context = serializer.getContext();
            while (context != null) {
                level++;
                if (level > 10) {
                    return false;
                }
                context = context.parent;
            }
            if (item.getKey().isAssignableFrom(clazz)) {
                Set<String> strs = item.getValue();

                if (isHave(strs, name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Class<?>, Set<String>> getIncludes() {
        return includes;
    }

    public void setIncludes(Map<Class<?>, Set<String>> includes) {
        this.includes = includes;
    }

    public Map<Class<?>, Set<String>> getExcludes() {
        return excludes;
    }

    public void setExcludes(Map<Class<?>, Set<String>> excludes) {
        this.excludes = excludes;
    }


}

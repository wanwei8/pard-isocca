package com.pard.common;

import com.google.common.collect.Maps;
import com.pard.common.constant.StringConstant;
import com.pard.common.utils.PropertiesLoader;
import com.pard.common.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wawe on 17/6/4.
 */
@Component
public class Global implements StringConstant {
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("application.properties");

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取管理端根路径
     *
     * @return
     */
    public String getAdminPath() {
        return getConfig("adminPath");
    }

    /**
     * 获取API路径
     *
     * @return
     */
    public String getApiPath() {
        return getConfig("apiPath");
    }

    /**
     * 是否演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     *
     * @return
     */
    public boolean isDemoMode() {
        String dm = getConfig("demoMode");
        return "true".equals(dm) || "1".equals(dm);
    }
}

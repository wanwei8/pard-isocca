package com.pard.common.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by wawe on 17/4/26.
 */
@WebServlet(urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "loginUsername", value = "pard"), //用户名
                @WebInitParam(name = "loginPassword", value = "pard905"),//密码
                @WebInitParam(name = "resetEnable", value = "false"),//是否允许清空统计数据
        })
public class DruidStatViewServlet extends StatViewServlet {
}

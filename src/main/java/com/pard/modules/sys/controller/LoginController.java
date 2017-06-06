package com.pard.modules.sys.controller;

import com.google.code.kaptcha.Producer;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.pard.common.controller.GenericController;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.service.BaseService;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Created by wawe on 17/4/26.
 */
@Controller
public class LoginController extends GenericController {

    @Autowired
    private Producer captchaProducer;
    @Autowired
    private MenuService menuService;

    private Ordering<Menu> orderMenu = new Ordering<Menu>() {
        public int compare(Menu menu1, Menu menu2) {
            if (menu1 == null || menu2 == null) return -1;
            return Ints.compare(menu1.getSort(), menu2.getSort());
        }
    };

    /**
     * 管理登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @AccessLogger("管理登录")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String requestType = request.getHeader("X-Requested-With");
        if (StringUtils.isBlank(requestType))
            return "modules/sys/login";
        return "modules/sys/login2";
    }

    /**
     * 生成验证码图片
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/components/kaptcha.png")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.addHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");
        String capText = captchaProducer.createText();
        session.setAttribute(SESSION_VALIDATE_CODE, capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();

        ImageIO.write(bi, "png", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        String code = (String) session.getAttribute(SESSION_VALIDATE_CODE);
        logger.debug("*********************验证码:" + code + "********************");
        return null;
    }

    @RequestMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<Menu> menus = menuService.findAllMenu();
        model.addAttribute("menu", makeIndexMenu(menus, null));
        return "modules/sys/sysIndex";
    }

    private String makeIndexMenu(List<Menu> menus, Menu parent) {
        StringBuilder sbHtml = new StringBuilder();

        List<Menu> childs = orderMenu.sortedCopy(Collections2.filter(menus, new Predicate<Menu>() {
            @Override
            public boolean apply(Menu menu) {
                if (parent == null) {
                    return menu.getParent() == null;
                }
                return parent.equals(menu.getParent());
            }
        }));

        for (Menu m : childs) {
            String url = getUrl(m.getHref());
            sbHtml.append("<li class='hover'>");
            String childHtml = makeIndexMenu(menus, m);
            if (StringUtils.isBlank(url)) {
                sbHtml.append("<a href='#' " + (StringUtils.isBlank(childHtml) ? "" : "class='dropdown-toggle'") + ">");
            } else {
                sbHtml.append("<a data-url='" + url + "' href='#" + url + "'>");
            }
            sbHtml.append("<i class='menu-icon " + (StringUtils.isBlank(m.getIcon()) ? " fa fa-caret-right" : m.getIcon()) + "'></i>");
            if (StringUtils.isBlank(childHtml)) {
                sbHtml.append("<span class='menu-text'>" + m.getName() + "</span>");
            } else {
                sbHtml.append(m.getName());
            }
            sbHtml.append(StringUtils.isNotBlank(childHtml) ? "<b class='arrow fa fa-angle-down'></b>" : "")
                    .append("</a>").append("<b class='arrow'></b>");
            if (StringUtils.isNotBlank(childHtml)) {
                sbHtml.append("<ul class='submenu'>")
                        .append(childHtml)
                        .append("</ul>");
            }
            sbHtml.append("</li>");
        }

        return sbHtml.toString();
    }

    private String getUrl(String href) {
        if (StringUtils.isBlank(href)) return "";
        String url = href.trim();
        return adminPath + "/" + (url.indexOf("/") == 0 ? url.substring(1) : url);
    }
}

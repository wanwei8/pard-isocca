package com.pard.modules.sys.controller;

import com.pard.common.controller.GenericController;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

/**
 * Created by wawe on 17/5/15.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends GenericController {
    @Autowired
    private MenuService menuService;

    @ModelAttribute
    public Menu get(@RequestParam(required = false) String id) {
        Menu menu = null;
        if (StringUtils.isNotBlank(id)) {
            menu = menuService.findOne(id);
            if (menu != null) {
                if (menu.getParent() == null)
                    menu.setParent(new Menu("0"));
                return menu;
            }
        }
        menu = new Menu();
        menu.setParent(new Menu("0"));
        return menu;
    }

    @PreAuthorize("hasAuthority('sys:menu:view')")
    @RequestMapping(value = {"", "list"})
    public String list(Model model) {
        return "modules/sys/menuList";
    }

    @PreAuthorize("hasAnyAuthority('sys:menu:view', 'sys:menu:add', 'sys:menu:edit')")
    @RequestMapping(value = "form")
    public String form(Menu menu, Model model) {
        model.addAttribute("menu", menu);
        return "modules/sys/menuForm";
    }

    @RequestMapping(value = "icon")
    public String icon(String sel, Model model) {
        if (StringUtils.isBlank(sel)) {
            sel = "glyphicon glyphicon-plus";
        }
        String[] icons = HtmlUtils.htmlUnescape(sel).split(" ");
        model.addAttribute("icon", sel);
        model.addAttribute("icon_name", sel.replaceFirst(icons[0], ""));
        return "modules/sys/icon";
    }
}

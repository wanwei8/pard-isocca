package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pard.common.controller.GenericController;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.utils.Collections3;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by wawe on 17/6/12.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
@AccessLogger("角色管理")
public class RoleController extends GenericController {
    @Autowired
    private RoleService roleService;

    @ModelAttribute
    public Role get(@RequestParam(required = false) String id) {
        Role role;
        if (StringUtils.isNotBlank(id)) {
            role = roleService.findOne(id);
            if (role != null) {
                if (role.getCompany() == null) {
                    role.setCompany(new Office());
                }
                if (role.getOffice() == null) {
                    role.setOffice(new Office());
                }
                return role;
            }
        }
        role = new Role();
        role.setCompany(new Office());
        role.setOffice(new Office());
        return role;
    }

    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/sys/roleList";
    }

    @RequestMapping(value = "form")
    public String form(Role role, Model model) {
        model.addAttribute("role", role);
        return "modules/sys/roleForm";
    }

    /**
     * @param role
     * @param model
     * @return
     */
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public String selectMenuToRole(Role role, Model model) {
        model.addAttribute("role", role);
        model.addAttribute("selectIds", Collections3.extractToString(role.getMenuList(), "id", ","));
        return "modules/sys/selectMenuToRole";
    }

    /**
     * 角色分配页面
     *
     * @param role
     * @param model
     * @return
     */
    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public String assign(Role role, Model model) {
        model.addAttribute("role", role);
        model.addAttribute("userList", role.getUserList());
        return "modules/sys/roleAssign";
    }

    /**
     * 角色分配 -- 打开角色分配对话框
     *
     * @param role
     * @param model
     * @return
     */
    @RequestMapping(value = "usertorole", method = RequestMethod.GET)
    public String selectUserToRole(Role role, Model model) {
        model.addAttribute("role", role);
        model.addAttribute("selectIds", Collections3.extractToString(role.getUserList(), "id", ","));
        List<Map<String, String>> users = Lists.newArrayList();
        role.getUserList().forEach(user -> {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", user.getId());
            map.put("text", "<font color='red' style='font-weight:bold;'>" + user.getName() + "</font>");
            map.put("icon", "fa fa-user red");
            users.add(map);
        });
        model.addAttribute("userList", users);
        return "modules/sys/selectUserToRole";
    }
}

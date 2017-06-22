package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pard.common.controller.GenericController;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.utils.Collections3;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.service.RoleService;
import com.pard.modules.sys.service.UserService;
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
 * Created by wawe on 17/5/22.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
@AccessLogger("用户管理")
public class UserController extends GenericController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        User user;
        if (StringUtils.isNotBlank(id)) {
            user = userService.findOne(id);
            if (user != null) {
                if (user.getCompany() == null) {
                    user.setCompany(new Office());
                }
                if (user.getOffice() == null) {
                    user.setOffice(new Office());
                }
                return user;
            }
        }
        user = new User();
        user.setCompany(new Office());
        user.setOffice(new Office());
        return user;
    }

    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/sys/userList";
    }

    @RequestMapping(value = "form")
    public String form(User user, Model model) {
        model.addAttribute("user", user);
        return "modules/sys/userForm";
    }

    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public String assign(User user, Model model) {
        model.addAttribute("user", user);
        return "modules/sys/userAssign";
    }

    @RequestMapping(value = "roletouser", method = RequestMethod.GET)
    public String selectRoleToUser(User user, Model model) {
        model.addAttribute("user", user);
        List<Role> roles = roleService.findRoleByUser(user.getId());
        model.addAttribute("selectIds", Collections3.extractToString(roles, "id", ""));
        List<Map<String, String>> roleList = Lists.newArrayList();
        roles.forEach(role -> {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", role.getId());
            map.put("text", "<font color='red' style='font-weight:bold;'>" + role.getName() + "</font>");
            map.put("icon", "fa fa-users red");
            roleList.add(map);
        });
        model.addAttribute("roleList", roleList);
        return "modules/sys/selectRoleToUser";
    }
}

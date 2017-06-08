package com.pard.modules.sys.controller;

import com.pard.common.controller.GenericController;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wawe on 17/5/22.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends GenericController {

    @Autowired
    private UserService userService;

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
}

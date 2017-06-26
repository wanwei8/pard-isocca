package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.message.JsTree;
import com.pard.common.message.ResponseMessage;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.service.RoleService;
import com.pard.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/user")
public class UserRestController extends GenericController implements MessageConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Value("${pard.fileUploadPath}")
    private String ROOT = "/Users/wawe/upfile/";

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            User user = userService.findOne(id);
            user.setOffice(new Office(user.getOfficeId()));
            user.setCompany(new Office(user.getCompanyId()));
            return user;
        } else {
            return new User();
        }
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public ResponseMessage findByPage(@Valid DataTableRequest input) {
        DataTableResponse<User> rst = userService.findAllUser(input);

        return ResponseMessage.ok(rst)
                .include(User.class, "id", "companyId", "officeId", "no", "loginName",
                        "name", "loginFlag", "companyName", "officeName")
                .onlyData();
    }

    @PreAuthorize("hasAnyAuthority('sys:user:add', 'sys:user:edit')")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(User user) {
        try {
            ResponseMessage checkName = checkName(user.getOldName(), user.getLoginName());
            if (!checkName.isSuccess()) {
                return checkName;
            }
            deletePhoto(user);
            user.setCompany(new Office(request.getParameter("company.id")));
            user.setOffice(new Office(request.getParameter("office.id")));

            userService.save(user);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception ex) {
            logger.error("User Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    public ResponseMessage checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return ResponseMessage.ok();
        } else if (name != null && userService.findByName(name) == null) {
            return ResponseMessage.ok();
        }
        return ResponseMessage.error(USERNAME_EXISTS_FAILD);
    }

    @AccessLogger("禁用")
    @RequestMapping(value = "/{id}/disable", method = RequestMethod.PUT)
    public ResponseMessage disable(@PathVariable String id) {
        userService.disableUser(id);
        return ResponseMessage.ok();
    }

    @AccessLogger("启用")
    @RequestMapping(value = "/{id}/enable", method = RequestMethod.PUT)
    public ResponseMessage enable(@PathVariable String id) {
        userService.enableUser(id);
        return ResponseMessage.ok();
    }

    private void deletePhoto(User user) {
        try {
            if (StringUtils.isNotBlank(user.getOldPhoto()) && !user.getOldPhoto().equals(user.getPhoto())) {
                StringBuilder sbPath = new StringBuilder();
                sbPath.append(ROOT.charAt(ROOT.length() - 1) == '/' ? ROOT.substring(0, ROOT.length() - 1) : ROOT)
                        .append("/").append(user.getOldPhoto().replaceFirst("/upload", ""));
                org.apache.commons.io.FileUtils.forceDelete(new File(sbPath.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreAuthorize("hasAuthority('sys:user:del')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseMessage delete(@PathVariable(name = "id", required = true) String id) {
        try {
            userService.deleteUser(id);
        } catch (Exception ex) {
            logger.error("User delete faild", ex);
            return ResponseMessage.error(DELETE_FAILD);
        }
        return ResponseMessage.ok(DELETE_SUCCESS);
    }

    @PreAuthorize("hasAuthority('sys:user:assign')")
    @RequestMapping(value = "/officeuser/{id}", method = RequestMethod.GET)
    public ResponseMessage getOfficeUser(@PathVariable(name = "id", required = true) String id) {
        List<User> users = userService.findByOffice(id);
        List<JsTree> r = Lists.newArrayList();
        users.forEach(u -> {
            JsTree tree = new JsTree();
            tree.setParent("#");
            tree.setId(u.getId());
            tree.setText(u.getName());
            tree.setIcon("fa fa-user yellow");

            r.add(tree);
        });

        return ResponseMessage.ok(r).onlyData();
    }

    @PreAuthorize("hasAuthority('sys:user:assign')")
    @RequestMapping(value = "assignrole", method = RequestMethod.POST)
    public ResponseMessage assignRole(User user, String[] idsArr) {
        int newNum = 0;
        for (int i = 0; i < idsArr.length; i++) {
            Role role = roleService.findOne(idsArr[i]);
            if (null != role && !role.getUserList().contains(user)) {
                role.getUserList().add(user);
                roleService.save(role);
                newNum++;
            }
        }
        return ResponseMessage.ok("已成功分配 " + newNum + " 个角色");
    }
}

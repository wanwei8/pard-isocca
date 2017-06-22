package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.message.JsTree;
import com.pard.common.message.ResponseMessage;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.service.RoleService;
import com.pard.modules.sys.service.UserService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by wawe on 17/6/13.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/role")
public class RoleRestController extends GenericController implements MessageConstant {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public Role get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            Role role = roleService.findOne(id);

            role.setOffice(new Office(role.getOfficeId()));
            role.setCompany(new Office(role.getCompanyId()));

            return role;
        } else {
            return new Role();
        }
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    public ResponseMessage findByPage(@Valid DataTableRequest input) {
        DataTableResponse<Role> rst = roleService.findAllRole(input);
        ResponseMessage message = ResponseMessage.created(rst).include(Role.class, "id",
                "companyId", "officeId", "name", "companyName", "officeName", "useable");
        return message.onlyData();
    }

    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public ResponseMessage findWithTree() {
        List<Role> roles = roleService.findAll();
        List<JsTree> r = Lists.newArrayList();

        roles.forEach(role -> {
            JsTree tree = new JsTree();
            tree.setParent("#");
            tree.setId(role.getId());
            tree.setText(role.getName());
            tree.setIcon("ace-icon fa fa-group yellow");
            r.add(tree);
        });

        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "checkName", method = RequestMethod.GET)
    public ResponseMessage checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return ResponseMessage.ok(true).onlyData();
        } else if (name != null && roleService.countRoleByName(name) == 0) {
            return ResponseMessage.ok(true).onlyData();
        }
        return ResponseMessage.ok(false).onlyData();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(Role role) {
        try {
            String cid = request.getParameter("company.id");
            if (StringUtils.isNotBlank(cid)) {
                role.setCompany(new Office(cid));
            }
            String oid = request.getParameter("office.id");
            if (StringUtils.isNotBlank(oid)) {
                role.setOffice(new Office(oid));
            }
            roleService.save(role);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception ex) {
            logger.error("User Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseMessage delete(@PathVariable(name = "id", required = true) String id) {
        try {
            roleService.deleteRole(id);
        } catch (Exception ex) {
            logger.error("Role delete faild", ex);
            return ResponseMessage.error(DELETE_FAILD);
        }
        return ResponseMessage.ok(DELETE_SUCCESS);
    }

    @RequestMapping(value = "assignmenu", method = RequestMethod.PUT)
    public ResponseMessage assignMenu(Role role, String menuIds) {
        int newNum = 0;
        String[] menus = menuIds.split(",");
        role.getMenuList().clear();
        for (int i = 0; i < menus.length; i++) {
            if (StringUtils.isBlank(menus[i]) || "#".equals(menus[i])) continue;
            role.getMenuList().add(new Menu(menus[i]));
            newNum++;
        }
        try {
            roleService.save(role);
            return ResponseMessage.ok("已成功分配 " + newNum + " 个权限");
        } catch (Exception ex) {
            logger.error("Role Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "roleuser", method = RequestMethod.GET)
    public ResponseMessage getUsersByRole(@RequestParam(value = "roleid") String id, @Valid DataTableRequest input) {
        PageRequest pageRequest = new PageRequest(input.getStart(), input.getLength());
        Page<User> page = roleService.findRoleUser(id, pageRequest);
        DataTableResponse<User> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());
        r.setData(page.getContent());
        r.setRecordsTotal(page.getTotalElements());
        r.setRecordsFiltered(page.getTotalElements());
        return ResponseMessage.ok(r).include(User.class, "id", "companyId", "officeId", "companyName",
                "officeName", "loginName", "name").onlyData();
    }

    @RequestMapping(value = "assignrole", method = RequestMethod.POST)
    public ResponseMessage assignRole(Role role, String[] idsArr) {
        int newNum = 0;
        for (int i = 0; i < idsArr.length; i++) {
            User user = userService.findOne(idsArr[i]);
            if (null != user && !role.getUserList().contains(user)) {
                role.getUserList().add(user);
                roleService.save(role);
                newNum++;
            }
        }
        return ResponseMessage.ok("已成功分配 " + newNum + " 个用户");
    }

    @RequestMapping(value = "/outrole/{role}/{user}", method = RequestMethod.DELETE)
    public ResponseMessage outRole(@PathVariable(name = "role") Role role, @PathVariable(name = "user") String userId) {
        if (!UserUtils.getUser().isAdmin() && UserUtils.getUser().getId().equals(userId)) {
            return ResponseMessage.error("无法从角色【" + role.getName() + "】中移除用户【" + UserUtils.getUser().getName() + "】自己！");
        }
        role.getUserList().removeIf(new java.util.function.Predicate<User>() {
            @Override
            public boolean test(User user) {
                return userId.equals(user.getId());
            }
        });
        try {
            roleService.save(role);
            return ResponseMessage.ok("用户从角色【" + role.getName() + "】中移除成功！");
        } catch (Exception e) {
            logger.error("Role remove user Save Faild", e);
            return ResponseMessage.error("用户从角色【" + role.getName() + "】中移除失败！");
        }
    }

    @RequestMapping(value = "userrole", method = RequestMethod.GET)
    public ResponseMessage getRolesByUser(@RequestParam(value = "userid") String id, @Valid DataTableRequest input) {
        PageRequest pageRequest = new PageRequest(input.getStart(), input.getLength());
        Page<Role> page = roleService.findRoleByUser(id, pageRequest);
        DataTableResponse<Role> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());
        r.setData(page.getContent());
        r.setRecordsTotal(page.getTotalElements());
        r.setRecordsFiltered(page.getTotalElements());
        return ResponseMessage.ok(r).include(Role.class, "id", "companyId", "officeId", "companyName",
                "officeName", "name").onlyData();
    }

    @RequestMapping(value = "/office/{id}", method = RequestMethod.GET)
    public ResponseMessage getOfficeUser(@PathVariable(name = "id", required = true) String id) {
        List<Role> roles = roleService.findByOffice(id);
        List<JsTree> r = Lists.newArrayList();
        roles.forEach(u -> {
            JsTree tree = new JsTree();
            tree.setParent("#");
            tree.setId(u.getId());
            tree.setText(u.getName());
            tree.setIcon("fa fa-users yellow");

            r.add(tree);
        });

        return ResponseMessage.ok(r).onlyData();
    }
}

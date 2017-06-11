package com.pard.modules.sys.controller;

import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.message.ResponseMessage;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Area;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

/**
 * Created by wawe on 17/5/22.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/user")
public class UserRestController extends GenericController implements MessageConstant {
    @Autowired
    private UserService userService;

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

    @RequestMapping(value = "page", method = RequestMethod.GET)
    public ResponseMessage findByPage(@Valid DataTableRequest input) {
        DataTableResponse<User> rst = userService.findAll(input);

        return ResponseMessage.ok(rst)
                .exclude(User.class, "company.area", "office.area")
                .onlyData();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(User user) {
        try {
            ResponseMessage checkName = checkName(user.getOldName(), user.getLoginName());
            if (!checkName.isSuccess()) {
                return checkName;
            }
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
}

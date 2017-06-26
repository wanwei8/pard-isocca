package com.pard.modules.sys.controller;

import com.pard.common.controller.GenericController;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Area;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wawe on 17/5/22.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends GenericController {
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public Office get(@RequestParam(required = false) String id) {
        Office office;
        if (StringUtils.isNotBlank(id)) {
            office = officeService.findOne(id);
            if (office != null) {
                if (office.getParent() == null) {
                    office.setParent(new Office("0"));
                }
                if (office.getArea() == null) {
                    office.setArea(new Area());
                }
                return office;
            }
        }
        office = new Office();
        office.setParent(new Office("0"));
        office.setArea(new Area());
        return office;
    }

    @PreAuthorize("hasAuthority('sys:office:view')")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/sys/officeList";
    }

    @PreAuthorize("hasAnyAuthority('sys:office:view', 'sys:office:add', 'sys:office:edit')")
    @RequestMapping(value = "form")
    public String form(Office office, Model model) {
        model.addAttribute("office", office);
        return "modules/sys/officeForm";
    }
}

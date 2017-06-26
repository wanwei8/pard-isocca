package com.pard.modules.sys.controller;

import com.pard.common.controller.GenericController;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Area;
import com.pard.modules.sys.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wawe on 17/4/27.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends GenericController {
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public Area get(@RequestParam(required = false) String id) {
        Area area;
        if (StringUtils.isNotBlank(id)) {
            area = areaService.findOne(id);
            if (area != null) {
                if (area.getParent() == null) {
                    area.setParent(new Area("0"));
                }
                return area;
            }
        }
        area = new Area();
        area.setParent(new Area("0"));
        return area;
    }

    @PreAuthorize("hasAuthority('sys:area:view')")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/sys/areaList";
    }

    @PreAuthorize("hasAnyAuthority('sys:area:view', 'sys:area:add', 'sys:area:edit')")
    @RequestMapping(value = "form")
    public String form(Area area, Model model) {
        model.addAttribute("area", area);
        return "modules/sys/areaForm";
    }
}

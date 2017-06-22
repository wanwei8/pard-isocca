package com.pard.modules.sys.controller;

import com.pard.common.controller.GenericController;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Dict;
import com.pard.modules.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wawe on 17/5/3.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
@AccessLogger("数据字典")
public class DictController extends GenericController {
    @Autowired
    private DictService dictService;

    @ModelAttribute
    public Dict get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return dictService.findOne(id);
        }
        return new Dict();
    }

    @PreAuthorize("authenticated and hasAuthority('sys:dict:view')")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        return "modules/sys/dictList";
    }

    @PreAuthorize("authenticated and hasAnyAuthority('sys:dict:view','sys:dict:add','sys:dict:edit')")
    @RequestMapping(value = "form")
    public String form(Dict dict, Model model) {
        if (StringUtils.isBlank(dict.getId()) && StringUtils.isNotBlank(dict.getType())) {
            int sort = dictService.findMaxSortByType(dict.getType());
            dict.setSort(sort + 10);
        }
        model.addAttribute("dict", dict);
        return "modules/sys/dictForm";
    }
}

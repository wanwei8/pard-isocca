package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.message.ResponseMessage;
import com.pard.common.message.Select2;
import com.pard.modules.sys.entity.Dict;
import com.pard.modules.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by wawe on 17/5/4.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/dict")
public class DictRestController extends GenericController implements MessageConstant {

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseMessage findByPage(@Valid DataTableRequest input) {
        DataTableResponse<Dict> dicts = dictService.findAll(input);
        return ResponseMessage.ok(dicts).onlyData();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(Dict dict) {
        try {
            dictService.save(dict);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception ex) {
            logger.error("Dict Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseMessage delete(@PathVariable(name = "id", required = true) String id) {
        try {
            dictService.delete(id);
            return ResponseMessage.ok(DELETE_SUCCESS);
        } catch (Exception ex) {
            logger.error("Dict delete faild", ex);
            return ResponseMessage.error(DELETE_FAILD);
        }
    }

    @RequestMapping(value = "/type", method = RequestMethod.POST)
    public ResponseMessage type() {
        List<Select2> result = Lists.newArrayList();
        List<String> dicts = dictService.findDistinctDictType();
        for (String type : dicts) {
            result.add(new Select2(type));
        }
        return ResponseMessage.ok(result).onlyData();
    }

    @RequestMapping(value = "dict", method = RequestMethod.POST)
    public ResponseMessage findByType(String type) {
        List<Select2> result = Lists.newArrayList();
        List<Dict> dicts = dictService.findByType(type);
        for (Dict dict : dicts) {
            result.add(new Select2(dict.getValue(), dict.getLabel()));
        }
        return ResponseMessage.ok(result).onlyData();
    }
}

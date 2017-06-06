package com.pard.modules.sys.controller;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.Column;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.exception.ChildNodeExistsException;
import com.pard.common.message.JsTree;
import com.pard.common.message.ResponseMessage;
import com.pard.common.message.Select2;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Dict;
import com.pard.modules.sys.entity.Office;
import com.pard.modules.sys.service.DictService;
import com.pard.modules.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by wawe on 17/4/28.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/office")
public class OfficeRestController extends GenericController implements MessageConstant {
    @Autowired
    private OfficeService officeService;
    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public DataTableResponse<Office> getAreaList(@Valid DataTableRequest input) {
        Column column = input.getColumn("parent.id");
        List<Office> offices = officeService.findByParentId(column.getSearch().getValue());
        Set<Dict> dicts = Sets.newHashSet(dictService.findByType("sys_office_type"));
        for (Office a : offices) {
            Set<Dict> filter = Sets.filter(dicts, new com.google.common.base.Predicate<Dict>() {
                @Override
                public boolean apply(Dict dict) {
                    return dict.getValue().equals(a.getType());
                }
            });
            if (!filter.isEmpty()) {
                Dict dict = filter.iterator().next();
                a.setTypeLabel(dict.getLabel());
            }
        }
        DataTableResponse<Office> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());
        r.setData(offices);
        r.setRecordsTotal(offices.size());
        r.setRecordsFiltered(offices.size());
        return r;
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseMessage getJsTreeList(@RequestParam(name = "isall", defaultValue = "0") int isAll) {
        List<Office> lst = Lists.newArrayList(officeService.findAllWithTree());
        List<JsTree> r = Lists.newArrayList();
        if (isAll == 1) {
            JsTree root = new JsTree();
            root.setText("根节点");
            root.setId("0");
            root.setParent("#");
            root.setIcon("fa fa-home red");
            root.getState().setOpened(true);
            r.add(root);
        }
        for (Office office : lst) {
            JsTree tree = new JsTree();
            tree.setId(office.getId());
            String pid = office.getParentId();
            if (isAll != 1 && "0".equals(pid)) {
                pid = "#";
            }
            tree.setParent(pid);
            tree.setText(office.getName());
            tree.getState().setOpened(StringUtils.isEmpty(office.getParentIds()));
            tree.setIcon(office.getIcon());
            r.add(tree);
        }
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/select2tree", method = RequestMethod.POST)
    public ResponseMessage getSelect2TreeList(@RequestParam(name = "all", defaultValue = "1") int all, String extId) {
        List<Office> lst = officeService.findAllWithTree();
        List<Select2> r = Lists.newArrayList();
        if (all == 1) {
            r.add(new Select2("0", "根节点"));
        }
        r.addAll(officeService.getTreeSelect(lst, extId));
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/select2company", method = RequestMethod.POST)
    public ResponseMessage getSelect2CompanyList() {
        List<Office> lst = officeService.findCompanyWithTree();
        List<Select2> r = Lists.newArrayList();
        for (Office office : lst) {
            r.add(new Select2(office.getId(), office.getName()));
        }
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/select2office", method = RequestMethod.POST)
    public ResponseMessage getSelect2OfficeList(@RequestParam(name = "company") String cid) {
        List<Select2> r = Lists.newArrayList();
        if (StringUtils.isNotBlank(cid)) {
            List<Office> lst = officeService.findOfficeWithTree(cid);
            Collection<Office> parents = Collections2.filter(lst, x -> x.getId().equals(cid));
            for (Office parent : parents) {
                r.add(new Select2(parent.getId(), parent.getName()));
            }
            r.addAll(officeService.getTreeSelect(lst, new Office(cid)));
            for (Select2 select2 : r) {
                if ("0".equals(select2.getParent())) {
                    select2.setParent(null);
                }
            }
        }
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(Office office) {
        try {
            officeService.save(office);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception ex) {
            logger.error("Office Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseMessage delete(@PathVariable(name = "id", required = true) String id) {
        try {
            officeService.delete(id);
            return ResponseMessage.ok(DELETE_SUCCESS);
        } catch (ChildNodeExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Area delete faild", ex);
            return ResponseMessage.error(DELETE_FAILD);
        }
    }

    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public ResponseMessage updateSort(String[] ids, Integer[] sorts) {
        if (ids == null || sorts == null || ids.length == 0) {
            return ResponseMessage.ok(SAVE_SUCCESS);
        }
        if (ids.length != sorts.length) {
            throw new ChildNodeExistsException();
        }
        List<Office> updates = Lists.newArrayList();
        for (int i = 0; i < ids.length; i++) {
            Office office = new Office(ids[i]);
            office.setSort(sorts[i]);
            updates.add(office);
        }
        try {
            officeService.updateSort(updates);
        } catch (Exception e) {
            logger.error("Save menu sort faild!", e);
            return ResponseMessage.error(SAVE_FAILD);
        }
        return ResponseMessage.ok(SAVE_SUCCESS);
    }
}

package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.Column;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.exception.ChildNodeExistsException;
import com.pard.common.exception.RequestParameterException;
import com.pard.common.logger.annotation.AccessLogger;
import com.pard.common.message.JsTree;
import com.pard.common.message.ResponseMessage;
import com.pard.common.message.Select2;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Area;
import com.pard.modules.sys.entity.Dict;
import com.pard.modules.sys.service.AreaService;
import com.pard.modules.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Created by wawe on 17/4/28.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/area")
public class AreaRestController extends GenericController implements MessageConstant {
    @Autowired
    private AreaService areaService;
    @Autowired
    private DictService dictService;

    @AccessLogger("获取地区列表")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseMessage getAreaList(@Valid DataTableRequest input) {
        Column column = input.getColumn("parent.id");
        List<Area> areas = areaService.findByParentId(column.getSearch().getValue());
        Set<Dict> dicts = Sets.newHashSet(dictService.findByType("sys_area_type"));
        for (Area a : areas) {
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
        DataTableResponse<Area> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());
        r.setData(areas);
        r.setRecordsTotal(areas.size());
        r.setRecordsFiltered(areas.size());
        return ResponseMessage.ok(r).onlyData();
    }

    @AccessLogger("获取地区树")
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public ResponseMessage getJsTreeList(@RequestParam(name = "isall", defaultValue = "0") int isAll) {
        List<Area> lst = Lists.newArrayList(areaService.findAllWithTree());
        List<JsTree> r = Lists.newArrayList();
        if (isAll == 1) {
            JsTree root = new JsTree();
            root.setText("根节点");
            root.setId("0");
            root.setParent("#");
            root.setIcon("fa fa-home yellow");
            root.getState().setOpened(true);
            r.add(root);
        }
        for (Area area : lst) {
            JsTree tree = new JsTree();
            tree.setId(area.getId());
            String pid = area.getParentId();
            if (isAll != 1 && "0".equals(pid)) {
                pid = "#";
            }
            tree.setParent(pid);
            tree.setText(area.getName());
            tree.setIcon("fa fa-file yellow");
            tree.getState().setOpened(StringUtils.isEmpty(area.getParentIds()));
            r.add(tree);
        }
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "select2tree", method = RequestMethod.POST)
    public ResponseMessage getSelect2TreeList(@RequestParam(name = "all", defaultValue = "0") int all, String extId) {
        List<Area> lst = areaService.findAllWithTree();
        List<Select2> r = Lists.newArrayList();
        if (all == 1) {
            Select2 root = new Select2();
            root.setText("根节点");
            root.setId("0");
            r.add(root);
        }
        r.addAll(areaService.getTreeSelect(lst, extId));
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(Area area) {
        try {
            areaService.save(area);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception ex) {
            logger.error("Area Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseMessage delete(@PathVariable(name = "id", required = true) String id) {
        try {
            areaService.delete(id);
        } catch (ChildNodeExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Area delete faild", ex);
            return ResponseMessage.error(DELETE_FAILD);
        }

        return ResponseMessage.ok(DELETE_SUCCESS);
    }

    @RequestMapping(value = "sort", method = RequestMethod.POST)
    public ResponseMessage updateSort(String[] ids, Integer[] sorts) {
        if (ids.length != sorts.length) {
            throw new RequestParameterException();
        }
        List<Area> updates = Lists.newArrayList();
        for (int i = 0; i < ids.length; i++) {
            Area area = new Area(ids[i]);
            area.setSort(sorts[i]);
            updates.add(area);
        }
        try {
            areaService.updateSort(updates);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception e) {
            logger.error("Save menu sort faild!", e);
            throw new ChildNodeExistsException();
        }
    }
}

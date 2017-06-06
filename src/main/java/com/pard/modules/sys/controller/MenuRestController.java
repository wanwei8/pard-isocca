package com.pard.modules.sys.controller;

import com.google.common.collect.Lists;
import com.pard.common.constant.MessageConstant;
import com.pard.common.controller.GenericController;
import com.pard.common.datatables.Column;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.exception.ChildNodeExistsException;
import com.pard.common.exception.RequestParameterException;
import com.pard.common.message.JsTree;
import com.pard.common.message.ResponseMessage;
import com.pard.common.message.Select2;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.IconInfo;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.repository.IconInfoRepository;
import com.pard.modules.sys.service.MenuService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by wawe on 17/5/15.
 */
@RestController
@RequestMapping(value = "${apiPath}/sys/menu")
public class MenuRestController extends GenericController implements MessageConstant {
    @Autowired
    private IconInfoRepository iconInfoRepository;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public DataTableResponse<Menu> getAreaList(@Valid DataTableRequest input) {
        Column column = input.getColumn("parent.id");
        String pid = column.getSearch().getValue();
        if (StringUtils.isBlank(pid))
            pid = "0";
        List<Menu> menus = menuService.findByParentId(pid);

        DataTableResponse<Menu> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());
        r.setData(menus);
        r.setRecordsTotal(menus.size());
        r.setRecordsFiltered(menus.size());
        return r;
    }

    @RequestMapping(value = "select2tree", method = RequestMethod.POST)
    public ResponseMessage getSelect2TreeList(@RequestParam(name = "all", defaultValue = "1") int all, String extId) {
        List<Menu> lst = menuService.findAllMenu();
        List<Select2> r = Lists.newArrayList();
        if (all == 1) {
            Select2 root = new Select2();
            root.setText("功能菜单");
            root.setId("0");
            r.add(root);
        }
        r.addAll(menuService.getTreeSelect(lst, extId));
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseMessage getJsTreeList(@RequestParam(name = "all", defaultValue = "0") int all) {
        List<Menu> lst = menuService.findAllMenu();
        List<JsTree> r = Lists.newArrayList();
        if (all == 1) {
            JsTree root = new JsTree();
            root.setText("功能菜单");
            root.setId("0");
            root.setParent("#");
            root.setIcon("glyphicon glyphicon-home yellow");
            root.getState().setOpened(true);
            r.add(root);
        }
        for (Menu menu : lst) {
            JsTree tree = new JsTree();
            tree.setId(menu.getId());
            String pid = menu.getParentId();
            if (all != 1 && "0".equals(pid)) {
                pid = "#";
            }
            tree.setParent(pid);
            tree.setText(menu.getName());
            tree.setIcon("fa fa-file yellow");
            tree.getState().setOpened(StringUtils.isEmpty(menu.getParentIds()));
            r.add(tree);
        }
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseMessage save(Menu menu) {
        try {
            menuService.save(menu);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception ex) {
            logger.error("Menu Save Faild", ex);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "sort", method = RequestMethod.POST)
    public ResponseMessage updateSort(String[] ids, Integer[] sorts) {
        if (ids.length != sorts.length) {
            throw new RequestParameterException();
        }
        List<Menu> updates = Lists.newArrayList();
        for (int i = 0; i < ids.length; i++) {
            Menu menu = new Menu(ids[i]);
            menu.setSort(sorts[i]);
            updates.add(menu);
        }
        try {
            menuService.updateSort(updates);
            return ResponseMessage.ok(SAVE_SUCCESS);
        } catch (Exception e) {
            logger.error("Save menu sort faild!", e);
            return ResponseMessage.error(SAVE_FAILD);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseMessage delete(@PathVariable(name = "id", required = true) String id) {
        try {
            menuService.delete(id);
        } catch (ChildNodeExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Area delete faild", ex);
            return ResponseMessage.error(DELETE_FAILD);
        }
        return ResponseMessage.ok(DELETE_SUCCESS);
    }

    @RequestMapping(value = "/icon/category")
    public ResponseMessage getIconCategory() {
        List<JsTree> r = Lists.newArrayList();
        r.add(new JsTree("FontAwesome", "#", "FontAwesome", true));
        r.add(new JsTree("Glyphicons", "#", "Glyphicons"));
        return ResponseMessage.ok(r).onlyData();
    }

    @RequestMapping(value = "icon/list")
    public ResponseMessage findIconsWithPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                             @RequestParam(value = "size", defaultValue = "50") Integer size,
                                             String category, String icon) {
        Pageable pageable = new PageRequest(page, size);
        Specification<IconInfo> specification = new Specification<IconInfo>() {
            @Override
            public Predicate toPredicate(Root<IconInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                if (StringUtils.isNotBlank(category) && !"search".equals(category)) {
                    predicates.add(cb.equal(root.get("sourceType").as(String.class), category));
                }
                if (StringUtils.isNotBlank(icon)) {
                    predicates.add(cb.like(root.get("displayName").as(String.class), "%" + icon + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return ResponseMessage.ok(iconInfoRepository.findAll(specification, pageable));
    }

    @RequestMapping(value = "icon/generate")
    public ResponseMessage setBootstrapIconInfo() {
        String regex = "(?<=\\.)(.+?)(?=:before\\s*\\{)";
        String[] filePaths = new String[]{
                "classpath:static/css/font-awesome/css/font-awesome.css",
                "classpath:static/css/bootstrap.css"
        };
        List<IconInfo> dbInconInfoes = Lists.newArrayList();
        try {
            String realPath = ResourceUtils.getURL(filePaths[0]).getPath();
            String content = FileUtils.readFileToString(new java.io.File(realPath), "UTF-8");
            List<String> matchList1 = StringUtils.regexMatcherToList(regex, content);
            for (String str : matchList1) {
                IconInfo info = new IconInfo(IdGen.uuid());
                info.setClassName("fa " + str);
                info.setDisplayName(str);
                info.setSourceType("FontAwesome");
                dbInconInfoes.add(info);
            }

            realPath = ResourceUtils.getURL(filePaths[1]).getPath();
            content = FileUtils.readFileToString(new java.io.File(realPath), "UTF-8");
            List<String> matchList2 = StringUtils.regexMatcherToList(regex, content);
            for (String str : matchList2) {
                IconInfo info = new IconInfo(IdGen.uuid());
                info.setClassName("glyphicon " + str);
                info.setDisplayName(str);
                info.setSourceType("Glyphicons");
                dbInconInfoes.add(info);
            }

            iconInfoRepository.save(dbInconInfoes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseMessage.ok();
    }
}

package com.pard.common.datatables;

import com.google.common.collect.Maps;
import com.pard.common.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * Created by wawe on 17/5/4.
 */
@Component
public class DataTableResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(DataTableRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        DataTableRequest dataTableRequest = new DataTableRequest();
        dataTableRequest.setStart(StringUtils.toInt(request.getParameter("start"), 0));
        dataTableRequest.setLength(StringUtils.toInt(request.getParameter("length"), 25));
        dataTableRequest.setDraw(StringUtils.toInt(request.getParameter("draw"), 1));

        int idx = 0;
        Map<String, String[]> parameterMap = request.getParameterMap();
        // column
        while (parameterMap.containsKey("columns[" + idx + "][data]")) {
            Column col = new Column();
            if (parameterMap.containsKey("columns[" + idx + "][data]")) {
                col.setData(parameterMap.get("columns[" + idx + "][data]")[0]);
            }
            if (parameterMap.containsKey("columns[" + idx + "][name]")) {
                col.setName(parameterMap.get("columns[" + idx + "][name]")[0]);
            }
            if (parameterMap.containsKey("columns[" + idx + "][searchable]")) {
                col.setSearchable(Boolean.parseBoolean(parameterMap.get("columns[" + idx + "][searchable]")[0]));
            }
            if (parameterMap.containsKey("columns[" + idx + "][orderable]")) {
                col.setOrderable(Boolean.parseBoolean(parameterMap.get("columns[" + idx + "][orderable]")[0]));
            }
            if (parameterMap.containsKey("columns[" + idx + "][search][regex]")) {
                col.setSearchRegex(Boolean.parseBoolean(parameterMap.get("columns[" + idx + "][search][regex]")[0]));
            }
            if (parameterMap.containsKey("columns[" + idx + "][search][value]")) {
                col.setSearchValue(parameterMap.get("columns[" + idx + "][search][value]")[0]);
            }
            dataTableRequest.getColumns().add(col);
            idx++;
        }
        idx = 0;
        //order
        while (parameterMap.containsKey("order[" + idx + "][column]")) {
            Order ord = new Order();
            if (parameterMap.containsKey("order[" + idx + "][column]")) {
                ord.setColumn(Integer.parseInt(parameterMap.get("order[" + idx + "][column]")[0]));
            }
            if (parameterMap.containsKey("order[" + idx + "][dir]")) {
                ord.setDir(parameterMap.get("order[" + idx + "][dir]")[0]);
            }

            dataTableRequest.getOrder().add(ord);
            idx++;
        }

        if (parameterMap.containsKey("search[value]")) {
            dataTableRequest.getSearch().setValue(parameterMap.get("search[value]")[0]);
        }
        if (parameterMap.containsKey("search[regex]")) {
            dataTableRequest.getSearch().setRegex(Boolean.parseBoolean(parameterMap.get("search[regex]")[0]));
        }

        return dataTableRequest;
    }
}

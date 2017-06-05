package com.pard.common.validator;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 对象验证结果集合
 * Created by wawe on 17/5/30.
 */
public class ValidateResults extends ArrayList<ValidateResults.ValidResult> implements Serializable {
    private boolean success = true;

    @Override
    public boolean addAll(Collection<? extends ValidResult> c) {
        success = false;
        return super.addAll(c);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public void addResult(String field, String message) {
        this.add(new ValidResult(field, message));
    }

    /**
     * 单个属性验证结果
     */
    public class ValidResult {
        public ValidResult() {

        }

        public ValidResult(String field, String message) {
            this.field = field;
            this.message = message;
        }

        private String field;
        private String message;

        public void setField(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("{\"%s\":\"%s\"}", getField(), getMessage());
        }
    }

    public boolean isSuccess() {
        return success;
    }
}

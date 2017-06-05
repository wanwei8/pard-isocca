package com.pard.common.message;

import java.io.Serializable;

/**
 * Created by wawe on 17/5/6.
 */
public class Select2 implements Serializable {

    private String id;

    private String text;

    private String parent;

    public Select2() {

    }

    public Select2(String id) {
        this.id = id;
        this.text = id;
    }

    public Select2(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}

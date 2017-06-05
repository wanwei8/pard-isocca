package com.pard.common.message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wawe on 17/4/28.
 */
public class JsTree implements Serializable {

    private String id;

    private String parent;

    private String text;

    private String icon;

    private JsTreeState state;

    public JsTree() {
        state = new JsTreeState();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public JsTreeState getState() {
        return state;
    }

    public void setState(JsTreeState state) {
        this.state = state;
    }

    public class JsTreeState {
        private boolean opened;
        private boolean disabled;
        private boolean selected;

        public boolean getOpened() {
            return opened;
        }

        public void setOpened(boolean opened) {
            this.opened = opened;
        }

        public boolean getDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public boolean getSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}

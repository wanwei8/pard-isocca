package com.pard.common.exception;

public class ChildNodeExistsException extends ValidationException {
    public ChildNodeExistsException() {
        super("此节点下含有子节点");
    }

}

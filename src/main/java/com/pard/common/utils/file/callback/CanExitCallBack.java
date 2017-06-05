package com.pard.common.utils.file.callback;

/**
 * Created by wawe on 17/5/31.
 */
public interface CanExitCallBack {
    default void exit() {
    }

    default boolean isExit() {
        return false;
    }
}

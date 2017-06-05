package com.pard.common.utils.file.callback;

import java.io.File;

/**
 * Created by wawe on 17/5/31.
 */
public interface ScanCallBack extends CanExitCallBack {

    void accept(int deep, File file);

    default void error(int deep, File file, Throwable e) {
        e.printStackTrace();
    }

}

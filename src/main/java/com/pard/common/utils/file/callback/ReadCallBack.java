package com.pard.common.utils.file.callback;

/**
 * Created by wawe on 17/5/31.
 */
public interface ReadCallBack extends CanExitCallBack {

    void readLine(int lineNumber, String line);

    default void error(Throwable e) {
        e.printStackTrace();
    }

    default void done(int total) {
    }

}

package com.pard.common.utils.file.callback;

/**
 * Created by wawe on 17/5/31.
 */
public abstract class AbstractScanCallBack implements ScanCallBack {

    private boolean exit = false;

    @Override
    public void exit() {
        exit = true;
    }

    @Override
    public boolean isExit() {
        return exit;
    }

}

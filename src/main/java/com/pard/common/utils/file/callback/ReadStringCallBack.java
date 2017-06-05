package com.pard.common.utils.file.callback;

/**
 * Created by wawe on 17/5/31.
 */
public class ReadStringCallBack implements ReadCallBack {

    protected StringBuilder builder = new StringBuilder();

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public void readLine(int lineNumber, String line) {
        builder.append(line).append("\n");
    }
}

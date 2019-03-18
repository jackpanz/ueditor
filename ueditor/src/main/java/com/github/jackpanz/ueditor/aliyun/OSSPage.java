package com.github.jackpanz.ueditor.aliyun;

import java.util.ArrayList;

/**
 * Created by bj on 2017/6/25.
 */
public class OSSPage<E> extends ArrayList {

    String newxNextMarker;
    int maxKeys;

    public String getNewxNextMarker() {
        return newxNextMarker;
    }

    public void setNewxNextMarker(String newxNextMarker) {
        this.newxNextMarker = newxNextMarker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }
}

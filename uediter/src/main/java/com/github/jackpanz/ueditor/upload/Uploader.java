package com.github.jackpanz.ueditor.upload;

import com.github.jackpanz.ueditor.define.State;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class Uploader {
    private HttpServletRequest request = null;
    private Map<String, Object> conf = null;
    private FileManager fileManager = null;

    public Uploader(HttpServletRequest request, Map<String, Object> conf, FileManager fileManager) {
        this.request = request;
        this.conf = conf;
        this.fileManager = fileManager;
    }

    public final State doExec() {
        String filedName = (String) this.conf.get("fieldName");
        State state = null;

        if ("true".equals(this.conf.get("isBase64"))) {
            state = fileManager.saveBase64(this.request.getParameter(filedName), this.conf);
        } else {
            state = fileManager.save(this.request, this.conf);
        }

        return state;
    }
}

package com.github.jackpanz.ueditor.upload;

import com.github.jackpanz.ueditor.define.State;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

public interface FileManager {

    int BUFFER_SIZE = 8192;

    State save(HttpServletRequest request, Map<String, Object> conf);

    State listFile(int index, Map<String, Object> conf);

    State saveBinaryFile(byte[] data, String path);

    State saveFileByInputStream(InputStream is, String path, long maxSize);

    State saveBase64(String content, Map<String, Object> conf);

}

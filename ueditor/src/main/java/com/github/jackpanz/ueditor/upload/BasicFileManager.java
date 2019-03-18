package com.github.jackpanz.ueditor.upload;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class BasicFileManager implements FileManager {

    public static File getTmpFile() {
        File tmpDir = FileUtils.getTempDirectory();
        String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
        return new File(tmpDir, tmpFileName);
    }

    public static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }

    public static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    public static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}

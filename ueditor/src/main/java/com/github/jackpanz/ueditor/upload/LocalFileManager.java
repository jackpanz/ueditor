package com.github.jackpanz.ueditor.upload;

import com.github.jackpanz.ueditor.PathFormat;
import com.github.jackpanz.ueditor.define.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class LocalFileManager extends BasicFileManager implements FileManager {

    public static File local = null;

    public static String access = null;

    @Override
    public State listFile(int index, Map<String, Object> conf) {

        File dirFile = new File(local.getPath(), (String) conf.get("dir"));
        String[] allowFiles = getAllowFiles(conf.get("allowFiles"));
        int count = (Integer) conf.get("count");
        State state = null;

        if (!dirFile.exists()) {
            return new BaseState(false, AppInfo.NOT_EXIST);
        }

        if (!dirFile.isDirectory()) {
            return new BaseState(false, AppInfo.NOT_DIRECTORY);
        }

        Collection<File> list = FileUtils.listFiles(dirFile, allowFiles, true);

        if (index < 0 || index > list.size()) {
            state = new MultiState(true);
        } else {
            Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + count);
            state = this.getState(fileList, local);
        }

        state.putInfo("start", index);
        state.putInfo("total", list.size());

        return state;

    }

    @Override
    public State saveBinaryFile(byte[] data, String path) {
        File file = new File(local, path);
        State state = valid(file);
        if (!state.isSuccess()) {
            return state;
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bos.write(data);
            bos.flush();
            bos.close();
        } catch (IOException ioe) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

        state = new BaseState(true, file.getAbsolutePath());
        state.putInfo("size", data.length);
        state.putInfo("title", file.getName());
        return state;
    }

    @Override
    public State saveFileByInputStream(InputStream is, String path, long maxSize) {
        State state = null;

        File tmpFile = getTmpFile();

        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), BUFFER_SIZE);
            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            bos.close();

            if (tmpFile.length() > maxSize) {
                tmpFile.delete();
                return new BaseState(false, AppInfo.MAX_SIZE);
            }

            File targetFile = new File(path);
            if (targetFile.canWrite()) {
                state = new BaseState(false, AppInfo.PERMISSION_DENIED);
            } else {
                try {
                    FileUtils.moveFile(tmpFile, targetFile);
                    state = new BaseState(true);
                    state.putInfo("size", targetFile.length());
                    state.putInfo("title", targetFile.getName());
                } catch (IOException e) {
                    state = new BaseState(false, AppInfo.IO_ERROR);
                }
            }

            if (!state.isSuccess()) {
                tmpFile.delete();
            }

            return state;

        } catch (IOException e) {
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    @Override
    public State saveBase64(String content, Map<String, Object> conf) {
        byte[] data = decode(content);

        long maxSize = ((Long) conf.get("maxSize")).longValue();

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");

        String savePath = PathFormat.parse((String) conf.get("savePath"),
                (String) conf.get("filename"));

        savePath = savePath + suffix;

        State storageState = saveBinaryFile(data, savePath);

        if (storageState.isSuccess()) {
            storageState.putInfo("url", PathFormat.format(access + "/" + savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }

        return storageState;
    }

    private String[] getAllowFiles(Object fileExt) {

        String[] exts = null;
        String ext = null;

        if (fileExt == null) {
            return new String[0];
        }

        exts = (String[]) fileExt;

        for (int i = 0, len = exts.length; i < len; i++) {

            ext = exts[i];
            exts[i] = ext.replace(".", "");

        }

        return exts;

    }

    private State getState(Object[] files, File local) {
        MultiState state = new MultiState(true);
        BaseState fileState = null;
        File file = null;
        for (Object obj : files) {
            if (obj == null) {
                break;
            }
            file = (File) obj;
            fileState = new BaseState(true);
//            System.out.println(file.getAbsolutePath());
//            System.out.println(local.getAbsolutePath());
            String path = file.getAbsolutePath().replace(local.getAbsolutePath(), "");
            fileState.putInfo("url", PathFormat.format(access + "/" + path));
            state.addState(fileState);
        }
        return state;
    }

    private static State valid(File file) {
        File parentPath = file.getParentFile();

        if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
            return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
        }

        if (!parentPath.canWrite()) {
            return new BaseState(false, AppInfo.PERMISSION_DENIED);
        }

        return new BaseState(true);
    }


}

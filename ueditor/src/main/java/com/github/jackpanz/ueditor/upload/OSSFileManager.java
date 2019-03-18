package com.github.jackpanz.ueditor.upload;

import com.aliyun.oss.model.OSSObjectSummary;
import com.github.jackpanz.ueditor.PathFormat;
import com.github.jackpanz.ueditor.aliyun.OSSConfig;
import com.github.jackpanz.ueditor.aliyun.OSSPage;
import com.github.jackpanz.ueditor.aliyun.OSSUtils;
import com.github.jackpanz.ueditor.define.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class OSSFileManager extends BasicFileManager {

    private static Map<String, Map<Integer, String>> markerMap = new HashMap<>();

    @Override
    public State listFile(int index, Map<String, Object> conf) {

        int count = (int) conf.get("count");
        String dir = (String) conf.get("dir");
        Map<Integer, String> markers = markerMap.get(dir);
        if (markers == null) {
            markers = new HashMap();
            markerMap.put(dir, markers);
        }

        MultiState state = new MultiState(true);
        String pageMarker = null;
        String resultMarker = null;
        OSSPage<OSSObjectSummary> ossPage = null;

        if (index != 0) {
            pageMarker = markers.get(index);
        }

//        System.out.println("index=" + index);
//        System.out.println("count=" + count);
//        System.out.println("pageMarker=" + pageMarker);

        if (index == 0 || pageMarker != null) {
            ossPage = OSSUtils.listPage((String) conf.get("dir"), pageMarker, count);
            resultMarker = ossPage.getNewxNextMarker();
        }

        if (ossPage == null || ossPage.isEmpty()) {
            state.putInfo("start", index);
            state.putInfo("total", index);
            markers.remove(index);
            return state;
        }

        if (resultMarker != null) {
            state.putInfo("start", index);
            state.putInfo("total", (index + count + count));
            markers.put((index + count), resultMarker);
//            System.out.println("start=" + index);
//            System.out.println("total=" + (index + count + count));
//            System.out.println("put=" + (index + count) + "," + resultMarker);
        } else {
            state.putInfo("start", index);
            state.putInfo("total", (index + ossPage.size()));
            markers.remove(index + count);
//            System.out.println("start=" + index);
//            System.out.println("total=" + (index + ossPage.size()));
//            System.out.println("remove=" + (index + count));
        }

        for (int i = 0; i < ossPage.size(); i++) {
            OSSObjectSummary ossObjectSummary = (OSSObjectSummary) ossPage.get(i);
            BaseState fileState = new BaseState(true);
            fileState.putInfo("url", PathFormat.format(OSSConfig.image_access + "/" + ossObjectSummary.getKey()));
            state.addState(fileState);
        }

        return state;

    }

    @Override
    public State saveBinaryFile(byte[] data, String path) {
        State state = null;
        long length = data.length;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            OSSUtils.uploadToOSS(path, byteArrayInputStream);
        } catch (IOException ioe) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

        state = new BaseState(true);
        state.putInfo("size", length);
        state.putInfo("title", StringUtils.substringAfterLast(path, "/"));
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
            } else{
                try (FileInputStream fileInputStream = new FileInputStream(tmpFile)) {
                    OSSUtils.uploadToOSS(path, fileInputStream);
                    state = new BaseState(true);
                    state.putInfo("size", tmpFile.length());
                    state.putInfo("title", StringUtils.substringAfterLast(path, "/"));
                } catch (Exception e) {
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
        //String physicalPath = (String) conf.get("rootPath") + savePath;

        State storageState = saveBinaryFile(data, savePath);

        if (storageState.isSuccess()) {
            storageState.putInfo("url",  PathFormat.format(OSSConfig.image_access + "/" + savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }

        return storageState;
    }

}

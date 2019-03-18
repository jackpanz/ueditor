package com.github.jackpanz.ueditor.upload.spring;

import com.github.jackpanz.ueditor.PathFormat;
import com.github.jackpanz.ueditor.aliyun.OSSConfig;
import com.github.jackpanz.ueditor.define.AppInfo;
import com.github.jackpanz.ueditor.define.BaseState;
import com.github.jackpanz.ueditor.define.FileType;
import com.github.jackpanz.ueditor.define.State;
import com.github.jackpanz.ueditor.upload.OSSFileManager;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class OSSSpringFileManager extends OSSFileManager {

    @Override
    public State save(HttpServletRequest request, Map<String, Object> conf) {

        try {
            AbstractMultipartHttpServletRequest standardMultipartHttpServletRequest = (AbstractMultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multipartFileMap = standardMultipartHttpServletRequest.getMultiFileMap();

            if (multipartFileMap == null || multipartFileMap.isEmpty()) {
                return new BaseState(false, 7);
            }

            MultipartFile multipartFile = null;
            Iterator<MultipartFile> iterator = multipartFileMap.toSingleValueMap().values().iterator();
            if (!iterator.hasNext()) {
                return new BaseState(false, 7);
            }

            multipartFile = iterator.next();


            String savePath = (String) conf.get("savePath");
            String originFileName = multipartFile.getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = ((Long) conf.get("maxSize")).longValue();

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            InputStream is = multipartFile.getInputStream();
            State storageState = saveFileByInputStream(is, savePath, maxSize);
            is.close();

            if (storageState.isSuccess()) {
                storageState.putInfo("url", PathFormat.format(OSSConfig.image_access + "/" + savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;

        } catch (IOException e) {

        } catch (Exception e) {
            return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

}

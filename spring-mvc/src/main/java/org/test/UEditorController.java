package org.test;

/**
 * Created by Administrator on 2017/4/14.
 */


import com.github.jackpanz.ueditor.ActionEnter;
import com.github.jackpanz.ueditor.aliyun.OSSConfig;
import com.github.jackpanz.ueditor.upload.spring.LocalSpringFileManager;
import com.github.jackpanz.ueditor.upload.spring.OSSSpringFileManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping({"/ueditor"})
public class UEditorController {

    private static int isLoad = 0;

    @ResponseBody
    @RequestMapping(value = "aliyun", method = {RequestMethod.POST, RequestMethod.GET})
    public String aliyun(HttpServletRequest request) {

        if ( isLoad++ == 0) {
            OSSConfig.image_access = "http://xxx.aliyuncs.com/";
            OSSConfig.accessKeyId = "accessKeyId";
            OSSConfig.accessKeySecret = "accessKeySecret";
            OSSConfig.endpoint = "endpoint";
            OSSConfig.bucketName = "bucketName";
        }

        String value = new ActionEnter(request, new OSSSpringFileManager()).exec();
        return value;
    }
}

package com.test;


import com.github.jackpanz.ueditor.ActionEnter;
import com.github.jackpanz.ueditor.upload.spring.LocalSpringFileManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping
public class UEditorController {


    @ResponseBody
    @RequestMapping(value = "/ueditor/aliyun", method = {RequestMethod.POST, RequestMethod.GET})
    public String aliyun(HttpServletRequest request) {
        String value = new ActionEnter(request, new LocalSpringFileManager(
                new File("D:/java/nginx-1.15.8/html"),
                "http://localhost/")
        ).exec();
        return value;
    }


}

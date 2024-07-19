package com.treesoft.system.web;

import com.treesoft.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping({"system/fileUpload"})
public class FileUploadController extends BaseController {
    @Autowired
    HttpServletRequest request;

    /**
     * 文件上传
     *
     * @param fileToUpload
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = {"i/fileUpload"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFile fileToUpload) {
        String filename = fileToUpload.getOriginalFilename();
        String realPathStr = request.getSession().getServletContext().getRealPath("/backup");
        File realPath = new File(realPathStr);
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        File writeFile = new File(realPath + File.separator + filename);
        Map<String, Object> map = new HashMap<>();
        String mess = "";
        String status = "";
        try {
            FileCopyUtils.copy(fileToUpload.getBytes(), writeFile);
            mess = "文件上传完成！";
            status = "success";
        } catch (IOException e) {
            mess = e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}

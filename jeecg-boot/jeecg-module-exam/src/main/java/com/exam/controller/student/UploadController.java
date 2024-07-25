package com.exam.controller.student;


import com.exam.base.BaseApiController;
import com.exam.base.RestResponse;
import com.exam.service.FileUpload;
import com.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;


@RequestMapping("/api/student/upload")
@RestController("StudentUploadController")
public class UploadController extends BaseApiController {

    private final FileUpload fileUpload;
    private final UserService userService;

    @Autowired
    public UploadController(FileUpload fileUpload, UserService userService) {
        this.fileUpload = fileUpload;
        this.userService = userService;
    }


    @RequestMapping("/image")
    @ResponseBody
    public RestResponse questionUploadAndReadExcel(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        long attachSize = multipartFile.getSize();
        String imgName = multipartFile.getOriginalFilename();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String filePath = fileUpload.uploadFile(inputStream, attachSize, imgName);
            userService.changePicture(getCurrentUser(), filePath);
            return RestResponse.ok(filePath);
        } catch (IOException e) {
            return RestResponse.fail(2, e.getMessage());
        }
    }


}

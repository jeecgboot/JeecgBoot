package com.exam.service;


import java.io.InputStream;

public interface FileUpload {

    String uploadFile(InputStream inputStream, long size, String extName);

}

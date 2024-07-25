package com.shop.controller;

import com.shop.common.core.annotation.OperLog;
import com.shop.common.core.utils.FileUploadUtil;
import com.shop.common.core.utils.PicUtils;
import com.shop.common.core.web.JsonResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件服务器
 * 2018-12-24 16:10
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    public HttpServletRequest request;

    @RequiresPermissions("sys:file:view")
    @RequestMapping("/manage")
    public String view() {
        return "system/file.html";
    }

    /**
     * 上传文件
     */
    @OperLog(value = "文件管理", desc = "上传文件", param = false, result = true)
    @ResponseBody
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) {
        return FileUploadUtil.upload(file, request);
    }

    /**
     * 上传base64文件
     */
    @OperLog(value = "文件管理", desc = "上传base64文件", param = false, result = true)
    @ResponseBody
    @PostMapping("/upload/base64")
    public JsonResult uploadBase64(String base64) {
        return FileUploadUtil.upload(base64, request);
    }

    @ResponseBody
    @GetMapping("/enQrcode")
    public void enQrcode(HttpServletResponse resp, String url) throws IOException {
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 240;//图片的宽度
                int height = 240;//高度

                /*
                 * 定义二维码的参数
                 */
                HashMap hashMap = new HashMap();
                // 设置二维码字符编码
                hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                // 设置二维码纠错等级
                hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
                // 设置二维码边距
                hashMap.put(EncodeHintType.MARGIN, 1);

                stream = resp.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width, hashMap);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }

    @PostMapping("/import")
    @ResponseBody
    public void importZip(@RequestParam("file") MultipartFile file) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream(), Charset.forName("gbk"));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                // do nothing
                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                byte[] extra = zipEntry.getExtra();
                if (size == -1) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while (true) {
                        int bytes = zipInputStream.read();
                        if (bytes == -1) break;
                        baos.write(bytes);
                    }
                    baos.close();
                } else {
                    byte[] bytes = new byte[(int) zipEntry.getSize()];
                    zipInputStream.read(bytes, 0, (int) zipEntry.getSize());
                }
            }
        }
        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    /**
     * 预览文件
     */
    @GetMapping("/{dir}/{name:.+}")
    public void file(@PathVariable("dir") String dir, @PathVariable("name") String name, Integer width, Integer height, HttpServletResponse response) {
        if (width != null && width > 0 && height != null && height > 0) {
            PicUtils.changeSize(dir + "/" + name, width, height, response);
        } else {
            FileUploadUtil.preview(dir + "/" + name, response);
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{dir}/{name:.+}")
    public void downloadFile(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.download(dir + "/" + name, response);
    }

    /**
     * 查看缩略图
     */
    @GetMapping("/thumbnail/{dir}/{name:.+}")
    public void smFile(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.thumbnail(dir + "/" + name, response);
    }

    /**
     * 删除文件
     */
    @OperLog(value = "文件管理", desc = "删除文件", result = true)
    @RequiresPermissions("sys:file:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(String path) {
        if (path == null || path.trim().isEmpty()) {
            return JsonResult.error("参数不能为空");
        }
        if (FileUploadUtil.delete(path)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 查询文件列表
     */
    @OperLog(value = "文件管理", desc = "查询全部")
    @RequiresPermissions("sys:file:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(String dir) {
        List<Map<String, Object>> list = FileUploadUtil.list(dir);
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return ((Long) o2.get("updateTime")).compareTo((Long) o1.get("updateTime"));
            }
        });
        return JsonResult.ok().setData(list);
    }

}

package org.springframework.base.system.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

@Slf4j
public class FileUtil {

    public static void ZipFiles(File srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            @Cleanup
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            @Cleanup
            FileInputStream in = new FileInputStream(srcfile);
            out.putNextEntry(new ZipEntry(srcfile.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void unZipFiles(File zipfile, String descDir)
            throws Exception {
        ZipFile zf = new ZipFile(zipfile);
        for (Enumeration entries = zf.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            @Cleanup
            InputStream in = zf.getInputStream(entry);
            @Cleanup
            OutputStream out = new FileOutputStream(descDir + zipEntryName);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
        }
    }

    public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
        // if (!srcRarPath.toLowerCase().endsWith(".rar"))
        // {
        // return;
        // }
        // File dstDiretory = new File(dstDirectoryPath);
        // if (!dstDiretory.exists())
        // {
        // dstDiretory.mkdirs();
        // }
        // Archive a = null;
        // try
        // {
        // a = new Archive(new File(srcRarPath));
        // if (a != null)
        // {
        // a.getMainHeader().print();
        // FileHeader fh = a.nextFileHeader();
        // while (fh != null)
        // {
        // if (fh.isDirectory())
        // {
        // File fol = new File(dstDirectoryPath + File.separator + fh.getFileNameString());
        // fol.mkdirs();
        // }
        // else
        // {
        // File out = new File(dstDirectoryPath + File.separator + fh.getFileNameString().trim());
        // try
        // {
        // if (!out.exists())
        // {
        // if (!out.getParentFile().exists())
        // {
        // out.getParentFile().mkdirs();
        // }
        // out.createNewFile();
        // }
        // FileOutputStream os = new FileOutputStream(out);
        // a.extractFile(fh, os);
        // os.close();
        // }
        // catch (Exception ex)
        // {
        // ex.printStackTrace();
        // }
        // }
        // fh = a.nextFileHeader();
        // }
        // a.close();
        // }
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
    }
}

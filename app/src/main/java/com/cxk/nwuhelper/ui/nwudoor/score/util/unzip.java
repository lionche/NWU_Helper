package com.cxk.nwuhelper.ui.nwudoor.score.util;

import android.util.Log;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class unzip {
    public static String unzipPdf(File zipFile, String descDir) {
        ZipArchiveEntry entry = null;
        try (ZipArchiveInputStream inputStream = getZipFile(zipFile)) {
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            while ((entry = inputStream.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(descDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
//                        os = new BufferedOutputStream(new FileOutputStream(new File(descDir, entry.getName())));
                        os = new BufferedOutputStream(new FileOutputStream(new File(descDir, "temp.pdf")));
//                        Log.d("pdffile","temp.pdf");

                        //输出文件路径信息
                        Log.d("解压文件的当前路径为:", descDir + "/temp.pdf");
                        IOUtils.copy(inputStream, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }

        } catch (Exception e) {
//            LOG.error("[unzip] 解压zip文件出错", e);
        }
        return "temp.pdf";
    }

    private static ZipArchiveInputStream getZipFile(File zipFile) throws Exception {
        return new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
    }
}

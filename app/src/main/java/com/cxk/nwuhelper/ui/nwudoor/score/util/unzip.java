package com.cxk.nwuhelper.ui.nwudoor.score.util;

import android.util.Log;

import com.tom_roush.pdfbox.io.IOUtils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

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
                        os = new BufferedOutputStream(new FileOutputStream(new File(descDir, entry.getName())));
                        Log.d("pdffile",entry.getName());

                        //输出文件路径信息
//                        LOG.info("解压文件的当前路径为:{}", descDir + entry.getName());
                        IOUtils.copy(inputStream, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }

        } catch (Exception e) {
//            LOG.error("[unzip] 解压zip文件出错", e);
        }
        return entry.getName();
    }

    private static ZipArchiveInputStream getZipFile(File zipFile) throws Exception {
        return new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
    }
}

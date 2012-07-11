package org.jclouds.conferenceapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtility {
    public static void getZipFiles(InputStream inputZip, String destinationName) {
        try {
            byte[] buf = new byte[1024];
            ZipInputStream zipInputStream = new ZipInputStream(inputZip);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                //for each entry to be extracted
                String entryName = destinationName + zipEntry.getName();
                entryName = entryName.replace('/', File.separatorChar);
                entryName = entryName.replace('\\', File.separatorChar);
                FileOutputStream fileOutputStream;
                File newFile = new File(entryName);
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    fileOutputStream = new FileOutputStream(entryName);
                    int n;
                    while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
                        fileOutputStream.write(buf, 0, n);
                    }
                    fileOutputStream.close();
                    zipInputStream.closeEntry();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

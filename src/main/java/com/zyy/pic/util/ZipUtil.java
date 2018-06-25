package com.zyy.pic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {  
	  
    public static boolean doZip(String filesDirPath, String zipFilePath) {  
        return doZip(new File(filesDirPath), zipFilePath);  
    }  
  
    private static boolean doZip(File inputFile, String zipFileName) {  
        ZipOutputStream out = null;  
        try {  
            out = new ZipOutputStream(new FileOutputStream(new File(zipFileName)));  
            boolean result = doZip(out, inputFile, "");  
  
            return result;  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return false;  
        } finally {  
            try {  
                out.close();  
            } catch (IOException e) {  
                return false;  
            }  
        }  
    }  
  
    private static boolean doZip(ZipOutputStream out, File f, String base) {  
        try {  
            if (f.isDirectory()) {  
                File[] fl = f.listFiles();  
                out.putNextEntry(new ZipEntry(base + "/"));  
                base = base.length() == 0 ? "" : base + "/";  
                for (int i = 0; i < fl.length; i++) {  
                    doZip(out, fl[i], base + fl[i].getName());  
                }  
            } else {  
                out.putNextEntry(new ZipEntry(base));
                FileInputStream in = new FileInputStream(f);  
                int b;  
                while ((b = in.read()) != -1) {  
                    out.write(b);  
                }  
                in.close();  
            }  
            return true;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
  
    public static boolean unZip(String srcFile, String dest, boolean deleteFile) {  
        try {  
            File file = new File(srcFile);  
            if (!file.exists()) {  
                return false;  
            }  
            ZipFile zipFile = new ZipFile(file);  
            Enumeration<?> e = zipFile.getEntries();  
            while (e.hasMoreElements()) {  
                ZipEntry zipEntry = (ZipEntry) e.nextElement();  
                if (zipEntry.isDirectory()) {  
                    String name = zipEntry.getName();  
                    name = name.substring(0, name.length() - 1);  
                    File f = new File(dest + name);  
                    f.mkdirs();  
                } else {  
                    File f = new File(dest + zipEntry.getName());  
                    f.getParentFile().mkdirs();  
                    f.createNewFile();  
                    InputStream is = zipFile.getInputStream(zipEntry);  
                    FileOutputStream fos = new FileOutputStream(f);  
                    int length = 0;  
                    byte[] b = new byte[1024];  
                    while ((length = is.read(b, 0, 1024)) != -1) {  
                        fos.write(b, 0, length);  
                    }  
                    is.close();  
                    fos.close();  
                }  
            }  
  
            if (zipFile != null) {  
                zipFile.close();  
            }  
  
            if (deleteFile) {  
                file.deleteOnExit();  
            }  
  
            return true;  
        } catch (IOException e) {  
            return false;  
        }  
    }  
}  

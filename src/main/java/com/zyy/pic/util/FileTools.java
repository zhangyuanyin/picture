package com.zyy.pic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/12/19.
 */
public class FileTools {
    public static void mergePath(String uri){
        Path path = Paths.get(uri);
        if(Files.notExists(path)){
            System.out.println("文件不存在");
            return;
        }
        try{

            DirectoryStream<Path> txtFilter  = Files.newDirectoryStream(path, "*.{txt}");
            ArrayList<String> list = new ArrayList<String>();
            for(Path p: txtFilter){
                list.add(uri+p.getFileName());
                System.out.println(p.getFileName());
            }
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            mergeFiles(path+"all.txt",arr);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public static void mergeFiles(String outFile, String[] files) {
        final int BUFSIZE = 1024 * 8;
        FileChannel outChannel = null;
        System.out.println("Merge " + Arrays.toString(files) + " into " + outFile);
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for(String f : files){
                Charset charset=Charset.forName("utf-8");
                CharsetDecoder chdecoder=charset.newDecoder();
                CharsetEncoder chencoder=charset.newEncoder();
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                CharBuffer charBuffer=chdecoder.decode(bb);
                ByteBuffer nbuBuffer=chencoder.encode(charBuffer);

                while(fc.read(nbuBuffer) != -1){
                    bb.flip();
                    nbuBuffer.flip();
                    outChannel.write(nbuBuffer);
                    bb.clear();
                    nbuBuffer.clear();
                }
                fc.close();
            }
            System.out.println("Merged!! ");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }
    
    /**
     * 清空临时文件夹
     * @param filePath
     */
	public static void clearFilePath(String filePath) {
		File oldFile = new File(filePath);
		String[] paths = oldFile.list();
		for (String path : paths) {
			File oldFilePath = new File(filePath+path+"/");
			File[] oldFiles = oldFilePath.listFiles();
			for (File f : oldFiles) {
				if(f.isDirectory()) {
					File[] files = f.listFiles();
					for (File file : files) {
						file.delete();
					}
				}
				f.delete();
			}
			oldFilePath.delete();
		}
	}
}

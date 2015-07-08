package com.ncpzs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;

 

public class FileUtil {

	/**
	 * 创建目录
	 * 
	 * @param path
	 */

	public static void createDirectory(String path) {
		File fl = new File(path);
		if (!fl.exists()) {
			if (fl.getParentFile().exists())
				fl.mkdir();
			else {
				createDirectory(fl.getParent());
				fl.mkdir();
			}
		}
	}

	/**
	 * 
	 * @param pathname
	 * @param content
	 * @return
	 */
	public static boolean writerFile(String pathname, String content) {
		File file = new File(pathname);
		boolean b = true;
		try {
			if (!file.exists()) {
				System.out.println(file.getParent());
				createDirectory(file.getParent());
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes("GBK"));
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		if(path!=null){
			File fl = new File(path);
			if (fl.exists())
				fl.delete();
		}
 	}

	/**
	 * 获取文件的md5值
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMD5(String path) {
		if(path==null) return null;
		File file=new File(path);
		if (!file.exists()||!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	/**
	 * 获取文件大小
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSizes(String path)  
	 {
	  if(path==null) return 0;
	  File fl=new File(path);
	  if(!fl.exists()||!fl.isFile()) return 0;
	  long s = 0;
 	  FileInputStream fis = null;
 	  try{
 		 fis = new FileInputStream(fl);
 		  s = fis.available();
 	  }catch(Exception ex){
 		  ex.printStackTrace();
 	  }
  	  return s;
	 }
	 
	/**
	 * 读取文件流
	 * @param path
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] readFileToByte(String path,int start,int length){
		if(path==null) return null;
		File fl=new File(path);
		 if(!fl.exists()||!fl.isFile()) return null;
		try{
			//if((start+length)>fl.length())
			//	length=(int)(fl.length()-start);
			InputStream is = new FileInputStream(fl);
			byte[] bytes = new byte[length+1];
 			is.read(bytes, start, length);
	 		is.close();
	 		return bytes;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取文件流
	 * @param path
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] readFileToByte(String path){
		if(path==null) return null;
		File fl=new File(path);
		 if(!fl.exists()||!fl.isFile()) return null;
		try{
  			InputStream is = new FileInputStream(fl);
			byte[] bytes = new byte[(int)fl.length()];
  			is.read(bytes);
	 		is.close();
	 		return bytes;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
    /**
     * 将一个inputStream里面的数据写到SD卡中
     * @param path
     * @param fileName
     * @param inputStream
     * @return
*/
    public static  File writeToSDfromInput(String path,String fileName,InputStream inputStream){
        //createSDDir(path);
 		File dirFile = new File(path);
		if (!dirFile.exists()) {
  			FileUtil.createDirectory(path);
		}
        File file=new File(path+fileName);
        OutputStream outStream=null;
        try {
            outStream=new FileOutputStream(file);
            byte[] buffer=new byte[4*1024];
            while(inputStream.read(buffer)!=-1){
                outStream.write(buffer);
            }
            outStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    /** 
     * 递归删除文件和文件夹 
     *  
     * @param file 
     *            要删除的根目录 
     */  
    public static void DeleteFile(File file) {  
        if (file.exists() == false) {  
            return;  
        } else {  
            if (file.isFile()) {  
                file.delete();  
                return;  
            }  
            if (file.isDirectory()) {  
                File[] childFile = file.listFiles();  
                if (childFile == null || childFile.length == 0) {  
                    file.delete();  
                    return;  
                }  
                for (File f : childFile) {  
                    DeleteFile(f);  
                }  
                file.delete();  
            }  
        }  
    } 
    
    public static boolean hasFile(String path){
    	if(path==null) return false;
    	File fl=new File(path);
    	return fl.exists();
    }
}

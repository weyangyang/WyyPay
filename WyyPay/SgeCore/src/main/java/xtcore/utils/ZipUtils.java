/**
* Copyright (c)北京慕华信息科技有限公司
*
* ZipUtils
*
* xtcore.utils.ZipUtils.java
* @author: yusheng.li
* @since:  2015年1月6日
* @version: 1.0.0
*
* @changeLogs:
*     1.0.0: First created this class.
*
******************************************************************************/
package xtcore.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Zip压缩解压缩
 */
public final class ZipUtils {
	 private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte
	private ZipUtils(){
	}
	
	/**
	 * 取得压缩包中的 文件列表(文件夹,文件自选)
	 * @param zipFileString		压缩包名字
	 * @param bContainFolder	是否包括 文件夹
	 * @param bContainFile		是否包括 文件
	 * @return
	 * @throws Exception
	 */
	public static java.util.List<java.io.File> getFileList(String zipFileString, boolean bContainFolder, boolean bContainFile)throws Exception {
		java.util.List<java.io.File> fileList = new java.util.ArrayList<java.io.File>();
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(new java.io.FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";
		
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
		
			if (zipEntry.isDirectory()) {
		
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(szName);
				if (bContainFolder) {
					fileList.add(folder);
				}
		
			} else {
				java.io.File file = new java.io.File(szName);
				if (bContainFile) {
					fileList.add(file);
				}
			}
		}//end of while
		
		inZip.close();
		
		return fileList;
	}

	/**
	 * 返回压缩包中的文件InputStream
	 * 
	 * @param zipFilePath		压缩文件的名字
	 * @param fileString	解压文件的名字
	 * @return InputStream
	 * @throws Exception
	 */
	public static java.io.InputStream upZip(String zipFilePath, String fileString)throws Exception {
		java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFilePath);
		java.util.zip.ZipEntry zipEntry = zipFile.getEntry(fileString);
		
		return zipFile.getInputStream(zipEntry);

	}
	
	/**
	 * 解压一个压缩文档 到指定位置
	 * @param zipFileString	压缩包的名字
	 * @param outPathString	指定的路径
	 * @throws Exception
	 */
	public static void unZipFolder(InputStream input, String outPathString)throws Exception {
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(input);
		java.util.zip.ZipEntry zipEntry = null;
		String szName = "";
		
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
		
			if (zipEntry.isDirectory()) {
		
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(outPathString + java.io.File.separator + szName);
				folder.mkdirs();
		
			} else {
		
				java.io.File file = new java.io.File(outPathString + java.io.File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				java.io.FileOutputStream out = new java.io.FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}//end of while
		
		inZip.close();
	}
	
	/**
	 * 解压一个压缩文档 到指定位置
	 * @param zipFileString	压缩包的名字
	 * @param outPathString	指定的路径
	 * @throws Exception
	 */
	public static void unZipFolder(String zipFileString, String outPathString)throws Exception {
		unZipFolder(new java.io.FileInputStream(zipFileString),outPathString);
	}//end of func
	

	/**
	 * 压缩文件,文件夹
	 * 
	 * @param srcFilePath	要压缩的文件/文件夹名字
	 * @param zipFilePath	指定压缩的目的和名字
	 * @throws Exception
	 */
	public static boolean zipFolder(String srcFilePath, String zipFilePath)throws Exception {
		//创建Zip包
		java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));
		
		//打开要输出的文件
		java.io.File file = new java.io.File(srcFilePath);

		//压缩
		zipFiles(file.getParent()+java.io.File.separator, file.getName(), outZip);
		
		//完成,关闭
		outZip.finish();
		outZip.close();
		return true;
	
	}//end of func
	
	/**
	 * 压缩文件
	 * @param folderPath
	 * @param filePath
	 * @param zipOut
	 * @throws Exception
	 */
	private static void zipFiles(String folderPath, String filePath, java.util.zip.ZipOutputStream zipOut)throws Exception{
		if(zipOut == null){
			return;
		}
		
		java.io.File file = new java.io.File(folderPath+filePath);
		
		//判断是不是文件
		if (file.isFile()) {

			java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(filePath);
			java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
			zipOut.putNextEntry(zipEntry);
			
			int len;
			byte[] buffer = new byte[4096];
			
			while((len=inputStream.read(buffer)) != -1)
			{
				zipOut.write(buffer, 0, len);
			}
			
			zipOut.closeEntry();
		}
		else {
			
			//文件夹的方式,获取文件夹下的子文件
			String fileList[] = file.list();
			
			//如果没有子文件, 则添加进去即可
			if (fileList.length <= 0) {
				java.util.zip.ZipEntry zipEntry =  new java.util.zip.ZipEntry(filePath+java.io.File.separator);
				zipOut.putNextEntry(zipEntry);
				zipOut.closeEntry();				
			}
			
			//如果有子文件, 遍历子文件
			for (int i = 0; i < fileList.length; i++) {
				zipFiles(folderPath, filePath+java.io.File.separator+fileList[i], zipOut);
			}//end of for
	
		}//end of if
		
	}//end of func
	
	public void finalize() throws Throwable {
		
	}
	/**
	 * 压缩文件
	 * @param file 要压缩的文件
	 * @param zipFile 压缩后的zip文件
	 * @throws IOException
	 */
	public static boolean zipFile(String srcfile,File zipFile){
		try {
			File file = new File(srcfile);
			  ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
			            zipFile), BUFF_SIZE));
			   
			        zipFile(file, zipout, "");
			    
			    zipout.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}catch (Exception e){
			return false;
		}
		return true;
	}
	/**
     * 压缩文件
     *
     * @param resFile 需要压缩的文件（夹）
     * @param zipout 压缩的目的文件
     * @param rootpath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException 当压缩过程出错时抛出
     */
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
            throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                    BUFF_SIZE);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }
    /**
	 * 图片压缩，返回Bitmap.
	 * @param filePath
	 * @param scale
	 * @return
	 */
	public static Bitmap compressBitmap(String filePath, int scale) {
		if (scale <= 0) {
			scale = 2;
		}
		final BitmapFactory.Options option= new BitmapFactory.Options();
		option.inJustDecodeBounds = true; //decode不返回Bitmap
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, option);
		option.inJustDecodeBounds = false;
		int width = option.outWidth;
		int height = option.outHeight;
		int small = Math.min(width, height);
		if (small / scale > 600) { //保证分辨率不小于600 * 600
			option.inSampleSize = scale;
		}else {
			if (width > height) {
				option.inSampleSize = height / 600;
			}else {
				option.inSampleSize = width / 600;
			}
		}
		if (scale <= 0) {
			option.inSampleSize = 1;
		}
		bitmap = BitmapFactory.decodeFile(filePath, option);
		return bitmap;
	}
	/**
	 * 压缩文件并保存.
	 * @param filePath
	 * @param scale
	 */
	public static void compressBitmapFile(String filePath, int scale) {
		Bitmap bitmap = compressBitmap(filePath, scale);
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
				out.flush();
				out.close();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String base64File(String filePath) {
		File file = new File(filePath);
		try {
			FileInputStream  inputStream = new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
			inputStream.read(buffer);
			inputStream.close();
			return Base64.encodeToString(buffer, Base64.DEFAULT);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
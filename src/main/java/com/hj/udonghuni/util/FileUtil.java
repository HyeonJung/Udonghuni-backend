package com.hj.udonghuni.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	@Value("${imagePath}")
	static String imagePath;
	
	/**
	 * 파일 저장
	 */
	public static String fileSave(MultipartFile file, String path) {
		
		String storedFileName = "";
		
		try {
	
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			
			String originalFile = file.getOriginalFilename();
			
			logger.debug("### originalFile = {}, name = {}",originalFile,file.getName());
			String extension = ".png";
			if (!originalFile.equals("blob")) {
				extension = originalFile.substring(originalFile.lastIndexOf("."));
			}
			
			String storedFile = "";
			do {
			   long random = new Random().nextInt(1_000_000_000);
			   File temp = new File(imagePath + path,Base62.encode(random) + extension);
			   if (!temp.exists()) {
				   storedFile = Base62.encode(random) + extension;
			   }
			        	
			} while (storedFile == null || storedFile.equals(""));
			//String storedFile = "o-" + uuid + extension;
			
			File saveFile = new File(path, storedFile);
			file.transferTo(saveFile);
			
			storedFileName = storedFile;
		} catch (Exception e) {
			logger.error("filesave Error = ",e);
		}
		
		return storedFileName;
	}
	
	public static String fileSave(InputStream in, String path) {
		
		UUID uuid = UUID.randomUUID();
		
		String storedFileName = "";
		
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
						
			String extension = ".png";
			
			String storedFile = "o-" + uuid + extension;
			
			File saveFile = new File(path, storedFile);
			BufferedImage image = ImageIO.read(in);
			storedFileName = storedFile;
			
			ImageIO.write(image, extension, saveFile);
			
		} catch (Exception e) {
			logger.error("### error",e);
		} 
		
		return storedFileName;
	}
	
	/**
	 * 파일 다운로드
	 */
	public static void fileDownload(HttpServletResponse response, File file, String fileName) {
		OutputStream out = null;
		InputStream in = null;
		
		logger.debug("### filename = {}",file.getName());
		
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes("euc-kr"), "8859_1"));
			//response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
			
			out = response.getOutputStream();
			in = new FileInputStream(file);
			
			IOUtils.copy(in, out);
			out.flush();
		} catch (UnsupportedEncodingException e) {
			logger.error("### file download encoding error!", e);
		} catch (IOException e) {
			logger.error("### file download io error!", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * 이미지 View (Using the HttpMessageConverter)
	 * @param response
	 * @param file
	 */
//	public static ImageReturnModel ImageView(File file) {
//		InputStream in = null;
//		ImageReturnModel ret = new ImageReturnModel();
//		Tika tika = new Tika();
//		
//		try {
//			in = new FileInputStream(file);
//			
//			ret.setMediaType(getMediaType(tika.detect(file).toLowerCase()));
//			ret.setBytes(IOUtils.toByteArray(in));
//			
//		} catch (UnsupportedEncodingException e) {
//			logger.error("### file download encoding error!", e);
//		} catch (IOException e) {
//			logger.error("### file download io error!", e);
//		} finally {
//			IOUtils.closeQuietly(in);	
//		}
//		return ret;
//	}
		
	/**
	 * 이미지 MediaType값
	 */
	public static MediaType getMediaType(String mimeType) {
		
		if (mimeType.equals("image/jpeg")) {
			return MediaType.IMAGE_JPEG;
		} else if (mimeType.equals("image/gif")) {
			return MediaType.IMAGE_GIF;
		} else if (mimeType.equals("image/png")) {
			return MediaType.IMAGE_PNG;
		} else {
			return null;
		}
	} 
	
}

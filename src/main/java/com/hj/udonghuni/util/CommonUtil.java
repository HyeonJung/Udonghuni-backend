package com.hj.udonghuni.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class CommonUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * 파라미터 확인
	 * @param filePath
	 * @param fileSize
	 * @param originalFile
	 * @param storedFile
	 * @param userNo
	 * @return
	 */
	public static boolean checkParam(String filePath, Long fileSize, String originalFile, String storedFile, Integer userNo) {
		boolean ret = true;
		
		if (filePath == null || filePath.length() < 1)
			ret = false;
		if (fileSize == null || fileSize < 1)
			ret = false;
		if (originalFile == null || originalFile.length() < 1)
			ret = false;
		if (storedFile == null || storedFile.length() < 1)
			ret = false;
		if (userNo == null || userNo < 0)
			ret = false;
		
		return ret;
	}

	/**
	 * 확장자 확인
	 * @param originalFile
	 * @return
	 */
	public static boolean checkExtension(String originalFile, String fileExtensions) {
		
		if (fileExtensions.toLowerCase().contains(getExtension(originalFile).toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 확장자 추출
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		
		int length = fileName.length();
		return fileName.substring(fileName.lastIndexOf("."),length);
		
	}
	
	/**
	 * Image MIME TYPE 확인
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static boolean checkMimeType(InputStream file, String mimeTypes) throws IOException {
		
		Tika tika = new Tika();
		
		String mimeType = tika.detect(file).toLowerCase();
		
		logger.debug("### MimeType = {}", mimeType);
		
		if (mimeTypes.toLowerCase().contains(mimeType)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * MimeType과 확장자 체크
	 * @param file
	 * @param fileExtensions
	 * @param mimeTypes
	 * @return
	 * @throws IOException 
	 */
	public static boolean checkMimeTypeAndExtension(MultipartFile file, String fileExtensions, String mimeTypes) throws IOException {
		
		boolean ret = false;
		
		
		ret = checkMimeType(file.getInputStream(), mimeTypes);
			
		if (ret) {
			ret = checkExtension(file.getOriginalFilename(), fileExtensions);
		} else {
			return ret;
		}

		return ret;
	}
}


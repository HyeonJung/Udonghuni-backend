package com.hj.udonghuni.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hj.udonghuni.persistence.Photo;
import com.hj.udonghuni.service.PhotoService;
import com.hj.udonghuni.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags="udonghuni")
public class WebController {
	
	@Value("${imagePath}")
	String imagePath;
	
	@Autowired
	PhotoService photoService;
	
	/**
	 * 이미지 파일 업로드
	 * @param file
	 * @return
	 */
	@ApiOperation(value="이미지 파일 업로드")
	@RequestMapping(value = "/photos" , method = RequestMethod.POST)
	public ResponseEntity<?> upload (@ApiParam(value="이미지 파일(jpg,gif,png형식)",required=true) @RequestPart List<MultipartFile> files,
			HttpServletRequest request)	{
		
		String mimeTypes = "image/jpeg,image/png,image/gif";
		
		try {
			
			for (MultipartFile file : files) {
				if (!CommonUtil.checkMimeType(file.getInputStream(), mimeTypes)) {
					return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
				}
			}
			
			List<Photo> photos = photoService.fileSave(files);
			
			return new ResponseEntity<>(photos,HttpStatus.CREATED);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 모든 이미지
	 */
	@ApiOperation(value="모든 이미지")
	@RequestMapping(value = "/photos" , method = RequestMethod.GET)
	public ResponseEntity<?> getAllPhoto() {
		
		return new ResponseEntity<>(photoService.getAllPhoto(),HttpStatus.OK);
	}
}

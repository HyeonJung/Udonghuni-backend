package com.hj.udonghuni.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hj.udonghuni.persistence.Photo;
import com.hj.udonghuni.persistence.PhotoRepository;
import com.hj.udonghuni.util.CommonUtil;
import com.hj.udonghuni.util.FileUtil;

@Service
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	PhotoRepository photoRepository;
	
	@Value("${imagePath}")
	String imagePath;

	@Override
	@Transactional
	public List<Photo> fileSave(List<MultipartFile> files) {
		List<Photo> photoList = new ArrayList<Photo>();
		
		try {
			for (MultipartFile file : files) {
				Photo photo = fileSave(file);
				photoList.add(photo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return photoList;
	}
	
	@Transactional
	public Photo fileSave(MultipartFile file) throws IOException {
		
		BufferedImage uploadImage = ImageIO.read(file.getInputStream());
		
		String path = "image/";
		
		String originalFileName = file.getOriginalFilename();
		String storedFile = FileUtil.fileSave(file, imagePath + path);
		String extension = CommonUtil.getExtension(storedFile);
		
		Photo photo = new Photo();
		photo.setFileSize(file.getSize());
		photo.setFilePath(path + storedFile);
		photo.setStoredFile(storedFile);
		photo.setExtension(extension);
		photo.setOriginalFile(originalFileName);
		photo.setWidth(uploadImage.getWidth());
		photo.setHeight(uploadImage.getHeight());
		
		photo = savePhoto(photo);
		return photo;
	}
	
	@Transactional
	@Override
	public Photo savePhoto(Photo photo) {
		photo = photoRepository.save(photo);
		return photo;
	}
	
	@Override
	public List<Photo> getAllPhoto() {
		List<Photo> photoList = photoRepository.findAll();
		return photoList;
	}
}

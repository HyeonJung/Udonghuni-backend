package com.hj.udonghuni.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hj.udonghuni.persistence.Photo;

public interface PhotoService {

	
	List<Photo> fileSave(List<MultipartFile> files);
	
	Photo savePhoto(Photo photo);
	
	List<Photo> getAllPhoto();
}

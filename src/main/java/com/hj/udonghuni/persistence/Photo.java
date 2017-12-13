package com.hj.udonghuni.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.hj.udonghuni.common.BaseModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Photo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column
	String filePath;
	
	@Column
	String originalFile;
	
	@Column
	String storedFile;
	
	@Column
	Long fileSize;
	
	@Column
	String extension;
	
	@Column
	Integer width;
	
	@Column
	Integer height;
	
	
}

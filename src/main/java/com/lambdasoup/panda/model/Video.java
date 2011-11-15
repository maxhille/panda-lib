package com.lambdasoup.panda.model;

import java.util.Date;

public class Video {
	
	public enum Status {
		processing, success, fail
	}
	
	public String id;
	public String originalFilename;
	public String sourceUrl;
	public Status status;
	public String extname;
	public String videoCodec;
	public String audioCodec;
	public Integer thumbnailPosition;
	public Integer height;
	public Integer width;
	public Integer fps;
	public Integer duration;
	public Integer fileSize;
	public Date createdAt;
	public Date updatedAt;
}

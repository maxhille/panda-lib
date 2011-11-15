package com.lambdasoup.panda.model;

import java.util.Date;

public class Encoding {
	public enum Status {
		processing, success, fail
	}
	
	public String id;
	public String videoId;
	public String profileId;
	public Status status;
	public String extname;
	public Integer encodingProgress;
	public Integer height;
	public Integer width;
	public Integer fileSize;
	public Date startedEncodingAt;
	public Integer encoding_time;
	public Date createdAt;
	public Date updatedAt;
}

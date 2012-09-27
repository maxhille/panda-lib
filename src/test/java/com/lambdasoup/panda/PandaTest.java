/**
 *  Copyright 2011 Maximilian Hille <mh@lambdasoup.com>
 * 
 *  This file is part of panda-lib.
 *  
 *  panda-lib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  panda-lib is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with panda-lib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.lambdasoup.panda;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.lambdasoup.panda.model.Encoding;
import com.lambdasoup.panda.model.Profile;
import com.lambdasoup.panda.model.Video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PandaTest {
	
	Panda panda;
	Video video;
	Log log;

	@BeforeClass
	public void setup() {
	
    log = LogFactory.getLog(PandaTest.class);
		String accessKey = System.getProperty("panda.accessKey");
		String cloudId   = System.getProperty("panda.cloudId");
		String secretKey = System.getProperty("panda.secretKey");
		String apiHost   = System.getProperty("panda.apiHost");

		panda = new Panda(accessKey, cloudId, secretKey, apiHost);
		
	}

	@Test
	public void getProfilesTest() {
		Collection<Profile> profiles = this.panda.getProfiles();
		
		Assert.assertNotNull(profiles);
	}

	@Test
	public void postProfileTest() {
		Profile profile = new Profile();
		
		profile.presetName = "h264";
		
		Profile profileResponse = this.panda.postProfile(profile);
		
		Assert.assertNotNull(profileResponse);
	}

	@Test(dependsOnMethods={"postRemoteVideoTest"})
	public void getEncodingsTest() {
		Collection<Encoding> encodings = this.panda.getEncodings(this.video.id);
		
		assert encodings != null;
	}
	
	@Test(dependsOnMethods={"postRemoteVideoTest"})
	public void getEncodingTest() {
		Encoding encoding = this.panda.getEncoding(this.video.id);
		
		assert encoding != null;
	}
	
	@Test(dependsOnMethods={"postRemoteVideoTest"})
	public void getVideoTest() {
		Video video = this.panda.getVideo(this.video.id);
		
		assert video != null;
	}

	@Test(dependsOnMethods={"postRemoteVideoTest"})
	public void getVideosTest() {
		Collection<Video> videos = this.panda.getVideos(Video.Status.success);
		
		assert videos != null;
	}
	
	@Test(dependsOnMethods={"getVideosTest","getVideoTest","getEncodingsTest"})
	public void deleteVideoTest() {
		this.panda.deleteVideo(this.video.id);
	}

	@Test
	public void postRemoteVideoTest() throws InterruptedException {
		this.video = this.panda.postRemoteVideo("http://panda-test-harness-videos.s3.amazonaws.com/panda.mp4");
		
		assert this.video != null;
	}

  @Test
  public void postVideoTest(){
    File videoFile = new File(System.getProperty("test.videoPath"));
    this.video = this.panda.postVideo(videoFile);
    assert this.video.id != null;
    this.video = this.panda.postVideo(videoFile, "my hats");
    assert this.video.id != null;
  }

	@Test
	public void accessDetailsTest() {
		Map<String,String> details = this.panda.accessDetails(PandaHttp.Method.POST, "/videos.json");
		
		assert details != null;
	}

	@AfterClass
	public void cleanUp() throws InterruptedException {
		// delete all videos in panda cloud
/*		Collection<Video> videos = this.panda.getVideos();
		for (Video video : videos)
			this.panda.deleteVideo(video.id);
		assert this.panda.getVideos().size() == 0;

		// delete all encodings panda cloud
		Collection<Encoding> encodings = this.panda.getEncodings();
		for (Encoding encoding : encodings)
			this.panda.deleteEncoding(encoding.id);
		assert this.panda.getEncodings().size() == 0; */
	}

}

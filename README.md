# panda-lib

This is a small lib providing easy access to the API of the pandastream cloud video transcoding service (http://www.pandastream.com).

If you need more of the API reflected or improvement/bugfixes feel free
to contact me per <mh@lambdasoup.com>.

## Usage

Instantiate the Panda client with
```java
Panda panda = new Panda(accessKey, cloudId, secretKey, apiHost);
```
Upload a video file with
```java
Video video = panda.postRemoteVideo("http://panda-test-harness-videos.s3.amazonaws.com/panda.mp4");
```
The Video object now holds the id the pandastream.com service generated for it.
 
To upload a local video file
```java                       
File videoFile = new File("path/to/video.flv");
Video video = panda.postVideo(videoFile);
//post video with payload (some contextual data)
Video video = panda.postVideo(videoFile, "some related data");
```

To get the encodings that are produced after your upload use
```java
Collection<Encoding> encodings = panda.getEncodings(video.id);
```
For further usage see test class.

## Build

**WARNING** tests will *delete* all videos from the specified cloud.
```
mvn install test -Dtest.videoPath=my_local_test_video -Dpanda.accessKey=my_access_key -Dpanda.cloudId=my_cloud_id -Dpanda.secretKey=my_secret_key -Dpanda.apiHost=my_api_host
```

## Dependencies

* Apache Commons Http Libs
* Gson Json Library


## Changelog

### 0.1.2 -> 0.2.0

* implemented begin upload session

### 0.1.1 -> 0.1.2

Contributor http://github.com/mrloop

* added upload of local video file

### 0.1.0 -> 0.1.1

* added link to pandastream.com
* added usage example to README


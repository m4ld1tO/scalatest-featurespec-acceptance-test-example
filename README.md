# scalatest-featurespec

# specs2 acceptance test example

### Resources
ScalaTest FeatureSpec  
http://www.scalatest.org/getting_started_with_feature_spec

Scala HTTP client  
https://github.com/playframework/play-ws

## Prerequisites ##
Install Scala and sbt

## Test case ##
Acceptance test of for fetching a single video from Klaras REST API:   
 
<b>Given</b> there is a video added to ElasticSearch with id 'dn.screen9.1uwHxJLDuuBKBHGHQcissw'  
<b>When</b> fetching a video with id 'dn.screen9.1uwHxJLDuuBKBHGHQcissw'  
<b>Then</b> response should contain the right json body  

```json
{
  "id" : "dn.screen9.1uwHxJLDuuBKBHGHQcissw",
  "fileName" : "20171107-arbogany-1053_NormalHires.mp4",
  "title" : "Grafikfilm: Arbogamorden  (uppdaterad för hovrätten)",
  "description" : "Huvudpersonerna och händelserna som ledde fram till rättegången och dom mot den 42:åriga kvinnan och hennes pojkvän. Nu prövas målet i Svea Hovrätt",
  "streamUrl" : "https://video-cdn.dn.se/M/V/1/u/1uwHxJLDuuBKBHGHQcissw_360p_h264h.mp4?v=1&token=0ed558211ccafe3db4784",
  "thumbnails" : {
    "small" : "https://csp.screen9.com/img/1/u/w/H/thumb_1uwHxJLDuuBKBHGHQcissw/8.jpg",
    "large" : "https://csp.screen9.com/img/1/u/w/H/image_1uwHxJLDuuBKBHGHQcissw/8.jpg"
  },
  "duration" : "PT1M36.28S",
  "publishedAt" : null,
  "createdAt" : "2017-11-07T14:36:46.000+01:00",
  "createdBy" : null,
  "transcodeStatus" : "done",
  "sentToTranscodeAt" : "2017-11-07T14:38:12.000+01:00",
  "status" : "published"
}
```

## Setup test fixture ##
In nav project comment out existing Groovy tests mvn execution in script `.run.sh`, and then it to start containers with ElasticSearch, Redis and nav-klara-dx up and running:
```sh
./integration-test/nav-klara-it/run.sh
```

## Run test with sbt ##
```sh
sbt> testOnly example.NavaKlaraVideo 
```

## Run test with IntelliJ ##
Right click on test an choose run

### Pros
+ Can be run by IntelliJ
+ \<Click to se difference> in IntelliJ works
+ Seems pretty fast
 
### Cons
- For some types of test conditions are specified twice, both in text and in method, i.e you can have the right condition in the text but the wrong ine int the method doing the actual check.  
  This might be fixed using StandardDelimitedStepParsers 
- Error messages could be more informative
- Not very intuitive (at least not for a Scala newbie). A lot of magic.
- How pass result from Given to THen? Variables in Given is not in scope in Then

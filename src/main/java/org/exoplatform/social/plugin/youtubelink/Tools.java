package org.exoplatform.social.plugin.youtubelink;

import java.io.IOException;
import java.util.List;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;

public class Tools {
	  public static class VideoFeed {
		    @Key List<Video> items;
		  }

		  public static class Video {
		    @Key String title;
		    @Key String description;
		    @Key Player player;
		  }
		  
		  public static class Player {
		    @Key("default") String defaultUrl;
		  }

		  public static class YouTubeUrl extends GenericUrl {
		    @Key final String alt = "jsonc";
		    @Key String author;
		    @Key("max-results") Integer maxResults;
		    
		    YouTubeUrl(String url) { super(url); }
		  }

	public static void youTubeLink(){
	    // set up the JSON-C parser
	    JsonCParser parser = new JsonCParser();
	    parser.jsonFactory = new JacksonFactory();
	    // set up the Google headers
	    GoogleHeaders headers = new GoogleHeaders();
	    headers.setApplicationName("ExoPlatform-YouTubePraser/1.0");
	    headers.gdataVersion = "2";
	    // set up the HTTP transport
	    HttpTransport transport = new NetHttpTransport();
	    transport.defaultHeaders = headers;
	    transport.addParser(parser);
	    // build the YouTube URL
	    YouTubeUrl url = new YouTubeUrl("https://gdata.youtube.com/feeds/api/videos");
	    url.author = "searchstories";
	    url.maxResults = 2;
	    // build the HTTP GET request
	    HttpRequest request = transport.buildGetRequest();
	    request.url = url;
	    // execute the request and the parse video feed
	    VideoFeed feed = null;
		try {
			feed = request.execute().parseAs(VideoFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    for (Video video : feed.items) {
	      System.out.println();
	      System.out.println("Video title: " + video.title);
	      System.out.println("Description: " + video.description);
	      System.out.println("Play URL: " + video.player.defaultUrl);
	    }		
	}
}

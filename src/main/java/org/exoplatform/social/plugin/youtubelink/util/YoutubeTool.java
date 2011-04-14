package org.exoplatform.social.plugin.youtubelink.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.caja.util.Json;

public class YoutubeTool {
	private static final String YOUTUBE_URL_OEMBED_PREFIX = "http://www.youtube.com/oembed?url=";
	private static final String YOUTUBE_URL_OEMBED_POSTFIX = "&format=json";
	public static final String OEMBED_URL = "url";
	public static final String OEMBED_TITLE = "title";
	public static final String OEMBED_HTML = "html";
	public static final String OEMBED_THUMBURL = "thumbnail_url";

	
	private URL linkURL = null;
	private JSONObject oEmbedData = null;


	
	
	public static JSONObject jsonRequest(URL linkURL) throws IOException, JSONException {
		StringBuffer stringBuffer = null;
		JSONObject jsonObject = null;

			BufferedReader buffReader = new BufferedReader(new InputStreamReader(linkURL.openStream()));
			stringBuffer = new StringBuffer();
			int nextChar;
			while ((nextChar = buffReader.read()) != -1) {
				stringBuffer.append((char)nextChar);
			}
			buffReader.close();
			
			String jsonRequesResult = stringBuffer.toString();
			jsonObject = new JSONObject(jsonRequesResult);
		return jsonObject;
	}
	
	public YoutubeTool(URL linkURL) throws MalformedURLException{
		if(linkURL != null){
			this.linkURL = linkURL;
		} else {
			throw new MalformedURLException();
		}
		
	}

	public YoutubeTool(String linkString) throws MalformedURLException{
		this.linkURL = new URL(linkString);
	}
	
	public JSONObject getoembedData() throws Exception {
		if(oEmbedData == null){
			try {
				String youtubeoembedURL = YOUTUBE_URL_OEMBED_PREFIX + this.linkURL.toString() + YOUTUBE_URL_OEMBED_POSTFIX;
				oEmbedData = jsonRequest(new URL(youtubeoembedURL));
				oEmbedData.put("url", this.linkURL.toString());
			} catch (IOException e) {
				throw new Exception("Not vaild youtube link");
			} catch (JSONException e) {
				throw new Exception("Not vaild youtube link");
			}
		}
		return oEmbedData;
	}

	public URL getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(URL linkURL) {
		this.linkURL = linkURL;
	}
}

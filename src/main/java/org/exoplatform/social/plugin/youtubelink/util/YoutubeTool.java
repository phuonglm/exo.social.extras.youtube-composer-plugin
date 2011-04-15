package org.exoplatform.social.plugin.youtubelink.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeTool {
	private static final String YOUTUBE_URL_OEMBED = "http://www.youtube.com/oembed?url=%s&format=json";
    private static final String YOUTUBE_URL_QUERY = "http://gdata.youtube.com/feeds/api/videos?q=%s?alt=json";


	public static final String OEMBED_URL = "url";
	public static final String OEMBED_TITLE = "title";
	public static final String OEMBED_HTML = "html";
	public static final String OEMBED_THUMBURL = "thumbnail_url";
	
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

	
	public static JSONObject getoembedData(String url) throws Exception {
        JSONObject jsonOEmbed = new JSONObject();

        try {
            String youtubeoembedURL = String.format(YOUTUBE_URL_OEMBED, url );
            jsonOEmbed = jsonRequest(new URL(youtubeoembedURL));
            jsonOEmbed.put("url", url);
        } catch (IOException e) {
            throw new Exception("Not vaild youtube link");
        } catch (JSONException e) {
            throw new Exception("Not vaild youtube link");
        }

		return jsonOEmbed;
	}

    public static JSONObject getQueryData(String queryString) throws Exception {
        JSONObject jsonQueryResult = new JSONObject();

        try {
            String youtubeQueryURL = String.format(YOUTUBE_URL_QUERY, queryString);
            jsonQueryResult = jsonRequest(new URL(youtubeQueryURL));
            jsonQueryResult.put("url", queryString);
        } catch (IOException e) {
            throw new Exception("Not vaild gdata link");
        } catch (JSONException e) {
            throw new Exception("Not vaild gdata link");
        }

		return jsonQueryResult;
	}
}

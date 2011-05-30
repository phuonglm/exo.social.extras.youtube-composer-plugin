package org.exoplatform.social.plugin.youtube.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class VideoEmbedTool {
	private static final String YOUTUBE_URL_OEMBED = "http://www.youtube.com/oembed?url=%s&format=json";
  private static final String YOUTUBE_URL_QUERY = "http://gdata.youtube.com/feeds/api/videos?q=%s&alt=json";

  private static final String VIMEO_URL_OEMBED = "http://vimeo.com/api/oembed.json?url=%s";


	public static final String OEMBED_URL = "url";
	public static final String OEMBED_TITLE = "title";
	public static final String OEMBED_HTML = "html";

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
            throw new Exception("Not vaild video link");
        } catch (JSONException e) {
            throw new Exception("Not vaild video link");
        }

		return jsonOEmbed;
	}

    public static JSONArray getQueryData(String queryString) throws Exception {
        JSONObject jsonQuery = new JSONObject();
        JSONArray jsonResults = new JSONArray();
        queryString = URIUtil.encode(queryString, null);
        try {
            String youtubeQueryURL = String.format(YOUTUBE_URL_QUERY, queryString);
            jsonQuery = jsonRequest(new URL(youtubeQueryURL));
            JSONObject resultQueryItem = null;
            JSONObject resultItem = null;
            JSONObject resultObject = jsonQuery.getJSONObject("feed");
            if (resultObject.getJSONObject("openSearch$totalResults").getInt("$t") > 0){
	            JSONArray resultArray = resultObject.getJSONArray("entry");
	            
	            for (int i = 0; i < resultArray.length(); i++ ){
	            	resultQueryItem = resultArray.getJSONObject(i);
	            	String title = resultQueryItem.getJSONObject("title").getString("$t");
	            	String thumbURL = resultQueryItem.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(1).getString("url");
	            	String media = resultQueryItem.getJSONArray("link").getJSONObject(0).getString("href");
	            	
	            	resultItem = new JSONObject();
	            	resultItem.put("title", title);
	            	resultItem.put("thumbURL", thumbURL);
	            	resultItem.put("media", media);
	            	jsonResults.put(resultItem);
	            }
            }
            
        } catch (IOException e) {
            throw new Exception("Not vaild gdata link");
        } catch (JSONException e) {
            throw new Exception("Not vaild gdata link");
        }

		return jsonResults;
	}
}

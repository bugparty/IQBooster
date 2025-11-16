package com.ifancc.Utils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * http通信中需要使用到的工具类
 * @author liyang
 *
 */
public class ApacheUtils {
	/**
	 * 将存储用户信息的map转换为一个Json
	 * @param infosMap
	 * @return
	 */
	public static JSONObject convertMapToJson(Map<String,String> infosMap){
		JSONObject obj = new JSONObject();
		Set<String> keys = infosMap.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = infosMap.get(key);
			try {
				obj.put(key,value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	/**
	 * 此函数将map容器中的内容转存到list<NameValuePair>中
	 * @param oauthMap
	 * @return
	 */
	public static List<NameValuePair> convertMapToNameValuePairs(Map<String, String> oauthMap) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Set<String> keys = oauthMap.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = oauthMap.get(key);
			NameValuePair pair = new BasicNameValuePair(key, value);
			pairs.add(pair);
		}
		return pairs;
	}
	/**
	 * 将httpClient请求的结果保存到String中
	 * @param response
	 * @return
	 */
	public static String getResponseText(HttpResponse response) {
		HttpEntity responseEntity = response.getEntity();
		InputStream input = null;
		String result = null;
		try {
			input = responseEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
			result = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}

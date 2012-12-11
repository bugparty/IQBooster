package com.ifancc.Utils;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ifancc.models.ReturnMessage;
import com.ifancc.models.User;

/**
 * json解析工具类
 * 引用google。gson第三方类库
 * @author liyang
 *
 */
public class JsonUtil {
	public String ParseJson(String jsonData){
		String returnMessage = null;
		try{
			JsonReader reader = new JsonReader(new StringReader(jsonData));
			reader.beginArray();
			while(reader.hasNext()){//判断是否还有对象
				reader.beginObject();
				while(reader.hasNext()){//判断是否还有键值对
					String tagName = reader.nextName();
				}
				reader.endObject();
			}
			reader.endArray();
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnMessage;
	}
	
	
	public void parseUserFromJson(String jsonData){
		Gson gson = new Gson();
		User user = gson.fromJson(jsonData, User.class);
	}
	
	public ReturnMessage parseReturnMsgFromJson(String jsonData){
		Gson gson = new Gson();
		ReturnMessage returnMsg = gson.fromJson(jsonData, ReturnMessage.class);
		return returnMsg;
	}
	
	public void parseUserFromJson1(String jsonData){
		Type listType = new TypeToken<LinkedList<User>>(){}.getType();
		
		Gson gson = new Gson();
		LinkedList<User> users = gson.fromJson(jsonData, listType);
		
		for(Iterator<User> iterator = users.iterator();iterator.hasNext();){
			User user = (User) iterator.next();
		}
	}
}

package com.ifancc.braingame;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.ifancc.Constants.Constant;
import com.ifancc.Constants.UserInfos;
import com.ifancc.Constants.UserScores;
import com.ifancc.Utils.ApacheUtils;
import com.ifancc.Utils.JsonUtil;
import com.ifancc.models.ReturnMessage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 实现登录功能的异步操作
 * @author liyang
 *
 */
public class LoginAsync extends AsyncTask<HashMap<String,String>, Void, String>{

	private String url = "";
	
	private Login login = null;
	private ProgressDialog progressDialog = null;
	private String userName;
	
	public LoginAsync(Login login,ProgressDialog progressDialog) {
		// TODO Auto-generated constructor stub
		this.login = login;
		this.progressDialog = progressDialog;
	}
	@Override
	protected String doInBackground(HashMap<String, String>... map) {
		// TODO Auto-generated method stub
		String result = null;
    	userName = map[0].get("username");
    	try {
    		HttpPost httpPost = new HttpPost(Constant.LOGIN_URL);
        	//调用工具类中的方法，构建一个json
    		JSONObject userInfos =   ApacheUtils.convertMapToJson(map[0]);
    		//设置http头信息
    		httpPost.addHeader("content-type","application/json");
    		//设置entity
			httpPost.setEntity(new StringEntity(userInfos.toString()));
			//设置连接超时
			BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);  
		    HttpConnectionParams.setSoTimeout(httpParams, 5000);  
			
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse response = httpClient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			System.out.println(code+" ");
			if(code == 200){
				result = EntityUtils.toString(response.getEntity());
				JSONObject resultJson = new JSONObject(result);
				System.out.println(resultJson.toString());
			}
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(login, e.getMessage(), Toast.LENGTH_LONG).show();
		}catch (JSONException e) {
			Toast.makeText(login, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(login, e.getMessage(), Toast.LENGTH_LONG).show();
		}catch (Exception e){
			Toast.makeText(login, e.getMessage(), Toast.LENGTH_LONG).show();
		}finally{
			return result;
		}
	}

	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result == null){
			Toast.makeText(login, "登录失败", Toast.LENGTH_LONG).show();
		}else{
			System.out.println(result);
			JsonUtil jsonUtil = new JsonUtil();
			ReturnMessage returnMsg = jsonUtil.parseReturnMsgFromJson(result);
			if( returnMsg.getStatus_code() != 2){
				Toast.makeText(login, returnMsg.getMessage(), Toast.LENGTH_LONG).show();
			}else{
				SharedPreferences preference = login.getSharedPreferences(userName + Constant.PREFERENCESNAME, login.MODE_PRIVATE);
				SharedPreferences preference1 = login.getSharedPreferences(Constant.PREFERENCESNAME, login.MODE_PRIVATE);
				//初始化UserInfos中的信息
				Editor editor = preference1.edit();
				editor.putString("userName",userName);
				editor.commit();
				Date today = new Date();
				
				
				int d = preference.getInt("lastTrainTime",0 );
				UserScores.speedScore = preference.getInt("speedScore", 0);
				UserScores.memoryScore=preference.getInt("memorySocre",0);
				UserScores.attentionScore = preference.getInt("attentionScore",0);
				UserScores.flexibilityScore = preference.getInt("flexibilityScore",0);
				UserInfos.userName = userName;
				if(d != 0){
					if(d == today.getDate()){
						UserInfos.isTrained = true;
					}else{
						UserInfos.isTrained = false;
					}
				}else{
					UserInfos.isTrained =false;
				}
				UserInfos.isLogin = true;
				Random rand = new Random(System.currentTimeMillis());
				UserInfos.trainGames = GamesUtil.trainGames[Math.abs(rand.nextInt())%6];
				UserInfos.currentGame = null;
				
				Intent intent = new Intent(login, MenuPage.class);
				login.startActivity(intent);
				login.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				login.finish();
			}
		}
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = ProgressDialog.show(
				login, null, " 正在登录. . . ");
	}
}

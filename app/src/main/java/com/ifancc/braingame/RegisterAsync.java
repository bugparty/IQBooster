package com.ifancc.braingame;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


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
 * 实现注册功能的异步操作
 * @author liyang
 *
 */
public class RegisterAsync extends AsyncTask<HashMap<String,String>, Void, String> {
	private ProgressDialog progressDialog = null;
	private Register register = null;
	private String userName;
	public RegisterAsync(Register register,ProgressDialog progressDialog) {
		// TODO Auto-generated constructor stub
		this.progressDialog = progressDialog;
		this.register = register;
	}

	@Override
	protected String doInBackground(HashMap<String,String>... map) {
		
		String result = null;
		userName = map[0].get("username");
    	try {
    		HttpPost httpPost = new HttpPost(Constant.REGISTER_URL);
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
			System.out.println(code + "");
			if(code == 200){
				result = EntityUtils.toString(response.getEntity());
				JSONObject resultJson = new JSONObject(result);
				System.out.println(resultJson.toString());
			}
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(register, e.getMessage(), Toast.LENGTH_LONG).show();
		}catch (JSONException e) {
			Toast.makeText(register, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(register, e.getMessage(), Toast.LENGTH_LONG).show();
		}catch (Exception e){
			Toast.makeText(register, e.getMessage(), Toast.LENGTH_LONG).show();
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
			Toast.makeText(register, "注册失败", Toast.LENGTH_LONG).show();
		}else{
			System.out.println(result);
			JsonUtil jsonUtil = new JsonUtil();
			ReturnMessage returnMsg = jsonUtil.parseReturnMsgFromJson(result);
			if( returnMsg.getStatus_code()!= 1){
				Toast.makeText(register, returnMsg.getMessage(), Toast.LENGTH_LONG).show();
			}else{
				SharedPreferences preference = register.getSharedPreferences(Constant.PREFERENCESNAME, register.MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.putString("userName", userName);
				editor.commit();
				
				Intent intent = new Intent(register, Login.class);
				register.startActivity(intent);
				register.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		}
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = ProgressDialog.show(
				register, null, " 正在注册 . . . ");
	}
	
}

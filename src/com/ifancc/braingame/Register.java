package com.ifancc.braingame;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ifancc.Constants.Constant;
import com.ifancc.Utils.FontUtil;
import com.ifancc.braingame.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 注册页面
 * @author liyang
 *
 */
public class Register extends Activity {
	private LinearLayout root;
	
	private EditText registerUserName = null;
	private EditText registerPassword = null;
	private EditText registerMailbox = null;
	
	private Button registerBtn = null;
	private ProgressDialog progressDialog = null; 
	private AlertDialog.Builder builder = null;
	
	private String userName = "";
	private String password = "";
	private String mailbox = "";
	private String sex = "male";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        root = (LinearLayout)findViewById(R.id.root);
        registerUserName = (EditText) findViewById(R.id.registerUserName);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerMailbox = (EditText)findViewById(R.id.registerMailbox);
        
        registerBtn = (Button)findViewById(R.id.registerBtn);
        
        FontUtil.changeFonts(root,Constant.t1);
        
        
        registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userName = registerUserName.getText().toString().trim();
				password = registerPassword.getText().toString().trim();
				mailbox = registerMailbox.getText().toString().trim();
				
				String strPattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				Pattern p = Pattern.compile(strPattern);
				Matcher m = p.matcher(mailbox);
				
				if("".equals(userName)){
					Toast.makeText(getApplicationContext(), R.string.register_error_mention1,1).show();
				}else if("".equals(password)){
					Toast.makeText(getApplicationContext(), R.string.register_error_mention2,1).show();
				}else if("".equals(mailbox)){
					Toast.makeText(getApplicationContext(), R.string.register_error_mention3,1).show();
				}else if(!m.find()){
					Toast.makeText(getApplicationContext(), R.string.register_error_mention5,1).show();
				}else{
					HashMap<String ,String> map = new HashMap<String ,String>();
			    	map.put("username",userName);
			    	map.put("password", password);
			    	map.put("mailbox", mailbox);
			    	map.put("sex",sex);
			    	RegisterAsync register = new RegisterAsync(Register.this,progressDialog);
			    	register.execute(map);
			    	
				}
			}
		});
        
    }
    
    
    private void backToLogin(){
        	Intent intent = new Intent();
    		intent.setClass(Register.this,Login.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register, menu);
        return true;
    }
}

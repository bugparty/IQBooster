package com.ifancc.braingame;

import java.util.HashMap;

import com.ifancc.Constants.Constant;
import com.ifancc.Constants.SoundPlayer;
import com.ifancc.Constants.UserInfos;
import com.ifancc.GameViews.WelcomeView;
import com.ifancc.Utils.FontUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.appmedia.adshelf.AdshelfManager;
import cn.appmedia.adshelf.ShelfView;

/**
 * 登录界面
 * @author liyang
 *
 */
public class Login extends Activity {
	private static final String AID = "cab1048b6726e8fa";
	static{
		AdshelfManager.setAid(AID);
	}
	
	private EditText loginUserName;
	private EditText loginPassword;
	private LinearLayout root;
	
	private Button loginBtn;
	
	private String userName;
	private String password;
	
	private TextView registerLink;
	private TextView tryLink;
	
	private WelcomeView welcomeView;
	private ProgressDialog progressDialog = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        //初始化静态变量
        DisplayMetrics dm=new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        Constant.initConst(this,dm.widthPixels, dm.heightPixels);
        Constant.al.add(this);
        
        initThread.start();
        adThread.start();
        
        if(Constant.loginStatus == 0){
        	 welcomeView = new WelcomeView(Login.this);
             setContentView(welcomeView);
        }
    }
        
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if(Constant.loginStatus == 1){
    		handler.sendEmptyMessage(0);
    	}
    }
    
    private void initLoginPage(){
    	root = (LinearLayout)findViewById(R.id.root);
        loginUserName = (EditText)findViewById(R.id.loginUserName);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerLink = (TextView)findViewById(R.id.registerLink);
        tryLink = (TextView)findViewById(R.id.tryLink);
        
        SharedPreferences preference = getSharedPreferences(Constant.PREFERENCESNAME, MODE_PRIVATE);
        userName = preference.getString("userName", null);
        if(userName != null){
        	loginUserName.setText(userName);
        }
        
        FontUtil.changeFonts(root,Constant.t1);
        
        String text1 = "我没有帐号";
        String text2 = "体验";
        SpannableString spannableString1 = new SpannableString(text1);
        SpannableString spannableString2 = new SpannableString(text2);
        spannableString1.setSpan(new ClickableSpan() {
			
			@Override
			public void onClick(View widget) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this,Register.class);
				startActivity(intent);
			}
		},0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerLink.setText(spannableString1);
        registerLink.setTypeface(Constant.t1);
        registerLink.setMovementMethod(LinkMovementMethod.getInstance());
        
        spannableString2.setSpan(new ClickableSpan() {
			
			@Override
			public void onClick(View widget) {
				// TODO Auto-generated method stub
				UserInfos.initInfos("noLogin", null, null, false, false);
				Intent intent = new Intent(Login.this,MenuPage.class);
				startActivity(intent);
			}
		}, 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tryLink.setText(spannableString2);
        tryLink.setTypeface(Constant.t1);
        tryLink.setMovementMethod(LinkMovementMethod.getInstance());
        
        
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = loginUserName.getText().toString();
				password = loginPassword.getText().toString();
				if ("".equals(userName)) {
					Toast.makeText(Login.this, "用户名不能为空!", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(password)) {
					Toast.makeText(Login.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
				}else{
					HashMap<String ,String> map = new HashMap<String ,String>();
					map.put("username",userName);
			    	map.put("password", password);
			    	LoginAsync login = new LoginAsync(Login.this,progressDialog);
			    	login.execute(map);
				}
			}
		});
    }
    
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount() == 0){
    		AlertDialog.Builder builder = new Builder(Login.this);
    		builder.setMessage("确认要退出吗？？？");
    		builder.setTitle("提示");
    		builder.setPositiveButton("确认", new PositiveDialogListener());
    		builder.setNegativeButton("取消", new NegativeDialogListener());
    		builder.create().show();
    	}
		return false;
    }
    
    
    private class PositiveDialogListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			Login.this.finish();
			System.exit(0);
		}
    }
    
    private class NegativeDialogListener implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
    }
    public Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what == 0){
    			setContentView(R.layout.activity_login);
            	initLoginPage();
           		Constant.loginStatus = 1;
    		}
    	}
    };
    
    private Thread adThread = new Thread(){
    	public void run() {
    		Constant.shelView = new ShelfView(Login.this);
    	}
    };
    
    private Thread initThread = new Thread(){
    	public void run() {
            //初始化声音
            SoundPlayer.init(Login.this);
    	}
    };
}


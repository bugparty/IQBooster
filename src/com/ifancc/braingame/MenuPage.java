package com.ifancc.braingame;


import com.ifancc.Constants.Constant;
import com.ifancc.Constants.UserInfos;
import com.ifancc.braingame.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * 程序的主菜单页
 * @author liyang
 *
 */
public class MenuPage extends Activity {
	
	private Button dataAnalyseBtn = null;
	private Button startTrainBtn = null;
	private Button allGameBtn = null;
	private Button gameHelpBtn = null;
	private Button recommondBtn = null;
	
	private Intent intent = new Intent();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏模式
        
        Constant.al.add(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        dataAnalyseBtn = (Button)findViewById(R.id.dataAnalyseBtn);
        startTrainBtn = (Button)findViewById(R.id.startTrainBtn);
        allGameBtn = (Button)findViewById(R.id.allGameBtn);
        gameHelpBtn = (Button)findViewById(R.id.gameHelpBtn);
        recommondBtn = (Button)findViewById(R.id.recommond_btn);
        
        
        dataAnalyseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserInfos.isLogin){
					Toast.makeText(MenuPage.this, "亲，您还没有登录哦~", Toast.LENGTH_LONG).show();
				}else{
					turnToActivity(DataAnalyse.class);
				}
				
			}
		});
        
        
        startTrainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserInfos.isLogin){
					Toast.makeText(MenuPage.this, "亲，您还没有登录哦~", Toast.LENGTH_SHORT).show();
				}else if(UserInfos.isTrained){
					Toast.makeText(MenuPage.this, "亲，今天已经训练了哦", Toast.LENGTH_SHORT).show();
				}else{
					turnToActivity(Train.class);
				}
			}
		});
        
        
        allGameBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				turnToActivity(GamesGridView.class);
			}
		});
        
        gameHelpBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				turnToActivity(GameHelp.class);
				
			}
		});
	    recommondBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Constant.shelView != null){
					Constant.shelView.getShelf();
			        System.out.println("当前积分:" + Constant.shelView.getScore() + "");
				}
			}
		});
    }
    
    
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount() == 0&&UserInfos.userName != "noLogin"){
    		AlertDialog.Builder builder = new Builder(MenuPage.this);
    		builder.setMessage("确认要退出登录吗？？？");
    		builder.setTitle("提示");
    		builder.setPositiveButton("确认", new PositiveDialogListener());
    		builder.setNegativeButton("取消", new NegativeDialogListener());
    		builder.create().show();
    	}else
    		MenuPage.this.finish();
		return false;
    }
    
    
    private class PositiveDialogListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			MenuPage.this.finish();
		}
    }
    
    private class NegativeDialogListener implements DialogInterface.OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
    }
    

    private void turnToActivity(Class<?> className){
    	intent.setClass(MenuPage.this,className);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}

package com.ifancc.braingame;

import com.ifancc.braingame.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
/**
 * 实现gamelist功能的activity
 * @author liyang
 *
 */
public class GamesGridView extends Activity{
	private GridView gridView;
	public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏模式
		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		CoverFlow cf = new CoverFlow(this);
		cf.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.grid_view_bkground1));
		cf.setAdapter(new GamesAdapter(this));
		GamesAdapter imageAdapter = new GamesAdapter(this);
		cf.setAdapter(imageAdapter);
		cf.setSelection(2, true);
		cf.setAnimationDuration(1000);
		setContentView(cf);
		
		cf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("current : "+ arg2);
				Intent intent = new Intent(GamesGridView.this,Play.class);
				intent.putExtra("gameId", arg2);
				startActivity(intent);
			}
		});
	}
}

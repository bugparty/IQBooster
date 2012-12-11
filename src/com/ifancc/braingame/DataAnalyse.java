package com.ifancc.braingame;


import com.ifancc.Constants.Constant;
import com.ifancc.Constants.UserScores;
import com.ifancc.GameViews.DataView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * 实现成绩分析的Activity
 * @author liyang
 *
 */
public class DataAnalyse extends Activity {
	private DataView dataView = null;
	private int[] data = {UserScores.speedScore,UserScores.memoryScore,UserScores.attentionScore,UserScores.flexibilityScore};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏模式
        super.onCreate(savedInstanceState);
        
        dataView = new DataView(DataAnalyse.this, data);
        setContentView(dataView);
    }
}
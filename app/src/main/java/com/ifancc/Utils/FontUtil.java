package com.ifancc.Utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FontUtil {
	 public static void changeFonts(ViewGroup root,Typeface t){
	    	for(int i = 0;i<root.getChildCount();i++){
	    		View v = root.getChildAt(i);
	    		if(v instanceof TextView){
	    			((TextView) v).setTypeface(t);
	    		}else if(v instanceof EditText){
	    			((EditText) v).setTypeface(t);
	    		}
	    		else if (v instanceof LinearLayout){
	    			changeFonts((ViewGroup)v,t);
	    		}
	    	}
	    }
}

package com.ifancc.Constants;

import com.ifancc.braingame.R;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
/**
 * 游戏声音
 * @author liyang
 *
 */
public class SoundPlayer {
	private static SoundPool soundPool;
	
	private static boolean soundSt = true;
	
	private static Context context;
	private static Map<Integer,Integer> soundMap;
	
	private static Integer streamId;
	private static Integer clockId;
	
	public static void init(Context c){
		context = c;
		initSound();
	}
	
	@SuppressLint("UseSparseArrays")
	private static void initSound(){
		if(soundPool == null){
			soundPool = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
			soundMap = new HashMap<Integer,Integer>();
			
			//将声音资源装载到soundMap中
			soundMap.put(R.raw.right_sound,soundPool.load(context, R.raw.right_sound,1));
			soundMap.put(R.raw.wrong_sound,soundPool.load(context, R.raw.wrong_sound,1));
			soundMap.put(R.raw.clock_sound,soundPool.load(context, R.raw.clock_sound,1));
			soundMap.put(R.raw.touch_sound,soundPool.load(context, R.raw.touch_sound,1));
			soundMap.put(R.raw.round_sound, soundPool.load(context, R.raw.round_sound,1));
			soundMap.put(R.raw.round_sound2, soundPool.load(context, R.raw.round_sound2,1));
		}
	}
	
	
	public static void playSound(int resId){
		if(soundSt == false){
			return;
		}else{
			Integer soundId = soundMap.get(resId);
			if(soundId != null){
				if(resId == R.raw.clock_sound){
					streamId = soundPool.play(soundId,0, 1, 1, 0, 1);
					System.out.println("there is a value foe streamId:" + streamId.toString());
				}else{
					soundPool.play(soundId,0, 1, 1, 0, 1);
				}
			}
		}
	}
	
	public static void stopSound(){
		if(streamId != null){
			soundPool.stop(streamId);
			System.out.println("endSound:" + streamId.toString() );
		}
	}
	
}

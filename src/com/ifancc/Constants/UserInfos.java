package com.ifancc.Constants;

/**
 * 
 * @author liyang
 *该类存储保存用户信息的静态变量
 *需要在用户成功登陆后进行初始化
 */
public class UserInfos {
	private static int[] a1 = {1,2,3,4};
	private static int[] a2 = {1,2,3,4};
	
	
	
	
	public static String userName = null;
	public static int[] trainGames = null;
	public static Integer currentGame = null;
	public static boolean isLogin = false;
	public static boolean isTrained = false;
	
	public static void initInfos(String name,int[] games,Integer current,boolean isL,boolean isT){
		userName = name;
		trainGames = games;
		currentGame = current;
		isLogin = isL;
		isTrained = isT;
	}

}

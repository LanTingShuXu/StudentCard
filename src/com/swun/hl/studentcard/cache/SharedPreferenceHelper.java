package com.swun.hl.studentcard.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 集中处理SharedPreference的帮助类。防止太多SharedPreference混乱的问题【本类使用的是单例模式】
 * 
 * @author LANTINGSHUXU
 * 
 */
public class SharedPreferenceHelper {
	private Context context;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	// SharedPreference保存的文件名
	private final String fileName = "preferenceHelper";
	// 记录最近的账户名的key
	private final String recentAccount = "recentAccount";

	/**
	 * 单例模式
	 */
	public static SharedPreferenceHelper helper;

	private SharedPreferenceHelper(Context context) {
		this.context = context;
		sharedPreferences = this.context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	/**
	 * 获取SharedPreferenceHelper对象实例
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferenceHelper getInstance(Context context) {
		if (helper == null) {
			synchronized (context) {
				if (helper == null) {
					helper = new SharedPreferenceHelper(context);
				}
			}
		}
		return helper;
	}

	/**
	 * 设置最近登录的账户名（账号）
	 * 
	 * @param accountName
	 *            账号
	 */
	public void setRecentAccountName(String accountName) {
		editor.putString(recentAccount, accountName);
		editor.commit();
	}

	/**
	 * 获取最近登录的账户
	 * 
	 * @return 最近登录的账户名（账号）.如果没有。返回空字符串""
	 */
	public String getRecenetAccountName() {
		return sharedPreferences.getString(recentAccount, "");
	}

}

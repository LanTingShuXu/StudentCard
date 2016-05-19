package com.swun.hl.studentcard.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ���д���SharedPreference�İ����ࡣ��ֹ̫��SharedPreference���ҵ����⡾����ʹ�õ��ǵ���ģʽ��
 * 
 * @author LANTINGSHUXU
 * 
 */
public class SharedPreferenceHelper {
	private Context context;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	// SharedPreference������ļ���
	private final String fileName = "preferenceHelper";
	// ��¼������˻�����key
	private final String recentAccount = "recentAccount";

	/**
	 * ����ģʽ
	 */
	public static SharedPreferenceHelper helper;

	private SharedPreferenceHelper(Context context) {
		this.context = context;
		sharedPreferences = this.context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	/**
	 * ��ȡSharedPreferenceHelper����ʵ��
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
	 * ���������¼���˻������˺ţ�
	 * 
	 * @param accountName
	 *            �˺�
	 */
	public void setRecentAccountName(String accountName) {
		editor.putString(recentAccount, accountName);
		editor.commit();
	}

	/**
	 * ��ȡ�����¼���˻�
	 * 
	 * @return �����¼���˻������˺ţ�.���û�С����ؿ��ַ���""
	 */
	public String getRecenetAccountName() {
		return sharedPreferences.getString(recentAccount, "");
	}

}
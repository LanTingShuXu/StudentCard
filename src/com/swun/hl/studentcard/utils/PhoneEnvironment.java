package com.swun.hl.studentcard.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 获取手机的一些信息，例如：手机IMEI，手机型号等
 * 
 * @author 何玲
 * 
 */
public final class PhoneEnvironment {

	/**
	 * 获取手机的IMEI值
	 * 
	 * @param context
	 * @return 手机的IMEI值
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return mTm.getDeviceId();
	}

	/**
	 * 获取手机的唯一编号
	 * 
	 * @return
	 */
	public static String getFingerPrint() {
		return android.os.Build.FINGERPRINT;
	}

	/**
	 * 获取手机系统版本号
	 * 
	 * @return
	 */
	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return 手机型号
	 */
	public static String getPhoneType() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机厂商
	 * 
	 * @return 手机厂商
	 */
	public static String getPhoneFactory() {

		return android.os.Build.MANUFACTURER;
	}
}

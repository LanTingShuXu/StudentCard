package com.swun.hl.studentcard.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * ��ȡ�ֻ���һЩ��Ϣ�����磺�ֻ�IMEI���ֻ��ͺŵ�
 * 
 * @author ����
 * 
 */
public final class PhoneEnvironment {

	/**
	 * ��ȡ�ֻ���IMEIֵ
	 * 
	 * @param context
	 * @return �ֻ���IMEIֵ
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return mTm.getDeviceId();
	}

	/**
	 * ��ȡ�ֻ���Ψһ���
	 * 
	 * @return
	 */
	public static String getFingerPrint() {
		return android.os.Build.FINGERPRINT;
	}

	/**
	 * ��ȡ�ֻ�ϵͳ�汾��
	 * 
	 * @return
	 */
	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * ��ȡ�ֻ��ͺ�
	 * 
	 * @return �ֻ��ͺ�
	 */
	public static String getPhoneType() {
		return android.os.Build.MODEL;
	}

	/**
	 * ��ȡ�ֻ�����
	 * 
	 * @return �ֻ�����
	 */
	public static String getPhoneFactory() {

		return android.os.Build.MANUFACTURER;
	}
}

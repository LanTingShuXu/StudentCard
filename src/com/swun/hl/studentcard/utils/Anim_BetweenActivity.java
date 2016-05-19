package com.swun.hl.studentcard.utils;

import android.app.Activity;

import com.swun.hl.studentcard.R;

/**
 * ���õĽ����л�����Ч����װ��
 * 
 * @author ���
 * 
 */
public class Anim_BetweenActivity {

	private Anim_BetweenActivity() {
	}

	/**
	 * ����Ч���ǣ���߳�ȥ���ұ߽���������ʱ��300����
	 * 
	 * @param activity
	 *            ���ô˺�����Activity��
	 */
	public static void leftOut_rightIn(Activity activity) {

		activity.overridePendingTransition(R.anim.between_activity_right_in,
				R.anim.between_activity_left_out);
	}

	/**
	 * ����Ч���ǣ���߽������ұ߳�ȥ������ʱ��300����
	 * 
	 * @param activity
	 *            ���ô˺�����Activity��
	 */
	public static void leftIn_rightOut(Activity activity) {
		activity.overridePendingTransition(R.anim.between_activity_left_in,
				R.anim.between_activity_right_out);
	}

}

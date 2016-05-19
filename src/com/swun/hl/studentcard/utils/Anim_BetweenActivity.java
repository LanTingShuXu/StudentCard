package com.swun.hl.studentcard.utils;

import android.app.Activity;

import com.swun.hl.studentcard.R;

/**
 * 常用的界面切换动画效果封装类
 * 
 * @author 李长军
 * 
 */
public class Anim_BetweenActivity {

	private Anim_BetweenActivity() {
	}

	/**
	 * 动画效果是：左边出去，右边进来。动画时间300毫秒
	 * 
	 * @param activity
	 *            调用此函数的Activity。
	 */
	public static void leftOut_rightIn(Activity activity) {

		activity.overridePendingTransition(R.anim.between_activity_right_in,
				R.anim.between_activity_left_out);
	}

	/**
	 * 动画效果是：左边进来，右边出去。动画时间300毫秒
	 * 
	 * @param activity
	 *            调用此函数的Activity。
	 */
	public static void leftIn_rightOut(Activity activity) {
		activity.overridePendingTransition(R.anim.between_activity_left_in,
				R.anim.between_activity_right_out);
	}

}

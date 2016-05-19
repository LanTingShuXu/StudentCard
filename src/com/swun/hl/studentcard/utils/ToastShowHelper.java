package com.swun.hl.studentcard.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast显示帮助类
 * 
 * @author 何玲
 * 
 */
public final class ToastShowHelper {

	private static Toast toast;

	/**
	 * 显示Toast消息。显示时间Toast.LENGTH_SHORT,显示位置：Gravity.CENTER
	 * 
	 * @param context
	 *            上下文
	 * @param content
	 *            Toast内容
	 */
	public static void show(Context context, String content) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, "\n" + content + "\n",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 取消正在显示的Toast信息
	 */
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}

	}

}

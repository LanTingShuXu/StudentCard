package com.swun.hl.studentcard.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast��ʾ������
 * 
 * @author ����
 * 
 */
public final class ToastShowHelper {

	private static Toast toast;

	/**
	 * ��ʾToast��Ϣ����ʾʱ��Toast.LENGTH_SHORT,��ʾλ�ã�Gravity.CENTER
	 * 
	 * @param context
	 *            ������
	 * @param content
	 *            Toast����
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
	 * ȡ��������ʾ��Toast��Ϣ
	 */
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}

	}

}

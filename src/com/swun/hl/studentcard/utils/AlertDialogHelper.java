package com.swun.hl.studentcard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * �Ի���ĸ����ࡣ��װ��һЩ���õĽ���
 * 
 * @author LANTINGSHUXU
 * 
 */
public class AlertDialogHelper {
	/**
	 * ��ʾͨ����ʾ��
	 * 
	 * @param content
	 *            ��ʾ������
	 */
	public static void showCommonDialog(Context context, String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("��ʾ")//
				.setIcon(android.R.drawable.ic_dialog_alert)//
				.setMessage(content)//
				.setPositiveButton("ȷ��", null)//
				.setCancelable(false)//
				.create()//
				.show();
	}

	/**
	 * ��ʾ�Ի����û������ȷ�������˳����档����ȡ����ť
	 * 
	 * @param activity
	 * @param content
	 */
	public static void showDialogOkToExit(final Activity activity,
			String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("��ʾ")
				//
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(content)//
				.setNegativeButton("ȡ��", null)//
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
						Anim_BetweenActivity.leftIn_rightOut(activity);
					}
				})//
				.setCancelable(false)//
				.create()//
				.show();
	}

}

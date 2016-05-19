package com.swun.hl.studentcard.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 对话框的辅助类。封装了一些常用的交互
 * 
 * @author LANTINGSHUXU
 * 
 */
public class AlertDialogHelper {
	/**
	 * 显示通用提示框
	 * 
	 * @param content
	 *            提示框内容
	 */
	public static void showCommonDialog(Context context, String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示")//
				.setIcon(android.R.drawable.ic_dialog_alert)//
				.setMessage(content)//
				.setPositiveButton("确定", null)//
				.setCancelable(false)//
				.create()//
				.show();
	}

	/**
	 * 显示对话框。用户点击“确定”后退出界面。包含取消按钮
	 * 
	 * @param activity
	 * @param content
	 */
	public static void showDialogOkToExit(final Activity activity,
			String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("提示")
				//
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(content)//
				.setNegativeButton("取消", null)//
				.setPositiveButton("确定", new OnClickListener() {
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

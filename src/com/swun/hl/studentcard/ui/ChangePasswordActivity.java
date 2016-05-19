package com.swun.hl.studentcard.ui;

import org.jsoup.helper.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.CommonCallback;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * 修改密码界面
 * 
 * @author LANTINGSHUXU
 * 
 */
public class ChangePasswordActivity extends Activity {

	private EditText edt_psw1, edt_psw2, edt_psw3;
	private StudentCardClient client = StudentCardClient
			.getInstance(getApplication());

	// 保存token
	private String token = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		viewFind();
		getToken();
	}

	@Override
	public void onBackPressed() {
		this.finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	/**
	 * 提交按钮点击事件
	 * 
	 * @param v
	 */
	public void clk_commit(View v) {
		if (inputIsRight()) {
			final Button b = (Button) v;
			b.setText("正在为您提交..");
			b.setEnabled(false);
			final String newPsw = edt_psw2.getText().toString().trim();
			client.commitChangePassword(newPsw, token, new CommonCallback() {
				@Override
				public void onCallback(boolean success, String info) {
					b.setText("提  交");
					b.setEnabled(true);
					if (success) {
						LoginActivity.PASSWORD = newPsw;
						showOtherDialog(info);
					} else {
						AlertDialogHelper.showCommonDialog(
								ChangePasswordActivity.this, info);
					}
				}
			});
		}
	}

	/**
	 * 判断输入是否合法。
	 * 
	 * @return true表示合法
	 */
	private boolean inputIsRight() {
		if (edt_psw1.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"您还未输入原密码");
			return false;
		} else if (edt_psw2.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"您还未输入新密码");
			return false;
		} else if (edt_psw3.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"您还未输入确认密码");
			return false;
		} else if (!edt_psw1.getText().toString().trim()
				.equals(LoginActivity.PASSWORD)) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"原密码不正确！");
			return false;
		} else if (edt_psw2.getText().toString().trim().length() != 6
				|| edt_psw3.getText().toString().trim().length() != 6) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"密码位数不正确");
			return false;
		} else if (!StringUtil.isNumeric(edt_psw2.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"密码必须为6位数字！");
			return false;
		} else if (!edt_psw2.getText().toString().trim()
				.equals(edt_psw3.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"新密码与确认密码不匹配！");
			return false;
		}
		return true;
	}

	/**
	 * 获取当前会话的token
	 */
	private void getToken() {
		client.getChangePasswordToken(new CommonCallback() {
			@Override
			public void onCallback(boolean success, String info) {
				if (success) {
					token = info;
				} else {
					Toast.makeText(ChangePasswordActivity.this,
							"token获取失败！请检查网络", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 显示点击“确认”退出的对话框
	 * 
	 * @param content
	 *            提示框内容
	 */
	private void showOtherDialog(String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示").setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(content)
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onBackPressed();
					}
				}).setCancelable(false).create().show();
	}

	private void viewFind() {
		edt_psw1 = (EditText) findViewById(R.id.aty_changePsw_password1);
		edt_psw2 = (EditText) findViewById(R.id.aty_changePsw_password2);
		edt_psw3 = (EditText) findViewById(R.id.aty_changePsw_password3);
	}

}

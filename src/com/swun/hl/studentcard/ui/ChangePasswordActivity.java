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
 * �޸��������
 * 
 * @author LANTINGSHUXU
 * 
 */
public class ChangePasswordActivity extends Activity {

	private EditText edt_psw1, edt_psw2, edt_psw3;
	private StudentCardClient client = StudentCardClient
			.getInstance(getApplication());

	// ����token
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
	 * �ύ��ť����¼�
	 * 
	 * @param v
	 */
	public void clk_commit(View v) {
		if (inputIsRight()) {
			final Button b = (Button) v;
			b.setText("����Ϊ���ύ..");
			b.setEnabled(false);
			final String newPsw = edt_psw2.getText().toString().trim();
			client.commitChangePassword(newPsw, token, new CommonCallback() {
				@Override
				public void onCallback(boolean success, String info) {
					b.setText("��  ��");
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
	 * �ж������Ƿ�Ϸ���
	 * 
	 * @return true��ʾ�Ϸ�
	 */
	private boolean inputIsRight() {
		if (edt_psw1.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"����δ����ԭ����");
			return false;
		} else if (edt_psw2.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"����δ����������");
			return false;
		} else if (edt_psw3.getText().toString().trim().equals("")) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"����δ����ȷ������");
			return false;
		} else if (!edt_psw1.getText().toString().trim()
				.equals(LoginActivity.PASSWORD)) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"ԭ���벻��ȷ��");
			return false;
		} else if (edt_psw2.getText().toString().trim().length() != 6
				|| edt_psw3.getText().toString().trim().length() != 6) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"����λ������ȷ");
			return false;
		} else if (!StringUtil.isNumeric(edt_psw2.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"�������Ϊ6λ���֣�");
			return false;
		} else if (!edt_psw2.getText().toString().trim()
				.equals(edt_psw3.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(ChangePasswordActivity.this,
					"��������ȷ�����벻ƥ�䣡");
			return false;
		}
		return true;
	}

	/**
	 * ��ȡ��ǰ�Ự��token
	 */
	private void getToken() {
		client.getChangePasswordToken(new CommonCallback() {
			@Override
			public void onCallback(boolean success, String info) {
				if (success) {
					token = info;
				} else {
					Toast.makeText(ChangePasswordActivity.this,
							"token��ȡʧ�ܣ���������", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * ��ʾ�����ȷ�ϡ��˳��ĶԻ���
	 * 
	 * @param content
	 *            ��ʾ������
	 */
	private void showOtherDialog(String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ʾ").setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(content)
				.setPositiveButton("ȷ��", new OnClickListener() {
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

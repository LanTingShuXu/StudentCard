package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobAccount;
import com.swun.hl.studentcard.cache.SharedPreferenceHelper;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.CommonCallback;
import com.swun.hl.studentcard.service.BackService;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * һ��ͨ��¼���档�˽��滹�У������жϣ�����Ϸ����жϣ��ȳ����ж�δ��ɡ�-by�����ᣬ2016.02.26 <br />
 * ��ɻ����Ϸ������롣-by������2016.02.27
 * 
 * @author ����
 * 
 */
public class LoginActivity extends Activity {

	private ImageView img_checkCode;// ��֤��ͼƬ
	private EditText edt_checkCode;// ������֤��������
	private EditText edt_account;// �˺������
	private EditText edt_password;// ���������
	private StudentCardClient studentCardClient;// ѧ��һ��ͨ�ͻ�����
	private Handler handler = new Handler();
	// ���й���SharedPreference�İ�����
	private SharedPreferenceHelper preferenceHelper;

	public static String PASSWORD = "";// ��¼��¼�����롣

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (checkStatus()) {// ���û�е�¼����
			viewFind();
			init();
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��ʾ").setMessage("\n��Ҫ�˳������\n")
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginActivity.this.finish();
						// 0.5����˳�ϵͳ
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								System.exit(0);
							}
						}, 500);
					}
				}).setNegativeButton("ȡ��", null).create().show();
	}

	/**
	 * �����ѯ��Ѱ�ť������
	 * 
	 * @param v
	 */
	public void clk_queryElectric(View v) {
		startActivity(new Intent(this, QueryElectricActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(this);
	}

	private void viewFind() {
		img_checkCode = (ImageView) findViewById(R.id.aty_login_img_checkCode);
		edt_checkCode = (EditText) findViewById(R.id.aty_login_edt_checkCode);
		edt_account = (EditText) findViewById(R.id.aty_login_account);
		edt_password = (EditText) findViewById(R.id.aty_login_password);
	}

	/**
	 * �ж�֮ǰ�Ƿ��¼����ͨ��Service�е�Fragment�Ƿ�Ϊ�տ����жϳ������������¼��������ֱ�Ӵ�home����
	 * 
	 * @return true ��ʾû�е�¼��
	 */
	private boolean checkStatus() {
		// ���Service���Ѿ��й�Fragment����ʾ��¼����ֱ����ת��home����
		if (BackService.contains != null) {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ��ʼ������
	 */
	private void init() {
		BmobUpdateAgent.update(this);// ������
		preferenceHelper = SharedPreferenceHelper.getInstance(this);
		studentCardClient = StudentCardClient.getInstance(this);
		edt_account.setText(preferenceHelper.getRecenetAccountName());
		studentCardClient.getCheckCodeImage(handler, img_checkCode);
	}

	/**
	 * �ж������Ƿ�Ϸ�
	 * 
	 * @return true��ʾ�Ϸ���
	 */
	private boolean checkInput() {
		if ("".equals(edt_account.getText().toString().trim())) {// �������Ϊ��
			AlertDialogHelper.showCommonDialog(this, "����������ѧ��");
			return false;
		} else if ("".equals(edt_password.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(this, "��������������");
			return false;
		} else if ("".equals(edt_checkCode.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(this, "��������֤��");
			return false;
		} else if (edt_checkCode.getText().toString().trim().length() != 4) {
			AlertDialogHelper.showCommonDialog(this, "��֤�볤�Ȳ���ȷ��");
			return false;
		}
		return true;
	}

	/**
	 * �����¼����
	 */
	private void loginRequest(final Button btn) {
		final String recentName = edt_account.getText().toString().trim();
		studentCardClient.loginRequest(//
				recentName,//
				edt_password.getText().toString().trim(),//
				edt_checkCode.getText().toString().trim(),//
				new CommonCallback() {// �ص�����
					@Override
					public void onCallback(boolean success, String info) {
						if (success) {// �����¼�ɹ�,��ת��������
							preferenceHelper.setRecentAccountName(recentName);
							LoginActivity.this.startActivity(new Intent(
									LoginActivity.this, HomeActivity.class));
							PASSWORD = edt_password.getText().toString();
							checkBmobAccount(recentName);
							LoginActivity.this.finish();
							Anim_BetweenActivity
									.leftOut_rightIn(LoginActivity.this);
						} else {// ��¼ʧ��
							handler.post(new Runnable() {
								@Override
								public void run() {
									btn.setText("��¼");
									btn.setEnabled(true);
								}
							});
							AlertDialogHelper.showCommonDialog(
									LoginActivity.this, info);
							// ���»�ȡ����
							studentCardClient.getCheckCodeImage(handler,
									img_checkCode);
						}
					}
				});
	}

	/**
	 * �ж�Bmob�˻�
	 */
	private void checkBmobAccount(String username) {
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		// ֮ǰ��¼���������ٵ�¼
		if (bmobUser != null && username.equals(bmobUser.getUsername())) {

		}// ��Ҫ���µ�¼
		else {
			registorUser(username);
		}
	}

	/**
	 * ע��Bmob<br/>
	 * ע�⣺�������� �˻��������붼����Ϊһ���ġ�Ŀ���Ƿ�ֹй¶�û���˽���ݡ�<br/>
	 * ��������������������ܣ����ǽ�����ȡ�˺���Ϣ����ʶ�û����ѡ� Bmob��������Ҫ�Ǵ洢�û����˻���Ϣ��û���ռ��û����κ���˽���ݣ�
	 */
	private void registorUser(String username) {
		final BmobAccount user = new BmobAccount();
		user.setUsername(username);
		user.setPassword(username);
		user.signUp(this, new SaveListener() {
			@Override
			public void onSuccess() {
				loginBmob(user);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				loginBmob(user);
			}
		});
	}

	/**
	 * ��¼Bmob
	 * 
	 * @param user
	 */
	private void loginBmob(BmobAccount user) {
		user.login(this, new SaveListener() {
			@Override
			public void onSuccess() {
				Log.i("lcj", "��¼�ɹ�");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("lcj", "��¼ʧ�ܣ�" + arg1);
			}
		});
	}

	/**
	 * ��¼��ť�ĵ���¼�
	 * 
	 * @param v
	 *            ��ť����
	 */
	public void clk_login(View v) {
		if (checkInput()) {// ���������Ƿ�Ϸ�
			Button btn = (Button) v;
			btn.setText("��¼��...");
			btn.setEnabled(false);
			loginRequest(btn);
		}

	}

}

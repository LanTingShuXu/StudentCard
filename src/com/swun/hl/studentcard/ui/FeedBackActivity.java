package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.swun.hl.studentcard.MyApp;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobAccount;
import com.swun.hl.studentcard.bmobBean.BmobFeedBack;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;
import com.swun.hl.studentcard.utils.PhoneEnvironment;

/**
 * �û���������
 * 
 * @author LANTINGSHUXU
 * 
 */
public class FeedBackActivity extends Activity {

	// ��ϵ��ʽ�ͷ�������
	private EditText edt_contact, edt_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		viewFind();
	}

	@Override
	public void onBackPressed() {
		if (!TextUtils.isEmpty(edt_contact.getText().toString().trim())
				|| !TextUtils.isEmpty(edt_content.getText().toString().trim())) {
			AlertDialogHelper.showDialogOkToExit(this, "������δ�ύ�ķ�������Ҫ�˳���");
		} else {
			finish();
			Anim_BetweenActivity.leftIn_rightOut(this);
		}

	}

	/**
	 * ����ύ������ť
	 * 
	 * @param v
	 */
	public void clk_feedback(View v) {
		if (checkInput()) {
			commitFeedBack();
		}
	}

	/**
	 * ����ύ�����Ƿ�Ϸ�
	 * 
	 * @return true��ʾ���붼�Ϸ�
	 */
	private boolean checkInput() {
		String content = edt_content.getText().toString().trim();
		if ("".equals(content)) {
			AlertDialogHelper.showCommonDialog(this, "����δ���뷴�����ݹ�~");
			return false;
		}

		return true;
	}

	/**
	 * �ύ��������
	 */
	private void commitFeedBack() {
		BmobFeedBack feedBack = new BmobFeedBack();
		feedBack.setAccount(BmobUser.getCurrentUser(this, BmobAccount.class));// �˻�
		feedBack.setContact(edt_contact.getText().toString().trim());// ��ϵ��ʽ
		feedBack.setContent(edt_content.getText().toString().trim());// ����
		feedBack.setAppVersionName(MyApp.getVersionName(this));// ����汾
		feedBack.setAndroidVersion("android:" + PhoneEnvironment.getOSVersion());// ϵͳ�汾
		feedBack.setPhoneFactory(PhoneEnvironment.getPhoneFactory());// �ֻ�����
		feedBack.setPhoneType(PhoneEnvironment.getPhoneType());// �ֻ��ͺ�
		feedBack.setPhoneIMEI(PhoneEnvironment.getIMEI(this));// �ֻ�IMEI
		feedBack.setPhoneFingerPrint(PhoneEnvironment.getFingerPrint());// ��ȡ�ֻ�Ψһʶ���

		feedBack.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				edt_content.setText("");
				edt_contact.setText("");
				AlertDialogHelper.showDialogOkToExit(FeedBackActivity.this,
						"��л���ķ���~");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				AlertDialogHelper.showCommonDialog(FeedBackActivity.this,
						"����ʧ�ܣ�������������");
			}
		});
	}

	private void viewFind() {
		edt_contact = (EditText) findViewById(R.id.aty_feedBack_contact);
		edt_content = (EditText) findViewById(R.id.aty_feedBack_content);
	}
}

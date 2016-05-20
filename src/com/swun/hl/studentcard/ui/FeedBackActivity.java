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
 * 用户反馈界面
 * 
 * @author LANTINGSHUXU
 * 
 */
public class FeedBackActivity extends Activity {

	// 联系方式和反馈内容
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
			AlertDialogHelper.showDialogOkToExit(this, "您还有未提交的反馈，您要退出吗？");
		} else {
			finish();
			Anim_BetweenActivity.leftIn_rightOut(this);
		}

	}

	/**
	 * 点击提交反馈按钮
	 * 
	 * @param v
	 */
	public void clk_feedback(View v) {
		if (checkInput()) {
			commitFeedBack();
		}
	}

	/**
	 * 检测提交输入是否合法
	 * 
	 * @return true表示输入都合法
	 */
	private boolean checkInput() {
		String content = edt_content.getText().toString().trim();
		if ("".equals(content)) {
			AlertDialogHelper.showCommonDialog(this, "您还未输入反馈内容哈~");
			return false;
		}

		return true;
	}

	/**
	 * 提交反馈内容
	 */
	private void commitFeedBack() {
		BmobFeedBack feedBack = new BmobFeedBack();
		feedBack.setAccount(BmobUser.getCurrentUser(this, BmobAccount.class));// 账户
		feedBack.setContact(edt_contact.getText().toString().trim());// 联系方式
		feedBack.setContent(edt_content.getText().toString().trim());// 内容
		feedBack.setAppVersionName(MyApp.getVersionName(this));// 软件版本
		feedBack.setAndroidVersion("android:" + PhoneEnvironment.getOSVersion());// 系统版本
		feedBack.setPhoneFactory(PhoneEnvironment.getPhoneFactory());// 手机厂商
		feedBack.setPhoneType(PhoneEnvironment.getPhoneType());// 手机型号
		feedBack.setPhoneIMEI(PhoneEnvironment.getIMEI(this));// 手机IMEI
		feedBack.setPhoneFingerPrint(PhoneEnvironment.getFingerPrint());// 获取手机唯一识别号

		feedBack.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				edt_content.setText("");
				edt_contact.setText("");
				AlertDialogHelper.showDialogOkToExit(FeedBackActivity.this,
						"感谢您的反馈~");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				AlertDialogHelper.showCommonDialog(FeedBackActivity.this,
						"反馈失败，请检查网络设置");
			}
		});
	}

	private void viewFind() {
		edt_contact = (EditText) findViewById(R.id.aty_feedBack_contact);
		edt_content = (EditText) findViewById(R.id.aty_feedBack_content);
	}
}

package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.swun.hl.studentcard.MyApp;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.myview.CustomTopBar;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * "关于"界面
 * 
 * @author LANTINGSHUXU
 * 
 */
public class AboutUsActivity extends Activity implements
		CustomTopBar.callBackListener {
	private TextView tv_version;
	private CustomTopBar topBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		viewFind();
		init();
	}

	@Override
	public void onBackPressed() {
		this.finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	@Override
	public void leftClick(View view) {

	}

	@Override
	public void rightClick(View view) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "share");
		intent.putExtra(Intent.EXTRA_TEXT,
				"民大的童鞋有福咯~现在手机也能管理一卡通啦，快快来下载吧，下载地址：http://MinDaKaBao.bmob.cn ~“民大卡包”");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
	}

	private void viewFind() {
		tv_version = (TextView) findViewById(R.id.aty_aboutUs_version);
		topBar = (CustomTopBar) findViewById(R.id.aty_aboutUs_topBar);
	}

	private void init() {
		// 设置回调接口
		topBar.setCallBackListener(this);
		tv_version.setText("版本：" + MyApp.getVersionName(this));
	}

}

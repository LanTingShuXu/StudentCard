package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.swun.hl.studentcard.MyApp;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * "关于"界面
 * 
 * @author LANTINGSHUXU
 * 
 */
public class AboutUsActivity extends Activity {
	private TextView tv_version;

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

	private void viewFind() {
		tv_version = (TextView) findViewById(R.id.aty_aboutUs_version);
	}

	private void init() {
		tv_version.setText("版本：" + MyApp.getVersionName(this));
	}
}

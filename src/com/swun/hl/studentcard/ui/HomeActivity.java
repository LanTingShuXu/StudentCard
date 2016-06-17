package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.service.BackService;
import com.swun.hl.studentcard.ui.fragment.ChargeFragment;
import com.swun.hl.studentcard.ui.fragment.CircleFragment;
import com.swun.hl.studentcard.ui.fragment.StudentcardFragment;

/**
 * 主界面：分为3个tab，分别为：一卡通、缴费、圈子
 * 
 * @author 何玲
 * 
 */
public class HomeActivity extends Activity {

	// 3个Fragment分别为：一卡通、缴费、圈子
	private Fragment[] contains = new Fragment[3];
	// 3个常量分别代表三个Fragment的脚标
	private final int STUDENT_CARD = 0, CHARGE = 1, CIRCLE = 2;
	// 记录当前的Fragment的index(默认为第一个)
	private int currentIndex = STUDENT_CARD;

	private TextView tv_studentCard, tv_charge, tv_circle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		viewFind();
		initFragment();
		initService();
	}
	
	@Override
	public void onBackPressed() {
		// 如果是当前正在圈子界面，同时圈子界面的UserCenter是展开的，我们应该先折叠UserCenter界面
		if (currentIndex == CIRCLE) {
			CircleFragment circleFragment = (CircleFragment) contains[CIRCLE];
			if (circleFragment.collpaseUserCenter()) {
				return;
			}
		}
		super.onBackPressed();
	}

	/**
	 * 点击底部指示器的监听事件
	 * 
	 * @param v
	 *            被点击的对象
	 */
	public void clk_indicator(View v) {
		int index = 0;
		switch (v.getId()) {
		case R.id.aty_home_studentcard:// 一卡通
			index = STUDENT_CARD;
			break;
		case R.id.aty_home_charge:// 缴费
			index = CHARGE;
			break;
		case R.id.aty_home_sns:// 圈子
			index = CIRCLE;
			break;
		}
		// 如果点击的是当前Fragment
		if (index == currentIndex) {
			return;
		}
		currentIndex = index;
		resetIndicator();
		((TextView) v).setTextColor(Color.YELLOW);
		setFragment(index);
	}

	/**
	 * 启动后台服务
	 */
	private void initService() {
		startService(new Intent(this, BackService.class));
	}

	private void viewFind() {
		tv_studentCard = (TextView) findViewById(R.id.aty_home_studentcard);
		tv_charge = (TextView) findViewById(R.id.aty_home_charge);
		tv_circle = (TextView) findViewById(R.id.aty_home_sns);
	}

	/**
	 * 初始化各个Fragment
	 */
	private void initFragment() {
		// 默认是选中的第一个Fragment
		tv_studentCard.setTextColor(Color.YELLOW);
		// 如果Service中存在之前的Fragment，我们直接填充之前的Fragment
		if (BackService.contains == null) {
			// 一卡通Fragment
			contains[STUDENT_CARD] = new StudentcardFragment();
			// 缴费Fragment
			contains[CHARGE] = new ChargeFragment();
			// 圈子Fragment
			contains[CIRCLE] = new CircleFragment();

			BackService.contains = contains;
		} else {
			contains = BackService.contains;
		}

		getFragmentManager().beginTransaction()
				.add(R.id.aty_home_content, contains[STUDENT_CARD])
				//
				.add(R.id.aty_home_content, contains[CHARGE])
				//
				.add(R.id.aty_home_content, contains[CIRCLE])
				//
				.hide(contains[CHARGE])//
				.hide(contains[CIRCLE])//
				.show(contains[STUDENT_CARD]).commit();//

	}

	/**
	 * 重置指示器
	 */
	private void resetIndicator() {
		tv_charge.setTextColor(Color.WHITE);
		tv_circle.setTextColor(Color.WHITE);
		tv_studentCard.setTextColor(Color.WHITE);
	}

	/**
	 * 设置当前可见的Fragment
	 * 
	 * @param index
	 *            Fragment的索引
	 */
	private void setFragment(int index) {
		getFragmentManager().beginTransaction()//
				.hide(contains[CHARGE])//
				.hide(contains[CIRCLE])//
				.hide(contains[STUDENT_CARD])//
				.show(contains[index]).commit();//
		contains[index].onStart();// 调用该界面的onStart()方法
	}

}

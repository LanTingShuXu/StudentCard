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
 * �����棺��Ϊ3��tab���ֱ�Ϊ��һ��ͨ���ɷѡ�Ȧ��
 * 
 * @author ����
 * 
 */
public class HomeActivity extends Activity {

	// 3��Fragment�ֱ�Ϊ��һ��ͨ���ɷѡ�Ȧ��
	private Fragment[] contains = new Fragment[3];
	// 3�������ֱ��������Fragment�Ľű�
	private final int STUDENT_CARD = 0, CHARGE = 1, CIRCLE = 2;
	// ��¼��ǰ��Fragment��index(Ĭ��Ϊ��һ��)
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
		// ����ǵ�ǰ����Ȧ�ӽ��棬ͬʱȦ�ӽ����UserCenter��չ���ģ�����Ӧ�����۵�UserCenter����
		if (currentIndex == CIRCLE) {
			CircleFragment circleFragment = (CircleFragment) contains[CIRCLE];
			if (circleFragment.collpaseUserCenter()) {
				return;
			}
		}
		super.onBackPressed();
	}

	/**
	 * ����ײ�ָʾ���ļ����¼�
	 * 
	 * @param v
	 *            ������Ķ���
	 */
	public void clk_indicator(View v) {
		int index = 0;
		switch (v.getId()) {
		case R.id.aty_home_studentcard:// һ��ͨ
			index = STUDENT_CARD;
			break;
		case R.id.aty_home_charge:// �ɷ�
			index = CHARGE;
			break;
		case R.id.aty_home_sns:// Ȧ��
			index = CIRCLE;
			break;
		}
		// ���������ǵ�ǰFragment
		if (index == currentIndex) {
			return;
		}
		currentIndex = index;
		resetIndicator();
		((TextView) v).setTextColor(Color.YELLOW);
		setFragment(index);
	}

	/**
	 * ������̨����
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
	 * ��ʼ������Fragment
	 */
	private void initFragment() {
		// Ĭ����ѡ�еĵ�һ��Fragment
		tv_studentCard.setTextColor(Color.YELLOW);
		// ���Service�д���֮ǰ��Fragment������ֱ�����֮ǰ��Fragment
		if (BackService.contains == null) {
			// һ��ͨFragment
			contains[STUDENT_CARD] = new StudentcardFragment();
			// �ɷ�Fragment
			contains[CHARGE] = new ChargeFragment();
			// Ȧ��Fragment
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
	 * ����ָʾ��
	 */
	private void resetIndicator() {
		tv_charge.setTextColor(Color.WHITE);
		tv_circle.setTextColor(Color.WHITE);
		tv_studentCard.setTextColor(Color.WHITE);
	}

	/**
	 * ���õ�ǰ�ɼ���Fragment
	 * 
	 * @param index
	 *            Fragment������
	 */
	private void setFragment(int index) {
		getFragmentManager().beginTransaction()//
				.hide(contains[CHARGE])//
				.hide(contains[CIRCLE])//
				.hide(contains[STUDENT_CARD])//
				.show(contains[index]).commit();//
		contains[index].onStart();// ���øý����onStart()����
	}

}

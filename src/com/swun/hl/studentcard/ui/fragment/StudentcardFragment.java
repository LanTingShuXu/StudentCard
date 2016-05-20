package com.swun.hl.studentcard.ui.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.pulltorefresh.library.PullToRefreshBase;
import com.github.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.github.pulltorefresh.library.PullToRefreshScrollView;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bean.Student;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.CommonCallback;
import com.swun.hl.studentcard.client.StudentCardClient.StudentInfoCallback;
import com.swun.hl.studentcard.ui.AboutUsActivity;
import com.swun.hl.studentcard.ui.ChangePasswordActivity;
import com.swun.hl.studentcard.ui.CostInfoActivity;
import com.swun.hl.studentcard.ui.FeedBackActivity;
import com.swun.hl.studentcard.ui.QueryElectricActivity;
import com.swun.hl.studentcard.ui.ReportLostActivity;
import com.swun.hl.studentcard.ui.StudentInfoActivity;
import com.swun.hl.studentcard.ui.TransInfoActivity;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * ��һ��ͨ����Fragment
 * 
 * @author ����
 * 
 */
public class StudentcardFragment extends Fragment implements
		View.OnClickListener, OnRefreshListener<ScrollView> {

	// ѧ�����ͻ���
	private StudentCardClient studentCardClient = StudentCardClient
			.getInstance(getActivity());
	private Student student = null;// ѧ������
	private String balInfo = "";// �����Ϣ

	// �����������Ϣ
	private TextView tv_name, tv_bal;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private View layout_info, tv_costInfo, tv_transInfo, tv_changePsw,
			tv_reportLost, tv_aboutUs, tv_queryElectric, tv_exit, tv_feedBack;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View content = inflater.inflate(R.layout.fragment_studentcard,
				container, false);
		viewFind(content);
		restoreSavedData();
		initListener();
		return content;
	}

	@Override
	public void onStart() {
		getDataFromServer();
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_studentcard_layout_info:// �����ϸ��Ϣ
			clk_studentInfo(v);
			break;
		case R.id.fragment_studentcard_costInfo:// ������������ѯ
			clk_costInfo(v);
			break;
		case R.id.fragment_studentcard_transInfo:// ���Ȧ����ϸ��ѯ
			clk_transInfo(v);
			break;
		case R.id.fragment_studentcard_changePsw:// ����޸�����
			clk_changePsw(v);
			break;
		case R.id.fragment_studentcard_reportLost:// �����Ƭ��ʧ
			clk_reportLost(v);
			break;
		case R.id.fragment_studentcard_queryElectric:// �����Ѳ�ѯ
			clk_queryElectric(v);
			break;
		case R.id.fragment_studentcard_aboutUs:// �����������
			clk_about(v);
			break;
		case R.id.fragment_studentcard_exit:// ����˳�
			clk_exit(v);
			break;
		case R.id.fragment_studentcard_feedback:// �������
			clk_feedBack(v);
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		getDataFromServer();
	}

	/**
	 * �����Ѳ�ѯ
	 * 
	 * @param v
	 */
	private void clk_queryElectric(View v) {
		startActivity(new Intent(getActivity(), QueryElectricActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * �������
	 * 
	 * @param v
	 */
	private void clk_feedBack(View v) {
		startActivity(new Intent(getActivity(), FeedBackActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * ���������
	 * 
	 * @param v
	 */
	private void clk_about(View v) {
		startActivity(new Intent(getActivity(), AboutUsActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * �����Ƭ��ʧ
	 * 
	 * @param v
	 */
	private void clk_reportLost(View v) {
		startActivity(new Intent(getActivity(), ReportLostActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * ���������ϸ
	 * 
	 * @param v
	 *            ���������
	 */
	private void clk_changePsw(View v) {
		startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * ���������ϸ
	 * 
	 * @param v
	 *            ���������
	 */
	private void clk_transInfo(View v) {
		startActivity(new Intent(getActivity(), TransInfoActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * ���������ϸ
	 * 
	 * @param v
	 *            ���������
	 */
	private void clk_costInfo(View v) {
		startActivity(new Intent(getActivity(), CostInfoActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * ����鿴������Ϣ
	 * 
	 * @param v
	 *            ������Ķ���
	 */
	private void clk_studentInfo(View v) {
		Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
		intent.putExtra(StudentInfoActivity.INTENT_EXTRA_KEY, student);
		getActivity().startActivity(intent);
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * ������˳���
	 * 
	 * @param v
	 */
	private void clk_exit(View v) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("��ʾ").setMessage("\n��Ҫ�˳������\n")
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StudentcardFragment.this.getActivity().finish();
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

	private void viewFind(View v) {
		tv_name = (TextView) v.findViewById(R.id.fragment_studentcard_name);
		tv_bal = (TextView) v.findViewById(R.id.fragment_studentcard_bal);
		layout_info = v.findViewById(R.id.fragment_studentcard_layout_info);
		tv_costInfo = v.findViewById(R.id.fragment_studentcard_costInfo);
		tv_transInfo = v.findViewById(R.id.fragment_studentcard_transInfo);
		tv_changePsw = v.findViewById(R.id.fragment_studentcard_changePsw);
		tv_reportLost = v.findViewById(R.id.fragment_studentcard_reportLost);
		tv_aboutUs = v.findViewById(R.id.fragment_studentcard_aboutUs);
		tv_queryElectric = v
				.findViewById(R.id.fragment_studentcard_queryElectric);
		tv_exit = v.findViewById(R.id.fragment_studentcard_exit);
		tv_feedBack = v.findViewById(R.id.fragment_studentcard_feedback);
		pullToRefreshScrollView = (PullToRefreshScrollView) v
				.findViewById(R.id.fragment_studentCard_scroll);
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		layout_info.setOnClickListener(this);
		tv_costInfo.setOnClickListener(this);
		tv_transInfo.setOnClickListener(this);
		tv_changePsw.setOnClickListener(this);
		tv_reportLost.setOnClickListener(this);
		tv_aboutUs.setOnClickListener(this);
		tv_queryElectric.setOnClickListener(this);
		tv_exit.setOnClickListener(this);
		pullToRefreshScrollView.setOnRefreshListener(this);
		tv_feedBack.setOnClickListener(this);
	}

	/**
	 * �ӷ���˻�ȡѧ�����������
	 */
	private void getDataFromServer() {
		getStudentNameFromServer();// ��ȡѧ����Ϣ
		getChargeInfo();// ��ȡ�����Ϣ
	}

	/**
	 * �ָ�֮ǰ������
	 */
	private void restoreSavedData() {
		if (student != null) {
			tv_name.setText(student.getName());
			tv_bal.setText(balInfo);
		}
	}

	/**
	 * �ӷ�������ȡѧ����Ϣ
	 */
	private void getStudentNameFromServer() {
		studentCardClient.getStudentInfo(new StudentInfoCallback() {
			@Override
			public void onCallback(Student student) {
				pullToRefreshScrollView.onRefreshComplete();
				if (student != null) {// �����ȡ�������µ�Student��Ϣ
					StudentcardFragment.this.student = student;
					tv_name.setText(student.getName());
				} else {
					tv_name.setText("��Ϣ��ȡʧ��");
				}
			}
		});
	}

	/**
	 * ��ȡ�����Ϣ
	 */
	private void getChargeInfo() {
		studentCardClient.getChargeInfo(new CommonCallback() {
			@Override
			public void onCallback(boolean success, String info) {
				pullToRefreshScrollView.onRefreshComplete();
				tv_bal.setText(info);
				balInfo = info;
			}
		});
	}

}

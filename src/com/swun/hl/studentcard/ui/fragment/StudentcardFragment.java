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
 * “一卡通”的Fragment
 * 
 * @author 何玲
 * 
 */
public class StudentcardFragment extends Fragment implements
		View.OnClickListener, OnRefreshListener<ScrollView> {

	// 学生卡客户端
	private StudentCardClient studentCardClient = StudentCardClient
			.getInstance(getActivity());
	private Student student = null;// 学生对象
	private String balInfo = "";// 余额信息

	// 姓名和余额信息
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
		case R.id.fragment_studentcard_layout_info:// 点击详细信息
			clk_studentInfo(v);
			break;
		case R.id.fragment_studentcard_costInfo:// 点击消费详情查询
			clk_costInfo(v);
			break;
		case R.id.fragment_studentcard_transInfo:// 点击圈存明细查询
			clk_transInfo(v);
			break;
		case R.id.fragment_studentcard_changePsw:// 点击修改密码
			clk_changePsw(v);
			break;
		case R.id.fragment_studentcard_reportLost:// 点击卡片挂失
			clk_reportLost(v);
			break;
		case R.id.fragment_studentcard_queryElectric:// 点击电费查询
			clk_queryElectric(v);
			break;
		case R.id.fragment_studentcard_aboutUs:// 点击关于我们
			clk_about(v);
			break;
		case R.id.fragment_studentcard_exit:// 点击退出
			clk_exit(v);
			break;
		case R.id.fragment_studentcard_feedback:// 点击反馈
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
	 * 点击电费查询
	 * 
	 * @param v
	 */
	private void clk_queryElectric(View v) {
		startActivity(new Intent(getActivity(), QueryElectricActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击反馈
	 * 
	 * @param v
	 */
	private void clk_feedBack(View v) {
		startActivity(new Intent(getActivity(), FeedBackActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击关于我
	 * 
	 * @param v
	 */
	private void clk_about(View v) {
		startActivity(new Intent(getActivity(), AboutUsActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击卡片挂失
	 * 
	 * @param v
	 */
	private void clk_reportLost(View v) {
		startActivity(new Intent(getActivity(), ReportLostActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击消费明细
	 * 
	 * @param v
	 *            被点击对象
	 */
	private void clk_changePsw(View v) {
		startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击消费明细
	 * 
	 * @param v
	 *            被点击对象
	 */
	private void clk_transInfo(View v) {
		startActivity(new Intent(getActivity(), TransInfoActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击消费明细
	 * 
	 * @param v
	 *            被点击对象
	 */
	private void clk_costInfo(View v) {
		startActivity(new Intent(getActivity(), CostInfoActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击查看基本信息
	 * 
	 * @param v
	 *            被点击的对象
	 */
	private void clk_studentInfo(View v) {
		Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
		intent.putExtra(StudentInfoActivity.INTENT_EXTRA_KEY, student);
		getActivity().startActivity(intent);
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	/**
	 * 点击“退出”
	 * 
	 * @param v
	 */
	private void clk_exit(View v) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("提示").setMessage("\n您要退出软件吗？\n")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StudentcardFragment.this.getActivity().finish();
						// 0.5秒后退出系统
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								System.exit(0);
							}
						}, 500);
					}
				}).setNegativeButton("取消", null).create().show();
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
	 * 初始化监听器
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
	 * 从服务端获取学生和余额数据
	 */
	private void getDataFromServer() {
		getStudentNameFromServer();// 获取学生信息
		getChargeInfo();// 获取余额信息
	}

	/**
	 * 恢复之前的数据
	 */
	private void restoreSavedData() {
		if (student != null) {
			tv_name.setText(student.getName());
			tv_bal.setText(balInfo);
		}
	}

	/**
	 * 从服务器获取学生信息
	 */
	private void getStudentNameFromServer() {
		studentCardClient.getStudentInfo(new StudentInfoCallback() {
			@Override
			public void onCallback(Student student) {
				pullToRefreshScrollView.onRefreshComplete();
				if (student != null) {// 如果获取到了最新的Student信息
					StudentcardFragment.this.student = student;
					tv_name.setText(student.getName());
				} else {
					tv_name.setText("信息获取失败");
				}
			}
		});
	}

	/**
	 * 获取余额信息
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

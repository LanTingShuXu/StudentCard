package com.swun.hl.studentcard.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pulltorefresh.library.PullToRefreshBase;
import com.github.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.github.pulltorefresh.library.PullToRefreshListView;
import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bean.CostInfo;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.CostInfoQueryCallback;
import com.swun.hl.studentcard.ui.adapter.CostInfoListAdapter;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * 消费明细查询界面
 * 
 * @author 何玲
 * 
 */
public class CostInfoActivity extends Activity {

	private StudentCardClient cardClient = StudentCardClient
			.getInstance(getApplication());

	private Calendar calendar = Calendar.getInstance(Locale.getDefault());
	// 用户选择的开始时间的日历对象
	private Calendar startCalender = Calendar.getInstance(Locale.getDefault());
	// 用户选择的结束时间的日历对象
	private Calendar endCalender = Calendar.getInstance(Locale.getDefault());

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.getDefault());

	private TextView tv_startDate, tv_endDate;
	private ListView lst_content;
	private PullToRefreshListView pullToRefreshListView;// 下拉刷新组件
	private View v_progress;

	private CostInfoListAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cost_info);
		viewFind();
		initDate();
		initListView();
	}

	@Override
	public void onBackPressed() {
		this.finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	/**
	 * 点击选择开始时间
	 * 
	 * @param v
	 */
	public void clk_startDate(View v) {
		new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = Calendar.getInstance(Locale.getDefault());
				calendar.set(year, monthOfYear, dayOfMonth - 1);
				if (calendar.before(endCalender)) {
					startCalender.set(year, monthOfYear, dayOfMonth);
					tv_startDate.setText(simpleDateFormat.format(startCalender
							.getTime()));
				} else {
					AlertDialogHelper.showCommonDialog(CostInfoActivity.this,
							"开始时间不能在结束时间之后！");
				}
			}
		}, startCalender.get(Calendar.YEAR), startCalender.get(Calendar.MONTH),
				startCalender.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * 点击查询按钮监听事件
	 * 
	 * @param v
	 */
	public void clk_query(View v) {
		v_progress.setVisibility(View.VISIBLE);
		cardClient.costInfoQuery(tv_startDate.getText().toString(), tv_endDate
				.getText().toString(), new CostInfoQueryCallback() {
			@Override
			public void onCallback(boolean success, List<CostInfo> costInfos) {
				v_progress.setVisibility(View.GONE);
				if (success) {
					adapter = new CostInfoListAdapter(CostInfoActivity.this,
							costInfos);
					lst_content.setAdapter(adapter);
				} else {
					Toast.makeText(CostInfoActivity.this, "数据获取失败！请检查网络连接",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * 点击选择结束时间
	 * 
	 * @param v
	 */
	public void clk_endDate(View v) {
		new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = Calendar.getInstance(Locale.getDefault());
				calendar.set(year, monthOfYear, dayOfMonth);
				if (startCalender.before(calendar)) {
					endCalender.set(year, monthOfYear, dayOfMonth);
					tv_endDate.setText(simpleDateFormat.format(endCalender
							.getTime()));
				} else {
					AlertDialogHelper.showCommonDialog(CostInfoActivity.this,
							"结束时间不能在开始时间之前！");
				}
			}
		}, endCalender.get(Calendar.YEAR), endCalender.get(Calendar.MONTH),
				endCalender.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * 初始化下拉刷新ListView
	 */
	private void initListView() {
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						loadMoreCostInfo();
					}
				});
	}

	/**
	 * 获取下一页的消费记录
	 */
	private void loadMoreCostInfo() {
		cardClient.loadNextPageCostInfo(new CostInfoQueryCallback() {

			@Override
			public void onCallback(boolean success, List<CostInfo> costInfos) {
				if (success) {
					if (costInfos == null || costInfos.size() == 0) {
						Toast.makeText(CostInfoActivity.this, "暂无更多新数据",
								Toast.LENGTH_LONG).show();
					}
					if (adapter != null) {
						adapter.addData(costInfos);
						adapter.notifyDataSetChanged();
					} else {
						adapter = new CostInfoListAdapter(
								CostInfoActivity.this, costInfos);
						lst_content.setAdapter(adapter);
					}
				} else {
					Toast.makeText(CostInfoActivity.this, "数据获取失败！请检查网络连接",
							Toast.LENGTH_LONG).show();
				}

				pullToRefreshListView.onRefreshComplete();
			}
		});
	}

	private void viewFind() {
		tv_startDate = (TextView) findViewById(R.id.aty_costInfo_startDate);
		tv_endDate = (TextView) findViewById(R.id.aty_costInfo_endDate);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.aty_cost_info_list);
		lst_content = pullToRefreshListView.getRefreshableView();
		v_progress = findViewById(R.id.aty_costInfo_progress);
	}

	/**
	 * 初始化日期
	 */
	private void initDate() {
		tv_startDate.setText(simpleDateFormat.format(calendar.getTime()));
		tv_endDate.setText(simpleDateFormat.format(calendar.getTime()));
	}

}

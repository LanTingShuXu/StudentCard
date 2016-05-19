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
import com.swun.hl.studentcard.bean.TransInfo;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.TransInfoQueryCallback;
import com.swun.hl.studentcard.ui.adapter.TransInfoListAdapter;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * Ȧ����Ϣ��ѯ
 * 
 * @author LANTINGSHUXU
 * 
 */
public class TransInfoActivity extends Activity {

	private StudentCardClient cardClient = StudentCardClient
			.getInstance(getApplication());

	private Calendar calendar = Calendar.getInstance(Locale.getDefault());
	// �û�ѡ��Ŀ�ʼʱ�����������
	private Calendar startCalender = Calendar.getInstance(Locale.getDefault());
	// �û�ѡ��Ľ���ʱ�����������
	private Calendar endCalender = Calendar.getInstance(Locale.getDefault());

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.getDefault());

	private TextView tv_startDate, tv_endDate;
	private ListView lst_content;
	private TransInfoListAdapter adapter;
	private PullToRefreshListView pullToRefreshListView;// ����ˢ�����
	private View v_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trans_info);
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
	 * ���ѡ��ʼʱ��
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
					AlertDialogHelper.showCommonDialog(TransInfoActivity.this,
							"��ʼʱ�䲻���ڽ���ʱ��֮��");
				}
			}
		}, startCalender.get(Calendar.YEAR), startCalender.get(Calendar.MONTH),
				startCalender.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * �����ѯ��ť�����¼�
	 * 
	 * @param v
	 */
	public void clk_query(View v) {
		v_progress.setVisibility(View.VISIBLE);
		cardClient.getTransInfo(tv_startDate.getText().toString(), tv_endDate
				.getText().toString(), new TransInfoQueryCallback() {
			@Override
			public void onCallback(boolean success, List<TransInfo> transInfos) {
				v_progress.setVisibility(View.GONE);
				adapter = new TransInfoListAdapter(TransInfoActivity.this,
						transInfos);
				lst_content.setAdapter(adapter);
			}
		});
	}

	/**
	 * ���ѡ�����ʱ��
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
					AlertDialogHelper.showCommonDialog(TransInfoActivity.this,
							"����ʱ�䲻���ڿ�ʼʱ��֮ǰ��");
				}
			}
		}, endCalender.get(Calendar.YEAR), endCalender.get(Calendar.MONTH),
				endCalender.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * ��ʼ������ˢ��ListView
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
	 * ��ȡ��һҳ�����Ѽ�¼
	 */
	private void loadMoreCostInfo() {
		cardClient.loadNextPageTransInfo(new TransInfoQueryCallback() {
			@Override
			public void onCallback(boolean success, List<TransInfo> transInfos) {
				pullToRefreshListView.onRefreshComplete();
				if (success) {
					if (adapter != null) {
						adapter.addData(transInfos);
						adapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(TransInfoActivity.this, "����ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void viewFind() {
		tv_startDate = (TextView) findViewById(R.id.aty_transInfo_startDate);
		tv_endDate = (TextView) findViewById(R.id.aty_transInfo_endDate);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.aty_trans_info_list);
		lst_content = pullToRefreshListView.getRefreshableView();
		v_progress = findViewById(R.id.aty_transInfo_progress);
	}

	/**
	 * ��ʼ������
	 */
	private void initDate() {
		tv_startDate.setText(simpleDateFormat.format(calendar.getTime()));
		tv_endDate.setText(simpleDateFormat.format(calendar.getTime()));
	}
}

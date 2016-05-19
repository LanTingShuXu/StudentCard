package com.swun.hl.studentcard.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bean.TransInfo;

/**
 * 消费明细查询的适配器
 * 
 * @author LANTINGSHUXU
 * 
 */
public class TransInfoListAdapter extends BaseAdapter {

	private Context context;
	private List<TransInfo> infos;

	public TransInfoListAdapter(Context context, List<TransInfo> infos) {
		super();
		this.context = context;
		this.infos = infos;
	}

	/**
	 * 添加数据到尾部
	 * 
	 * @param transInfos
	 */
	public void addData(List<TransInfo> transInfos) {
		if (infos == null) {
			infos = transInfos;
		} else {
			infos.addAll(transInfos);
		}
	}

	/**
	 * 设置适配器的数据
	 * 
	 * @param tranInfos
	 */
	public void setData(List<TransInfo> tranInfos) {
		infos = tranInfos;
	}

	@Override
	public int getCount() {
		return infos == null ? 0 : infos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context).inflate(R.layout.item_trans_info,
					parent, false);
			viewFind(v, holder);
			v.setTag(holder);
		} else {
			v = convertView;
			holder = (ViewHolder) v.getTag();
		}
		valueSet(position, holder);
		return v;
	}

	private void viewFind(View v, ViewHolder holder) {
		holder.tv_accountName = (TextView) v
				.findViewById(R.id.item_transInfo_money);
		holder.tv_transMoney = (TextView) v
				.findViewById(R.id.item_transInfo_department);
		holder.tv_time = (TextView) v.findViewById(R.id.item_transInfo_time);
	}

	/**
	 * 设置填充值
	 * 
	 * @param position
	 * @param holder
	 */
	private void valueSet(int position, ViewHolder holder) {
		TransInfo info = infos.get(position);
		holder.tv_accountName.setText(info.getAccountName());
		holder.tv_transMoney.setText(info.getTransMoney());
		holder.tv_time.setText(info.getTransTime());
	}

	class ViewHolder {
		public TextView tv_accountName, tv_transMoney, tv_time;
	}

}

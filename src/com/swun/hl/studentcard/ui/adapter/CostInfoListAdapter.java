package com.swun.hl.studentcard.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bean.CostInfo;

/**
 * 消费明细查询的适配器
 * 
 * @author LANTINGSHUXU
 * 
 */
public class CostInfoListAdapter extends BaseAdapter {

	private Context context;
	private List<CostInfo> infos;

	public CostInfoListAdapter(Context context, List<CostInfo> infos) {
		super();
		this.context = context;
		this.infos = infos;
	}

	/**
	 * 添加数据到尾部
	 * 
	 * @param costInfos
	 */
	public void addData(List<CostInfo> costInfos) {
		if (infos == null) {
			infos = costInfos;
		} else {
			infos.addAll(costInfos);
		}
	}

	/**
	 * 设置适配器的数据
	 * 
	 * @param costInfos
	 */
	public void setData(List<CostInfo> costInfos) {
		infos = costInfos;
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
			v = LayoutInflater.from(context).inflate(R.layout.item_cost_info,
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
		holder.tv_money = (TextView) v.findViewById(R.id.item_costInfo_money);
		holder.tv_department = (TextView) v
				.findViewById(R.id.item_costInfo_department);
		holder.tv_time = (TextView) v.findViewById(R.id.item_costInfo_time);
	}

	/**
	 * 设置填充值
	 * 
	 * @param position
	 * @param holder
	 */
	private void valueSet(int position, ViewHolder holder) {
		CostInfo info = infos.get(position);
		holder.tv_money.setText(info.getCost());
		holder.tv_department.setText(info.getDepartment());
		holder.tv_time.setText(info.getTime());
	}

	class ViewHolder {
		public TextView tv_money, tv_department, tv_time;
	}

}

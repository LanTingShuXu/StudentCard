package com.swun.hl.studentcard.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.CommonCallback;
import com.swun.hl.studentcard.ui.ElectricChargeActivity;
import com.swun.hl.studentcard.ui.OtherChargeActivity;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * “缴费”Fragment
 * 
 * @author 何玲
 * 
 */
public class ChargeFragment extends Fragment implements View.OnClickListener {

	// 网络计费缴费，电控缴费，其他缴费
	private View v_netCharge, v_electryCharge, v_otherCharge;

	private TextView tv_netChargeMoney;

	private StudentCardClient client = StudentCardClient
			.getInstance(getActivity());

	private String netChargeToken = "";// 网络计费充值的token

	private Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View content = inflater.inflate(R.layout.fragment_charge, container,
				false);
		viewFind(content);
		initListener();
		getNetChargeMoney();
		return content;
	}

	@Override
	public void onStart() {
		getNetChargeMoney();
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_charge_electryCharge:// 电控缴费
			clk_electryCharge();
			break;
		case R.id.fragment_charge_netCharge:// 网络计费缴费
			clk_netCharge();
			break;
		case R.id.fragment_charge_otherCharge:// 其他缴费
			clk_otherCharge();
			break;
		}
	}

	// 电控缴费
	private void clk_electryCharge() {
		startActivity(new Intent(getActivity(), ElectricChargeActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	// 其他缴费
	private void clk_otherCharge() {
		startActivity(new Intent(getActivity(), OtherChargeActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	// 网络计费缴费
	private void clk_netCharge() {
		final EditText edt_money = getInputEdittext();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog dialog = builder.setTitle("网络计费充值")//
				.setView(edt_money)//
				.setNegativeButton("取消", null)//
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						commitNetCharge(netChargeToken, edt_money.getText()
								.toString());
					}
				}).create();
		dialog.show();
	}

	/**
	 * 提交充值信息
	 * 
	 * @param token
	 *            当前会话的token
	 * @param number
	 *            充值金额
	 */
	private void commitNetCharge(String token, String number) {
		final Toast t = Toast.makeText(getActivity(), "\n\t提交中...\t\n",
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
		client.userSrunPay(number, token, new CommonCallback() {
			@Override
			public void onCallback(boolean success, String info) {
				if (success) {
					getNetChargeMoney();
					t.cancel();
					AlertDialogHelper.showCommonDialog(
							ChargeFragment.this.getActivity(),
							"\n充值成功！网费及一卡通余额信息同步显示可能会有延迟,下次登录会生效\n");
				} else {
					AlertDialogHelper.showCommonDialog(
							ChargeFragment.this.getActivity(), info);
				}
			}
		});
	}

	/**
	 * 获得Edittext
	 * 
	 * @return
	 */
	private EditText getInputEdittext() {
		final EditText edt_money = new EditText(getActivity());
		edt_money.setInputType(InputType.TYPE_CLASS_NUMBER);
		edt_money.setSingleLine(true);
		edt_money.setHint("您要充值的金额");
		edt_money.setFocusable(true);
		edt_money.setFocusableInTouchMode(true);
		// 长度限制为4位内
		edt_money
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
		return edt_money;
	}

	// 初始化配置监听器
	private void initListener() {
		v_electryCharge.setOnClickListener(this);
		v_netCharge.setOnClickListener(this);
		v_otherCharge.setOnClickListener(this);
	}

	private void viewFind(View v) {
		v_electryCharge = v.findViewById(R.id.fragment_charge_electryCharge);
		v_netCharge = v.findViewById(R.id.fragment_charge_netCharge);
		v_otherCharge = v.findViewById(R.id.fragment_charge_otherCharge);
		tv_netChargeMoney = (TextView) v
				.findViewById(R.id.fragment_charge_money);
	}

	/**
	 * 获取网络计费余额
	 */
	private void getNetChargeMoney() {
		client.userSrunQuery(handler,
		// 网费余额信息回调
				new CommonCallback() {
					@Override
					public void onCallback(boolean success, String info) {
						tv_netChargeMoney.setText(info);
					}
				},
				// token信息回调
				new CommonCallback() {
					@Override
					public void onCallback(boolean success, String info) {
						if (success) {
							netChargeToken = info;
						}
					}
				});
	}
}

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
 * ���ɷѡ�Fragment
 * 
 * @author ����
 * 
 */
public class ChargeFragment extends Fragment implements View.OnClickListener {

	// ����Ʒѽɷѣ���ؽɷѣ������ɷ�
	private View v_netCharge, v_electryCharge, v_otherCharge;

	private TextView tv_netChargeMoney;

	private StudentCardClient client = StudentCardClient
			.getInstance(getActivity());

	private String netChargeToken = "";// ����Ʒѳ�ֵ��token

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
		case R.id.fragment_charge_electryCharge:// ��ؽɷ�
			clk_electryCharge();
			break;
		case R.id.fragment_charge_netCharge:// ����Ʒѽɷ�
			clk_netCharge();
			break;
		case R.id.fragment_charge_otherCharge:// �����ɷ�
			clk_otherCharge();
			break;
		}
	}

	// ��ؽɷ�
	private void clk_electryCharge() {
		startActivity(new Intent(getActivity(), ElectricChargeActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	// �����ɷ�
	private void clk_otherCharge() {
		startActivity(new Intent(getActivity(), OtherChargeActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(getActivity());
	}

	// ����Ʒѽɷ�
	private void clk_netCharge() {
		final EditText edt_money = getInputEdittext();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog dialog = builder.setTitle("����Ʒѳ�ֵ")//
				.setView(edt_money)//
				.setNegativeButton("ȡ��", null)//
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						commitNetCharge(netChargeToken, edt_money.getText()
								.toString());
					}
				}).create();
		dialog.show();
	}

	/**
	 * �ύ��ֵ��Ϣ
	 * 
	 * @param token
	 *            ��ǰ�Ự��token
	 * @param number
	 *            ��ֵ���
	 */
	private void commitNetCharge(String token, String number) {
		final Toast t = Toast.makeText(getActivity(), "\n\t�ύ��...\t\n",
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
							"\n��ֵ�ɹ������Ѽ�һ��ͨ�����Ϣͬ����ʾ���ܻ����ӳ�,�´ε�¼����Ч\n");
				} else {
					AlertDialogHelper.showCommonDialog(
							ChargeFragment.this.getActivity(), info);
				}
			}
		});
	}

	/**
	 * ���Edittext
	 * 
	 * @return
	 */
	private EditText getInputEdittext() {
		final EditText edt_money = new EditText(getActivity());
		edt_money.setInputType(InputType.TYPE_CLASS_NUMBER);
		edt_money.setSingleLine(true);
		edt_money.setHint("��Ҫ��ֵ�Ľ��");
		edt_money.setFocusable(true);
		edt_money.setFocusableInTouchMode(true);
		// ��������Ϊ4λ��
		edt_money
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
		return edt_money;
	}

	// ��ʼ�����ü�����
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
	 * ��ȡ����Ʒ����
	 */
	private void getNetChargeMoney() {
		client.userSrunQuery(handler,
		// ���������Ϣ�ص�
				new CommonCallback() {
					@Override
					public void onCallback(boolean success, String info) {
						tv_netChargeMoney.setText(info);
					}
				},
				// token��Ϣ�ص�
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

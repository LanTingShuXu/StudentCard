package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bean.Student;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;

/**
 * ѧ����Ϣ����
 * 
 * @author LANTINGSHUXU
 * 
 */
public class StudentInfoActivity extends Activity {

	/**
	 * Intent�д��ݵ�Student�����key
	 */
	public static final String INTENT_EXTRA_KEY = "student";
	private Student student;
	private StudentCardClient client = StudentCardClient
			.getInstance(getApplication());

	private ImageView img_photo;
	private TextView tv_name, tv_sex, tv_status, tv_createDate, tv_country,
			tv_bank, tv_position, tv_adress, tv_studentID, tv_department,
			tv_IDcard, tv_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info);
		getDataFromIntent();
		viewFind();
		loadPhoto();
		showStudentInfo();
	}

	@Override
	public void onBackPressed() {
		finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	private void viewFind() {
		img_photo = (ImageView) findViewById(R.id.aty_studentInfo_photo);
		tv_adress = (TextView) findViewById(R.id.aty_studentInfo_address);
		tv_bank = (TextView) findViewById(R.id.aty_studentInfo_bankCard);
		tv_country = (TextView) findViewById(R.id.aty_studentInfo_country);
		tv_createDate = (TextView) findViewById(R.id.aty_studentInfo_createDate);
		tv_department = (TextView) findViewById(R.id.aty_studentInfo_department);
		tv_IDcard = (TextView) findViewById(R.id.aty_studentInfo_IDCard);
		tv_money = (TextView) findViewById(R.id.aty_studentInfo_money);
		tv_name = (TextView) findViewById(R.id.aty_studentInfo_name);
		tv_position = (TextView) findViewById(R.id.aty_studentInfo_position);
		tv_sex = (TextView) findViewById(R.id.aty_studentInfo_sex);
		tv_status = (TextView) findViewById(R.id.aty_studentInfo_status);
		tv_studentID = (TextView) findViewById(R.id.aty_studentInfo_cardNumber);
	}

	/**
	 * ��Intent�л�ȡStudent����
	 */
	private void getDataFromIntent() {
		Object o = getIntent().getSerializableExtra(INTENT_EXTRA_KEY);
		if (o != null) {
			student = (Student) o;
		}
	}

	/**
	 * ������Ƭ
	 */
	private void loadPhoto() {
		if (student != null) {
			client.loadPhoto(this, student.getSerialNumber(), img_photo);
		}
	}

	/**
	 * ����Ϣ��ʾ����
	 */
	private void showStudentInfo() {
		if (student == null) {
			return;
		}
		tv_adress.setText("��ַ��" + student.getAddress());
		tv_bank.setText("���п��ţ�" + student.getBankCard());
		tv_country.setText("���ң�" + student.getCountry());
		tv_createDate.setText("�������ڣ�" + student.getCreateDate());
		tv_department.setText("���ţ�" + student.getDepartment());
		tv_IDcard.setText("���֤��" + student.getIDCard());
		tv_money.setText("Ѻ��" + student.getCashPledge());
		tv_name.setText("������" + student.getName());
		tv_position.setText("��ݣ�" + student.getPosition());
		tv_sex.setText("�Ա�" + student.getSex());
		tv_status.setText("�˻�״̬��" + student.getStatus());
		tv_studentID.setText("��ţ�" + student.getSerialNumber());
	}

}

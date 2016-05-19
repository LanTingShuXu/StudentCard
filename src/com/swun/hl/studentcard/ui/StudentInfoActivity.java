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
 * 学生信息界面
 * 
 * @author LANTINGSHUXU
 * 
 */
public class StudentInfoActivity extends Activity {

	/**
	 * Intent中传递的Student对象的key
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
	 * 从Intent中获取Student对象
	 */
	private void getDataFromIntent() {
		Object o = getIntent().getSerializableExtra(INTENT_EXTRA_KEY);
		if (o != null) {
			student = (Student) o;
		}
	}

	/**
	 * 加载照片
	 */
	private void loadPhoto() {
		if (student != null) {
			client.loadPhoto(this, student.getSerialNumber(), img_photo);
		}
	}

	/**
	 * 将信息显示出来
	 */
	private void showStudentInfo() {
		if (student == null) {
			return;
		}
		tv_adress.setText("地址：" + student.getAddress());
		tv_bank.setText("银行卡号：" + student.getBankCard());
		tv_country.setText("国家：" + student.getCountry());
		tv_createDate.setText("创建日期：" + student.getCreateDate());
		tv_department.setText("部门：" + student.getDepartment());
		tv_IDcard.setText("身份证：" + student.getIDCard());
		tv_money.setText("押金：" + student.getCashPledge());
		tv_name.setText("姓名：" + student.getName());
		tv_position.setText("身份：" + student.getPosition());
		tv_sex.setText("性别：" + student.getSex());
		tv_status.setText("账户状态：" + student.getStatus());
		tv_studentID.setText("编号：" + student.getSerialNumber());
	}

}

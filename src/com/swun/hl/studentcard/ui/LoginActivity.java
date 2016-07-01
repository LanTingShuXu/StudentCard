package com.swun.hl.studentcard.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.update.BmobUpdateAgent;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.bmobBean.BmobAccount;
import com.swun.hl.studentcard.bmobBean.BmobDeclare;
import com.swun.hl.studentcard.bmobBean.BmobLoginRecord;
import com.swun.hl.studentcard.bmobBean.BmobSystemStatus;
import com.swun.hl.studentcard.cache.SharedPreferenceHelper;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.client.StudentCardClient.CommonCallback;
import com.swun.hl.studentcard.myview.CustomTopBar;
import com.swun.hl.studentcard.myview.CustomTopBar.callBackListener;
import com.swun.hl.studentcard.service.BackService;
import com.swun.hl.studentcard.utils.AlertDialogHelper;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;
import com.swun.hl.studentcard.utils.PhoneEnvironment;

/**
 * 一卡通登录界面。此界面还有：网络判断，输入合法性判断，等常规判断未完成。-by：何玲，2016.02.26 <br />
 * 完成基本合法性输入。-by：何玲2016.02.27
 * 
 * @author 何玲
 * 
 */
public class LoginActivity extends Activity {

	private CustomTopBar topBar;
	private ImageView img_checkCode;// 验证码图片
	private EditText edt_checkCode;// 输入验证码的输入框
	private EditText edt_account;// 账号输入框
	private EditText edt_password;// 密码输入框
	private StudentCardClient studentCardClient;// 学生一卡通客户端类
	private Handler handler = new Handler();

	// “申明”对象
	private BmobDeclare declare;
	// true表示“申明”界面正显示
	private boolean declareViewShowing = false;

	// 集中管理SharedPreference的帮助类
	private SharedPreferenceHelper preferenceHelper;

	public static String PASSWORD = "";// 登录记录下密码。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (checkStatus()) {// 如果没有登录过。
			viewFind();
			init();
		}
	}

	@Override
	public void onBackPressed() {
		// 如果正在显示申明界面，我们不响应返回事件
		if (declareViewShowing) {
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示").setMessage("\n您要退出软件吗？\n")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginActivity.this.finish();
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

	/**
	 * 点击查询电费按钮监听器
	 * 
	 * @param v
	 */
	public void clk_queryElectric(View v) {
		startActivity(new Intent(this, QueryElectricActivity.class));
		Anim_BetweenActivity.leftOut_rightIn(this);
	}

	/**
	 * 点击“申明”的关闭按钮
	 * 
	 * @param v
	 */
	public void clk_layoutDeclareClose(View v) {
		if (declare != null) {
			preferenceHelper.setDeclareVersionCode(declare.getVersionCode());
		}
		ViewGroup parent = (ViewGroup) getWindow().getDecorView();
		parent.removeViewAt(1);
		declareViewShowing = false;
	}

	/**
	 * 登录按钮的点击事件
	 * 
	 * @param v
	 *            按钮对象
	 */
	public void clk_login(View v) {
		if (checkInput()) {// 检验输入是否合法
			Button btn = (Button) v;
			btn.setText("登录中...");
			btn.setEnabled(false);
			checkSystemStatus(btn);// 检查系统状态
		}
	}

	private void viewFind() {
		img_checkCode = (ImageView) findViewById(R.id.aty_login_img_checkCode);
		edt_checkCode = (EditText) findViewById(R.id.aty_login_edt_checkCode);
		edt_account = (EditText) findViewById(R.id.aty_login_account);
		edt_password = (EditText) findViewById(R.id.aty_login_password);
		topBar = (CustomTopBar) findViewById(R.id.aty_login_topBar);
	}

	/**
	 * 查询服务端是否有申明信息
	 */
	private void checkDeclare() {
		BmobQuery<BmobDeclare> query = new BmobQuery<BmobDeclare>();
		query.addWhereGreaterThanOrEqualTo("versionCode", 0);
		query.order("-updatedAt");// 按照时间降序
		query.findObjects(this, new FindListener<BmobDeclare>() {
			@Override
			public void onSuccess(List<BmobDeclare> bmobDeclares) {

				if (bmobDeclares != null && bmobDeclares.size() > 0) {
					declare = bmobDeclares.get(0);
					if (preferenceHelper.getDeclareVersionCode() != declare
							.getVersionCode()) {
						declareViewShowing = true;
						showDeclareView();
					}
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	/**
	 * 显示申明界面
	 */
	private void showDeclareView() {
		ViewGroup parent = (ViewGroup) getWindow().getDecorView();
		View v = LayoutInflater.from(this).inflate(
				R.layout.layout_login_declare, parent, false);
		TextView tv_declare = (TextView) v
				.findViewById(R.id.layout_login_declare_content);
		if (declare != null) {
			// 设置申明
			tv_declare.setText(Html.fromHtml(declare.getContent()));
		} else {
			declareViewShowing = false;
		}
		parent.addView(v, 1);
	}

	/**
	 * 判断之前是否登录过（通过Service中的Fragment是否为空可以判断出来），如果登录过，我们直接打开home界面
	 * 
	 * @return true 表示没有登录过
	 */
	private boolean checkStatus() {
		// 如果Service中已经有过Fragment，表示登录过，直接跳转到home界面
		if (BackService.contains != null) {
			startActivity(new Intent(this, HomeActivity.class));
			finish();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 初始化操作
	 */
	private void init() {
		initListener();
		BmobUpdateAgent.update(this);// 检查更新
		preferenceHelper = SharedPreferenceHelper.getInstance(this);
		studentCardClient = StudentCardClient.getInstance(this);
		edt_account.setText(preferenceHelper.getRecenetAccountName());
		studentCardClient.getCheckCodeImage(handler, img_checkCode);
		checkDeclare();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		topBar.setCallBackListener(new callBackListener() {

			@Override
			public void rightClick(View view) {
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,
						FeedBackActivity.class));
				Anim_BetweenActivity.leftOut_rightIn(LoginActivity.this);
			}

			@Override
			public void leftClick(View view) {

			}
		});
	}

	/**
	 * 判断输入是否合法
	 * 
	 * @return true表示合法。
	 */
	private boolean checkInput() {
		if ("".equals(edt_account.getText().toString().trim())) {// 如果输入为空
			AlertDialogHelper.showCommonDialog(this, "请输入您的学号");
			return false;
		} else if ("".equals(edt_password.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(this, "请输入您的密码");
			return false;
		} else if ("".equals(edt_checkCode.getText().toString().trim())) {
			AlertDialogHelper.showCommonDialog(this, "请输入验证码");
			return false;
		} else if (edt_checkCode.getText().toString().trim().length() != 4) {
			AlertDialogHelper.showCommonDialog(this, "验证码长度不正确！");
			return false;
		}
		return true;
	}

	/**
	 * 判断用户状态
	 * 
	 * @param btn
	 *            登录按钮
	 */
	private void checkUserStatus(final Button btn) {
		String username = edt_account.getText().toString().trim();
		BmobQuery<BmobAccount> query = new BmobQuery<BmobAccount>();
		query.addWhereEqualTo("username", username);
		query.findObjects(this, new FindListener<BmobAccount>() {

			@Override
			public void onError(int arg0, String arg1) {
				playLogin(btn);
			}

			@Override
			public void onSuccess(List<BmobAccount> data) {
				if (data.size() > 0) {
					if (data.get(0).getStatus() >= 0) {
						playLogin(btn);
					} else {
						showLoginError(btn, null);
					}
				} else {
					playLogin(btn);
				}
			}
		});
	}

	/**
	 * 判断系统状态
	 * 
	 * @param btn
	 */
	private void checkSystemStatus(final Button btn) {
		BmobQuery<BmobSystemStatus> query = new BmobQuery<BmobSystemStatus>();
		query.order("-updatedAt");
		query.findObjects(this, new FindListener<BmobSystemStatus>() {

			@Override
			public void onError(int arg0, String arg1) {
				checkUserStatus(btn);
			}

			@Override
			public void onSuccess(List<BmobSystemStatus> data) {
				// 如果有data数据
				if (data.size() > 0) {
					BmobSystemStatus status = data.get(0);
					if (status.getStatus() >= 0) {
						checkUserStatus(btn);
					} else {
						// 如果系统设置为异常状态，那么提示信息
						showLoginError(btn, status.getInfo());
					}
				} else {
					checkUserStatus(btn);
				}
			}
		});
	}

	/**
	 * 发起登录请求
	 */
	private void loginRequest(final Button btn) {
		final String recentName = edt_account.getText().toString().trim();
		studentCardClient.loginRequest(//
				recentName,//
				edt_password.getText().toString().trim(),//
				edt_checkCode.getText().toString().trim(),//
				new CommonCallback() {// 回调函数
					@Override
					public void onCallback(boolean success, String info) {
						if (success) {// 如果登录成功,跳转到主界面
							preferenceHelper.setRecentAccountName(recentName);
							LoginActivity.this.startActivity(new Intent(
									LoginActivity.this, HomeActivity.class));
							PASSWORD = edt_password.getText().toString();
							checkBmobAccount(recentName);
							LoginActivity.this.finish();
							Anim_BetweenActivity
									.leftOut_rightIn(LoginActivity.this);
						} else {// 登录失败
							handler.post(new Runnable() {
								@Override
								public void run() {
									btn.setText("登录");
									btn.setEnabled(true);
								}
							});
							AlertDialogHelper.showCommonDialog(
									LoginActivity.this, info);
							// 重新获取数据
							studentCardClient.getCheckCodeImage(handler,
									img_checkCode);
						}
					}
				});
	}

	/**
	 * 判断Bmob账户
	 */
	private void checkBmobAccount(String username) {
		BmobAccount bmobUser = BmobUser.getCurrentUser(this, BmobAccount.class);
		// 之前登录过。不用再登录
		if (bmobUser != null && username.equals(bmobUser.getUsername())) {
			loginRecord(bmobUser);
		}// 需要重新登录
		else {
			registorUser(username);
		}
	}

	/**
	 * 注册Bmob<br/>
	 * 注意：我们这里 账户名和密码都设置为一样的。目的是防止泄露用户隐私数据。<br/>
	 * 【由于软件中有社区功能，我们仅仅获取账号信息来标识用户而已】 Bmob服务器主要是存储用户的账户信息，没有收集用户的任何隐私数据！
	 */
	private void registorUser(String username) {
		final BmobAccount user = new BmobAccount();
		user.setUsername(username);
		user.setPassword(username);
		user.signUp(this, new SaveListener() {
			@Override
			public void onSuccess() {
				loginBmob(user);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				loginBmob(user);
			}
		});
	}

	/**
	 * 登录Bmob
	 * 
	 * @param user
	 */
	private void loginBmob(final BmobAccount user) {
		user.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				loginRecord(user);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				loginRecord(user);
			}
		});
	}

	/**
	 * 记录登录记录
	 * 
	 * @param user
	 */
	private void loginRecord(BmobAccount user) {
		BmobLoginRecord record = new BmobLoginRecord();
		record.setAccount(user);
		record.setLoginTime(getLocaleTime());
		record.setPhoneFactory(PhoneEnvironment.getPhoneFactory());
		record.setPhoneFingerPrint(PhoneEnvironment.getFingerPrint());
		record.setPhoneType(PhoneEnvironment.getPhoneType());
		record.save(this);
	}

	// 获取当前时间
	private String getLocaleTime() {
		String time = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.getDefault());
		time = dateFormat.format(new Date());
		return time;
	}

	/**
	 * 执行登录操作
	 * 
	 * @param btn
	 *            登录按钮
	 */
	private void playLogin(Button btn) {
		loginRequest(btn);
	}

	/**
	 * 显示登录失败信息
	 * 
	 * @param btn
	 * @param content
	 *            提示信息，如果为空，默认提示：登录失败
	 */
	private void showLoginError(Button btn, String content) {
		String info = "登录失败";
		if (content != null) {
			info = content;
		}
		AlertDialogHelper.showCommonDialog(this, info);
		btn.setText("登录");
		btn.setEnabled(true);
	}

}

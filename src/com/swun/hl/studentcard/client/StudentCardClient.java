package com.swun.hl.studentcard.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.swun.hl.studentcard.MyApp;
import com.swun.hl.studentcard.bean.CostInfo;
import com.swun.hl.studentcard.bean.Student;
import com.swun.hl.studentcard.bean.TransInfo;
import com.swun.hl.studentcard.cache.CacheHelper;
import com.swun.hl.studentcard.ui.LoginActivity;

/**
 * 学生一卡通客户端类
 * 
 * @author 李长军
 * 
 */
@SuppressWarnings("deprecation")
public class StudentCardClient {

	/**
	 * 成功与否的回调接口
	 * 
	 * @author 何玲
	 * 
	 */
	public static interface CommonCallback {
		/**
		 * 状态回调函数
		 * 
		 * @param success
		 *            成功与否。true表示成功。
		 * @param info
		 *            成功或者失败的详细信息
		 */
		public void onCallback(boolean success, String info);
	}

	/**
	 * 获取学生信息的回调接口
	 * 
	 * @author 何玲
	 * 
	 */
	public static interface StudentInfoCallback {
		/**
		 * 获取student信息的回调。
		 * 
		 * @param student
		 *            如果获取失败，返回null
		 */
		public void onCallback(Student student);
	}

	/**
	 * 查询消费详情的回调接口
	 * 
	 * @author LANTINGSHUXU
	 * 
	 */
	public static interface CostInfoQueryCallback {

		/**
		 * 查询结果回调函数
		 * 
		 * @param success
		 *            true表示正常查询成功
		 * @param costInfos
		 *            查询的集合
		 */
		public void onCallback(boolean success, List<CostInfo> costInfos);
	}

	/**
	 * 查询圈存明细的回调接口
	 * 
	 * @author LANTINGSHUXU
	 * 
	 */
	public static interface TransInfoQueryCallback {
		/**
		 * 查询结果回调函数
		 * 
		 * @param success
		 * @param transInfos
		 */
		public void onCallback(boolean success, List<TransInfo> transInfos);
	}

	// ------------以下是网址常量----------
	/**
	 * 一卡通服务端IP地址
	 */
	public static final String SERVER_IP_ADDRESS = "https://218.194.85.250";

	/**
	 * 一卡通登录界面地址
	 */
	private static final String SERVER_LOGIN_PAGE = SERVER_IP_ADDRESS
			+ "/ExpressWeb/";

	/**
	 * 修改密码界面地址（用户获取token）
	 */
	private static final String SERVER_CHANGE_PASSWORD_PAGE = SERVER_LOGIN_PAGE
			+ "/jsp/user/password.jsp";

	/**
	 * 一卡通登录表单提交地址
	 */
	private static final String SERVER_LOGIN_FORM_POST = SERVER_LOGIN_PAGE
			+ "/login.action";

	/**
	 * 判断是否已经登录。如果已经登录将会返回true
	 */
	private static final String SERVER_IS_LOGIN = SERVER_LOGIN_PAGE
			+ "/isLogin.action";

	/**
	 * 一卡通验证码地址
	 */
	private static final String SERVER_LOGIN_CHECK_CODE = SERVER_LOGIN_PAGE
			+ "/servlet/checkcode";

	/**
	 * 获取用户基本信息地址
	 */
	private static final String SERVER_USER_INFO = SERVER_LOGIN_PAGE
			+ "/userInfo.action";

	/**
	 * 获取用户余额信息
	 */
	private static final String SERVER_USER_BAL = SERVER_LOGIN_PAGE
			+ "/userBal.action";

	/**
	 * 学生照片地址
	 */
	private static final String SERVER_PHOTO = SERVER_LOGIN_PAGE
			+ "/photo.action";

	/**
	 * 消费明细查询地址
	 */
	private static final String SERVER_COST_INFO = SERVER_LOGIN_PAGE
			+ "/cost.action";

	/**
	 * 获取消费明细的下一页请求地址
	 */
	private static final String SERVER_COST_INFO_NEXT = SERVER_LOGIN_PAGE
			+ "/paginationListAction.action?page=next";

	/**
	 * 网络计费缴费地址
	 */
	private static final String SERVER_NET_CHARGE = SERVER_LOGIN_PAGE
			+ "/userSrunQuery.action";

	/**
	 * 缴费表单提交地址（提交缴费信息）
	 */
	private static final String SERVER_NET_CHARGE_POST = SERVER_LOGIN_PAGE
			+ "/userSrunPay.action";

	/**
	 * 圈存明细查询地址
	 */
	private static final String SERVER_TRANS_INFO = SERVER_LOGIN_PAGE
			+ "/trans.action";

	/**
	 * 修改密码表单提交的地址
	 */
	private static final String SERVER_CHANGE_PSW_COMMIT = SERVER_LOGIN_PAGE
			+ "/password.action";

	/**
	 * 卡片挂失的网址
	 */
	public static final String SERVER_REPORT_LOST = SERVER_LOGIN_PAGE
			+ "/cardInit.action";

	/**
	 * “其他缴费”的网址
	 */
	public static final String SERVER_OTHER_CHARGE = SERVER_LOGIN_PAGE
			+ "/customPayInit.action";

	/**
	 * 电控缴费
	 */
	public static final String SERVER_ELECTRY_CHARGE = SERVER_LOGIN_PAGE
			+ "/powerPayInit.action";

	/**
	 * 查询电费地址
	 */
	public static final String SERVER_ELECTRY_QUERY = "http://218.194.85.70/ADK";

	// -------------以上是网址常量-------

	/**
	 * 标记是否已经登录。true表示登录了
	 */
	private static boolean IS_LOGIN = false;
	/**
	 * 模拟的浏览器客户端
	 */
	private static HttpClient httpClient = HttpClientHelper.getHttpClient();
	// 单例对象
	private static StudentCardClient client = null;
	private Context context;

	/**
	 * 存储当前会话的cookie值
	 */
	public static String str_cookie;
	/**
	 * 消息头 setcookie的值
	 */
	public static String str_setCookie;

	/**
	 * 获取一卡通客户端对象
	 * 
	 * @param context
	 *            上下文对象
	 * @return 客户端对象
	 */
	public static StudentCardClient getInstance(Context context) {
		if (client == null) {
			synchronized (context) {
				if (client == null) {
					client = new StudentCardClient(context);
				}
			}
		}
		return client;
	}

	/**
	 * 发起登录请求
	 * 
	 * @param studentCard
	 *            学生卡号
	 * @param password
	 *            密码
	 * @param checkCode
	 *            验证码
	 */
	public void loginRequest(final String studentCard, final String password,
			final String checkCode, final CommonCallback callback) {
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_LOGIN_FORM_POST);
					addHeaderInfos(post);// 添加常用的请求头信息

					// 添加请求参数
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", studentCard));
					params.add(new BasicNameValuePair("password", password));
					params.add(new BasicNameValuePair("checkCode", checkCode));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					// 发起请求
					HttpResponse response = httpClient.execute(post);
					// 获取登录结果信息
					String s = readServerData(response.getEntity().getContent());
					isLogin();// 判断是否已经登录

					Looper.prepare();
					if (callback != null) {
						String info = "";
						if (IS_LOGIN == false) {
							if (s.contains("验证码错误")) {
								info = "验证码错误!";
							} else {
								info = "账户与密码不匹配";
							}
						} else {
							info = "登录成功!";
						}
						callback.onCallback(IS_LOGIN, info);
					}
					Looper.loop();

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	/**
	 * 获取登录界面的验证码
	 * 
	 * @param imgview
	 *            需要显示的imageView对象（不返回bitmap是为了减少异步的操作）
	 */
	public void getCheckCodeImage(final Handler handler, final ImageView imgview) {
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_LOGIN_PAGE);// 请求登录界面，获取Cookie
					str_cookie = "";
					HttpResponse response = httpClient.execute(get);// 发起请求
					Header header = response.getFirstHeader("Set-Cookie");// 获取Cookie头信息
					str_setCookie = header.getValue();// 获取cookie的值
					// 提取真正的jsessionid号
					str_cookie = str_setCookie.substring(
							str_setCookie.indexOf("=") + 1,
							str_setCookie.indexOf(";"));
					// ----------获取当前会话的验证码图片---------
					get = new HttpGet(SERVER_LOGIN_CHECK_CODE);
					addHeaderInfos(get);// 填写消息头
					response = httpClient.execute(get);// 发起获取验证码请求
					// 下载验证码
					downloadCheckCodeImage(handler, imgview, response
							.getEntity().getContent());
					isLogin();// 判断是否已经登录

				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * 再次获取验证码图片（一般是验证码填写错误导致）
	 * 
	 * @param handler
	 *            主线程的Handler
	 * @param imgview
	 *            需要显示图片的ImageView
	 */
	public void reGetCheckCodeImage(final Handler handler,
			final ImageView imgview) {
		new Thread() {
			public void run() {
				try {
					// ----------获取当前会话的验证码图片---------
					HttpGet get = new HttpGet(SERVER_LOGIN_CHECK_CODE);
					addHeaderInfos(get);// 填写消息头
					HttpResponse response = httpClient.execute(get);// 发起获取验证码请求
					// 下载验证码
					downloadCheckCodeImage(handler, imgview, response
							.getEntity().getContent());
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * 获取学生的照片
	 * 
	 * @param studentID
	 *            学号
	 * @param imageview
	 *            需要显示的ImageView
	 * @param context
	 */
	public void loadPhoto(final Context context, final String studentID,
			final ImageView imageview) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_PHOTO);// 获取头像请求
					addHeaderInfos(get);// 填写消息头
					HttpResponse response = httpClient.execute(get);// 发起请求
					if (responseIsRight(response)) {
						downloadPhoto(handler, context, studentID, imageview,
								response.getEntity().getContent());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * 获取当前学员信息
	 */
	public void getStudentInfo(final StudentInfoCallback callback) {

		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_USER_INFO);
					addHeaderInfos(get);
					HttpResponse response = httpClient.execute(get);
					onGetStudentInfoCallback(handler, callback, response);
				} catch (Exception e) {
					e.printStackTrace();
					onGetStudentInfoCallback(handler, callback, null);
				}
			};
		}.start();

	}

	/**
	 * 获取余额信息
	 * 
	 * @param callback
	 *            回调接口。
	 */
	public void getChargeInfo(final CommonCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_USER_BAL);
					addHeaderInfos(get);
					HttpResponse response = httpClient.execute(get);
					onGetChargeInfoCallback(handler, callback, response);
				} catch (Exception e) {
					e.printStackTrace();
					onGetChargeInfoCallback(handler, callback, null);
				}
			};
		}.start();
	}

	/**
	 * 消费明细查询
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 */
	public void costInfoQuery(final String startDate, final String endDate,
			final CostInfoQueryCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_COST_INFO);
					addHeaderInfos(post);
					// 添加请求参数
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("startDate", startDate));
					params.add(new BasicNameValuePair("endDate", endDate));
					params.add(new BasicNameValuePair("提交2", "查 询"));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					HttpResponse response = httpClient.execute(post);
					onCostInfoQueryCallback(handler, response, callback);

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 获取下一页的消费记录
	 * 
	 * @param callback
	 */
	public void loadNextPageCostInfo(final CostInfoQueryCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_COST_INFO_NEXT);
					addHeaderInfos(get);
					HttpResponse response = httpClient.execute(get);
					onCostInfoQueryCallback(handler, response, callback);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 加载下一页的充值信息
	 * 
	 * @param callback
	 */
	public void loadNextPageTransInfo(final TransInfoQueryCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_COST_INFO_NEXT);
					addHeaderInfos(get);
					HttpResponse response = httpClient.execute(get);
					boolean success = false;
					List<TransInfo> transInfos = new ArrayList<TransInfo>();
					if (responseIsRight(response)) {
						success = true;
						transInfos = HtmlInfoParser
								.parseQueryTransInfo(readServerData(response
										.getEntity().getContent()));
					}
					final boolean success2 = success;
					final List<TransInfo> infos = transInfos;
					if (callback != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.onCallback(success2, infos);
							}
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 网络计费余额查询
	 * 
	 * @param moneyCallback
	 *            查询网费余额的回调
	 * @param tokenCallback
	 *            查询token的回调
	 */
	public void userSrunQuery(final Handler handler,
			final CommonCallback moneyCallback,
			final CommonCallback tokenCallback) {
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_NET_CHARGE);
					addHeaderInfos(get);
					HttpResponse response = httpClient.execute(get);
					String moneyInfo = "";
					String tokenInfo = "";
					boolean success = false;
					String str_response = "";// 服务端获取的返回数据
					if (responseIsRight(response)) {
						success = true;
						str_response = readServerData(response.getEntity()
								.getContent());
						moneyInfo = HtmlInfoParser
								.parseQueryNetCharge(str_response);
						tokenInfo = HtmlInfoParser
								.parseChangePswToken(str_response);
					}
					final String info2 = moneyInfo;
					final String token = tokenInfo;
					final boolean success2 = success;
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (moneyCallback != null) {
								moneyCallback.onCallback(success2, info2);
							}
							if (tokenCallback != null) {
								tokenCallback.onCallback(success2, token);
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 获取修改密码界面的token
	 * 
	 * @param callback
	 */
	public void getChangePasswordToken(final CommonCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_CHANGE_PASSWORD_PAGE);
					addHeaderInfos(get);
					HttpResponse response = httpClient.execute(get);
					boolean success = false;
					String info = "";
					if (responseIsRight(response)) {
						success = true;
						info = readServerData(response.getEntity().getContent());
						info = HtmlInfoParser.parseChangePswToken(info);
					}
					if (callback != null) {
						final boolean success2 = success;
						final String info2 = info;
						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.onCallback(success2, info2);
							}
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 查询圈存明细
	 * 
	 * @param startDate
	 * @param endDate
	 * @param callback
	 */
	public void getTransInfo(final String startDate, final String endDate,
			final TransInfoQueryCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_TRANS_INFO);
					addHeaderInfos(post);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("startDate", startDate));
					params.add(new BasicNameValuePair("endDate", endDate));
					params.add(new BasicNameValuePair("提交2", "查 询"));
					post.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse response = httpClient.execute(post);
					boolean success = false;
					List<TransInfo> infos = new ArrayList<TransInfo>();
					if (responseIsRight(response)) {
						success = true;
						infos = HtmlInfoParser
								.parseQueryTransInfo(readServerData(response
										.getEntity().getContent()));
					}
					final boolean success2 = success;
					final List<TransInfo> infos2 = infos;
					if (callback != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.onCallback(success2, infos2);
							}
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	/**
	 * 提交密码修改请求
	 * 
	 * @param newPsw
	 * @param token
	 *            当前会话的token
	 * @param callbak
	 */
	public void commitChangePassword(final String newPsw, final String token,
			final CommonCallback callbak) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_CHANGE_PSW_COMMIT);
					addHeaderInfos(post);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("oldPwd",
							LoginActivity.PASSWORD));
					params.add(new BasicNameValuePair("newPwd", newPsw));
					params.add(new BasicNameValuePair("rePwd", newPsw));
					params.add(new BasicNameValuePair("button", "确  定"));
					params.add(new BasicNameValuePair("struts.token.name",
							"token"));

					params.add(new BasicNameValuePair("token", token));
					post.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse response = httpClient.execute(post);
					boolean success = false;
					String info = "网络异常";
					if (responseIsRight(response)) {
						if (readServerData(response.getEntity().getContent())
								.contains("修改成功")) {
							success = true;
							info = "密码修改成功！";
						} else {
							info = "密码修改不成功,请检查您的输入是否合法";
						}
					}
					if (callbak != null) {
						final boolean s = success;
						final String str = info;
						handler.post(new Runnable() {
							@Override
							public void run() {
								callbak.onCallback(s, str);
							}
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 用户充值网费
	 * 
	 * @param number
	 *            充值金额
	 * @param token
	 *            会话token
	 * @param callback
	 *            回调函数
	 */
	public void userSrunPay(final String number, final String token,
			final CommonCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_NET_CHARGE_POST);
					addHeaderInfos(post);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("money", number));
					params.add(new BasicNameValuePair("struts.token.name",
							"token"));
					params.add(new BasicNameValuePair("token", token));
					post.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse response = httpClient.execute(post);
					boolean success = false;
					String info = "发送请求异常，请检查网络和您的输入！";
					if (responseIsRight(response)) {
						String str_response = readServerData(response
								.getEntity().getContent());
						if (str_response.contains("缴费成功")) {
							success = true;
							info = "充值成功！";
						} else {
							info = "充值过程中遇到了问题。请检查您的余额是否充足，或者选择稍后重试。";
						}
					}
					final boolean s = success;
					final String info2 = info;
					if (callback != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.onCallback(s, info2);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	/**
	 * 回调查询结果
	 * 
	 * @param handler
	 * @param response
	 */
	private void onCostInfoQueryCallback(final Handler handler,
			HttpResponse response, final CostInfoQueryCallback callback) {
		try {
			List<CostInfo> infos = new ArrayList<CostInfo>();
			boolean success = false;
			if (responseIsRight(response)) {
				success = true;
				infos = HtmlInfoParser.getCostInfo(readServerData(response
						.getEntity().getContent()));
			}
			final List<CostInfo> costInfos = infos;
			final boolean isSuccess = success;

			if (callback != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onCallback(isSuccess, costInfos);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将获取到的余额信息回调回去
	 */
	private void onGetChargeInfoCallback(Handler handler,
			final CommonCallback callback, final HttpResponse response) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					String info = "余额信息获取失败";// 默认为失败的info信息
					boolean success = false;// 默认是失败
					// 获取成功
					if (responseIsRight(response)) {
						success = true;
						info = HtmlInfoParser
								.getChargeInfo(readServerData(response
										.getEntity().getContent()));
					}
					if (callback != null) {
						callback.onCallback(success, info);
					}
				} catch (Exception e) {
					if (callback != null) {
						callback.onCallback(false, "余额信息获取失败");
					}
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 给获取的学生信息回调回去
	 * 
	 * @param handler
	 * @param callback
	 * @param response
	 * @param entity
	 */
	private void onGetStudentInfoCallback(Handler handler,
			final StudentInfoCallback callback, final HttpResponse response) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					HttpEntity entity = response.getEntity();
					Student student = null;
					// 返回了正常的数据
					if (responseIsRight(response)) {
						String data = readServerData(entity.getContent());
						student = HtmlInfoParser.parseToStudent(data);
					}
					if (callback != null) {
						callback.onCallback(student);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (callback != null) {
						callback.onCallback(null);
					}
				}

			}
		});
	}

	/**
	 * 判断Response是不是合法的数据
	 * 
	 * @param response
	 * @return
	 */
	private boolean responseIsRight(HttpResponse response) {
		if (response != null && response.getStatusLine().getStatusCode() == 200
				&& response.getEntity() != null) {
			return true;
		} else {
			return false;
		}
	}

	// 单例模式私有化构造函数
	private StudentCardClient(Context context) {
		this.context = context;
	}

	/**
	 * 添加通用的头信息
	 */
	private void addHeaderInfos(HttpUriRequest request) {
		// 填写消息头
		// request.addHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		// request.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		// request.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		// request.addHeader("Connection", "keep-alive");
		// request.addHeader("Upgrade-Insecure-Requests", "1");
		// request.addHeader("Host", "218.194.85.250");
		// request.addHeader("Referer", "https://218.194.85.250/ExpressWeb/");
		// request.addHeader(
		// "User-Agent",
		// "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
		request.addHeader("Cookie", "JSESSIONID=" + str_cookie);// 添加Cookie信息
	}

	/**
	 * 读取服务端返回的数据。
	 * 
	 * @param in
	 *            服务端数据的输入流
	 * @return 服务器的数据
	 */
	private String readServerData(InputStream in) {
		String data = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				data += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 判断是否已经登录.并没有开启单独的线程
	 */
	private boolean isLogin() {
		try {
			HttpPost post = new HttpPost(SERVER_IS_LOGIN);
			addHeaderInfos(post);

			HttpResponse response = httpClient.execute(post);

			String data = readServerData(response.getEntity().getContent());
			if (data != null && data.contains("true")) {// 如果登录了
				IS_LOGIN = true;
			} else {
				IS_LOGIN = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return IS_LOGIN;
	}

	/**
	 * 获取验证码图片
	 * 
	 * @param handler
	 *            主线程的handler
	 * @param imgview
	 *            imageview对象
	 * @param in
	 *            图片的服务端输入流对象
	 */
	private void downloadCheckCodeImage(final Handler handler,
			final ImageView imgview, InputStream in) {
		try {
			int length = -1;
			byte[] b = new byte[1024];
			final ByteArrayOutputStream bao = new ByteArrayOutputStream();
			while ((length = in.read(b)) != -1) {
				bao.write(b, 0, length);
			}
			// 显示图片
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (imgview != null) {
						imgview.setImageBitmap(BitmapFactory.decodeByteArray(
								bao.toByteArray(), 0, bao.size()));
					}
				}
			});
			bao.close();// 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 下载照片
	 * 
	 * @param context
	 * @param studentID
	 * @param imageView
	 * @param in
	 */
	private void downloadPhoto(Handler handler, final Context context,
			final String studentID, final ImageView imageView, InputStream in) {
		File file = new File(CacheHelper.getImageCacheDir(context) + "/"
				+ studentID);
		final File file2 = new File(CacheHelper.getImageCacheDir(context) + "/"
				+ studentID + ".png");
		File f = new File(CacheHelper.getImageCacheDir(context));
		try {
			f.mkdirs();
			FileOutputStream outputStream = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = in.read(b)) != -1) {
				outputStream.write(b, 0, len);
			}
			if (file2.exists()) {
				file2.delete();
			}
			file.renameTo(file2);
			handler.post(new Runnable() {
				@Override
				public void run() {
					String uri = "file://" + file2.getAbsolutePath();
					MyApp.imgLoader.displayImage(uri, imageView);
					MyApp.imgLoader.getDiskCache().remove(uri);
				}
			});
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			handler.post(new Runnable() {
				@Override
				public void run() {
					String uri = "file://" + file2.getAbsolutePath();
					MyApp.imgLoader.displayImage(uri, imageView);
					MyApp.imgLoader.getDiskCache().remove(uri);
				}
			});
			file.delete();
		}

	}
}

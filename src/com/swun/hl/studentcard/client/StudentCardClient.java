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
 * ѧ��һ��ͨ�ͻ�����
 * 
 * @author ���
 * 
 */
@SuppressWarnings("deprecation")
public class StudentCardClient {

	/**
	 * �ɹ����Ļص��ӿ�
	 * 
	 * @author ����
	 * 
	 */
	public static interface CommonCallback {
		/**
		 * ״̬�ص�����
		 * 
		 * @param success
		 *            �ɹ����true��ʾ�ɹ���
		 * @param info
		 *            �ɹ�����ʧ�ܵ���ϸ��Ϣ
		 */
		public void onCallback(boolean success, String info);
	}

	/**
	 * ��ȡѧ����Ϣ�Ļص��ӿ�
	 * 
	 * @author ����
	 * 
	 */
	public static interface StudentInfoCallback {
		/**
		 * ��ȡstudent��Ϣ�Ļص���
		 * 
		 * @param student
		 *            �����ȡʧ�ܣ�����null
		 */
		public void onCallback(Student student);
	}

	/**
	 * ��ѯ��������Ļص��ӿ�
	 * 
	 * @author LANTINGSHUXU
	 * 
	 */
	public static interface CostInfoQueryCallback {

		/**
		 * ��ѯ����ص�����
		 * 
		 * @param success
		 *            true��ʾ������ѯ�ɹ�
		 * @param costInfos
		 *            ��ѯ�ļ���
		 */
		public void onCallback(boolean success, List<CostInfo> costInfos);
	}

	/**
	 * ��ѯȦ����ϸ�Ļص��ӿ�
	 * 
	 * @author LANTINGSHUXU
	 * 
	 */
	public static interface TransInfoQueryCallback {
		/**
		 * ��ѯ����ص�����
		 * 
		 * @param success
		 * @param transInfos
		 */
		public void onCallback(boolean success, List<TransInfo> transInfos);
	}

	// ------------��������ַ����----------
	/**
	 * һ��ͨ�����IP��ַ
	 */
	public static final String SERVER_IP_ADDRESS = "https://218.194.85.250";

	/**
	 * һ��ͨ��¼�����ַ
	 */
	private static final String SERVER_LOGIN_PAGE = SERVER_IP_ADDRESS
			+ "/ExpressWeb/";

	/**
	 * �޸���������ַ���û���ȡtoken��
	 */
	private static final String SERVER_CHANGE_PASSWORD_PAGE = SERVER_LOGIN_PAGE
			+ "/jsp/user/password.jsp";

	/**
	 * һ��ͨ��¼���ύ��ַ
	 */
	private static final String SERVER_LOGIN_FORM_POST = SERVER_LOGIN_PAGE
			+ "/login.action";

	/**
	 * �ж��Ƿ��Ѿ���¼������Ѿ���¼���᷵��true
	 */
	private static final String SERVER_IS_LOGIN = SERVER_LOGIN_PAGE
			+ "/isLogin.action";

	/**
	 * һ��ͨ��֤���ַ
	 */
	private static final String SERVER_LOGIN_CHECK_CODE = SERVER_LOGIN_PAGE
			+ "/servlet/checkcode";

	/**
	 * ��ȡ�û�������Ϣ��ַ
	 */
	private static final String SERVER_USER_INFO = SERVER_LOGIN_PAGE
			+ "/userInfo.action";

	/**
	 * ��ȡ�û������Ϣ
	 */
	private static final String SERVER_USER_BAL = SERVER_LOGIN_PAGE
			+ "/userBal.action";

	/**
	 * ѧ����Ƭ��ַ
	 */
	private static final String SERVER_PHOTO = SERVER_LOGIN_PAGE
			+ "/photo.action";

	/**
	 * ������ϸ��ѯ��ַ
	 */
	private static final String SERVER_COST_INFO = SERVER_LOGIN_PAGE
			+ "/cost.action";

	/**
	 * ��ȡ������ϸ����һҳ�����ַ
	 */
	private static final String SERVER_COST_INFO_NEXT = SERVER_LOGIN_PAGE
			+ "/paginationListAction.action?page=next";

	/**
	 * ����Ʒѽɷѵ�ַ
	 */
	private static final String SERVER_NET_CHARGE = SERVER_LOGIN_PAGE
			+ "/userSrunQuery.action";

	/**
	 * �ɷѱ��ύ��ַ���ύ�ɷ���Ϣ��
	 */
	private static final String SERVER_NET_CHARGE_POST = SERVER_LOGIN_PAGE
			+ "/userSrunPay.action";

	/**
	 * Ȧ����ϸ��ѯ��ַ
	 */
	private static final String SERVER_TRANS_INFO = SERVER_LOGIN_PAGE
			+ "/trans.action";

	/**
	 * �޸�������ύ�ĵ�ַ
	 */
	private static final String SERVER_CHANGE_PSW_COMMIT = SERVER_LOGIN_PAGE
			+ "/password.action";

	/**
	 * ��Ƭ��ʧ����ַ
	 */
	public static final String SERVER_REPORT_LOST = SERVER_LOGIN_PAGE
			+ "/cardInit.action";

	/**
	 * �������ɷѡ�����ַ
	 */
	public static final String SERVER_OTHER_CHARGE = SERVER_LOGIN_PAGE
			+ "/customPayInit.action";

	/**
	 * ��ؽɷ�
	 */
	public static final String SERVER_ELECTRY_CHARGE = SERVER_LOGIN_PAGE
			+ "/powerPayInit.action";

	/**
	 * ��ѯ��ѵ�ַ
	 */
	public static final String SERVER_ELECTRY_QUERY = "http://218.194.85.70/ADK";

	// -------------��������ַ����-------

	/**
	 * ����Ƿ��Ѿ���¼��true��ʾ��¼��
	 */
	private static boolean IS_LOGIN = false;
	/**
	 * ģ���������ͻ���
	 */
	private static HttpClient httpClient = HttpClientHelper.getHttpClient();
	// ��������
	private static StudentCardClient client = null;
	private Context context;

	/**
	 * �洢��ǰ�Ự��cookieֵ
	 */
	public static String str_cookie;
	/**
	 * ��Ϣͷ setcookie��ֵ
	 */
	public static String str_setCookie;

	/**
	 * ��ȡһ��ͨ�ͻ��˶���
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return �ͻ��˶���
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
	 * �����¼����
	 * 
	 * @param studentCard
	 *            ѧ������
	 * @param password
	 *            ����
	 * @param checkCode
	 *            ��֤��
	 */
	public void loginRequest(final String studentCard, final String password,
			final String checkCode, final CommonCallback callback) {
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_LOGIN_FORM_POST);
					addHeaderInfos(post);// ��ӳ��õ�����ͷ��Ϣ

					// ����������
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("username", studentCard));
					params.add(new BasicNameValuePair("password", password));
					params.add(new BasicNameValuePair("checkCode", checkCode));
					post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

					// ��������
					HttpResponse response = httpClient.execute(post);
					// ��ȡ��¼�����Ϣ
					String s = readServerData(response.getEntity().getContent());
					isLogin();// �ж��Ƿ��Ѿ���¼

					Looper.prepare();
					if (callback != null) {
						String info = "";
						if (IS_LOGIN == false) {
							if (s.contains("��֤�����")) {
								info = "��֤�����!";
							} else {
								info = "�˻������벻ƥ��";
							}
						} else {
							info = "��¼�ɹ�!";
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
	 * ��ȡ��¼�������֤��
	 * 
	 * @param imgview
	 *            ��Ҫ��ʾ��imageView���󣨲�����bitmap��Ϊ�˼����첽�Ĳ�����
	 */
	public void getCheckCodeImage(final Handler handler, final ImageView imgview) {
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_LOGIN_PAGE);// �����¼���棬��ȡCookie
					str_cookie = "";
					HttpResponse response = httpClient.execute(get);// ��������
					Header header = response.getFirstHeader("Set-Cookie");// ��ȡCookieͷ��Ϣ
					str_setCookie = header.getValue();// ��ȡcookie��ֵ
					// ��ȡ������jsessionid��
					str_cookie = str_setCookie.substring(
							str_setCookie.indexOf("=") + 1,
							str_setCookie.indexOf(";"));
					// ----------��ȡ��ǰ�Ự����֤��ͼƬ---------
					get = new HttpGet(SERVER_LOGIN_CHECK_CODE);
					addHeaderInfos(get);// ��д��Ϣͷ
					response = httpClient.execute(get);// �����ȡ��֤������
					// ������֤��
					downloadCheckCodeImage(handler, imgview, response
							.getEntity().getContent());
					isLogin();// �ж��Ƿ��Ѿ���¼

				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * �ٴλ�ȡ��֤��ͼƬ��һ������֤����д�����£�
	 * 
	 * @param handler
	 *            ���̵߳�Handler
	 * @param imgview
	 *            ��Ҫ��ʾͼƬ��ImageView
	 */
	public void reGetCheckCodeImage(final Handler handler,
			final ImageView imgview) {
		new Thread() {
			public void run() {
				try {
					// ----------��ȡ��ǰ�Ự����֤��ͼƬ---------
					HttpGet get = new HttpGet(SERVER_LOGIN_CHECK_CODE);
					addHeaderInfos(get);// ��д��Ϣͷ
					HttpResponse response = httpClient.execute(get);// �����ȡ��֤������
					// ������֤��
					downloadCheckCodeImage(handler, imgview, response
							.getEntity().getContent());
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * ��ȡѧ������Ƭ
	 * 
	 * @param studentID
	 *            ѧ��
	 * @param imageview
	 *            ��Ҫ��ʾ��ImageView
	 * @param context
	 */
	public void loadPhoto(final Context context, final String studentID,
			final ImageView imageview) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpGet get = new HttpGet(SERVER_PHOTO);// ��ȡͷ������
					addHeaderInfos(get);// ��д��Ϣͷ
					HttpResponse response = httpClient.execute(get);// ��������
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
	 * ��ȡ��ǰѧԱ��Ϣ
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
	 * ��ȡ�����Ϣ
	 * 
	 * @param callback
	 *            �ص��ӿڡ�
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
	 * ������ϸ��ѯ
	 * 
	 * @param startDate
	 *            ��ʼʱ��
	 * @param endDate
	 *            ����ʱ��
	 */
	public void costInfoQuery(final String startDate, final String endDate,
			final CostInfoQueryCallback callback) {
		final Handler handler = new Handler(context.getMainLooper());
		new Thread() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SERVER_COST_INFO);
					addHeaderInfos(post);
					// ����������
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("startDate", startDate));
					params.add(new BasicNameValuePair("endDate", endDate));
					params.add(new BasicNameValuePair("�ύ2", "�� ѯ"));
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
	 * ��ȡ��һҳ�����Ѽ�¼
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
	 * ������һҳ�ĳ�ֵ��Ϣ
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
	 * ����Ʒ�����ѯ
	 * 
	 * @param moneyCallback
	 *            ��ѯ�������Ļص�
	 * @param tokenCallback
	 *            ��ѯtoken�Ļص�
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
					String str_response = "";// ����˻�ȡ�ķ�������
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
	 * ��ȡ�޸���������token
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
	 * ��ѯȦ����ϸ
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
					params.add(new BasicNameValuePair("�ύ2", "�� ѯ"));
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
	 * �ύ�����޸�����
	 * 
	 * @param newPsw
	 * @param token
	 *            ��ǰ�Ự��token
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
					params.add(new BasicNameValuePair("button", "ȷ  ��"));
					params.add(new BasicNameValuePair("struts.token.name",
							"token"));

					params.add(new BasicNameValuePair("token", token));
					post.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse response = httpClient.execute(post);
					boolean success = false;
					String info = "�����쳣";
					if (responseIsRight(response)) {
						if (readServerData(response.getEntity().getContent())
								.contains("�޸ĳɹ�")) {
							success = true;
							info = "�����޸ĳɹ���";
						} else {
							info = "�����޸Ĳ��ɹ�,�������������Ƿ�Ϸ�";
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
	 * �û���ֵ����
	 * 
	 * @param number
	 *            ��ֵ���
	 * @param token
	 *            �Ựtoken
	 * @param callback
	 *            �ص�����
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
					String info = "���������쳣������������������룡";
					if (responseIsRight(response)) {
						String str_response = readServerData(response
								.getEntity().getContent());
						if (str_response.contains("�ɷѳɹ�")) {
							success = true;
							info = "��ֵ�ɹ���";
						} else {
							info = "��ֵ���������������⡣������������Ƿ���㣬����ѡ���Ժ����ԡ�";
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
	 * �ص���ѯ���
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
	 * ����ȡ���������Ϣ�ص���ȥ
	 */
	private void onGetChargeInfoCallback(Handler handler,
			final CommonCallback callback, final HttpResponse response) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					String info = "�����Ϣ��ȡʧ��";// Ĭ��Ϊʧ�ܵ�info��Ϣ
					boolean success = false;// Ĭ����ʧ��
					// ��ȡ�ɹ�
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
						callback.onCallback(false, "�����Ϣ��ȡʧ��");
					}
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ����ȡ��ѧ����Ϣ�ص���ȥ
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
					// ����������������
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
	 * �ж�Response�ǲ��ǺϷ�������
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

	// ����ģʽ˽�л����캯��
	private StudentCardClient(Context context) {
		this.context = context;
	}

	/**
	 * ���ͨ�õ�ͷ��Ϣ
	 */
	private void addHeaderInfos(HttpUriRequest request) {
		// ��д��Ϣͷ
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
		request.addHeader("Cookie", "JSESSIONID=" + str_cookie);// ���Cookie��Ϣ
	}

	/**
	 * ��ȡ����˷��ص����ݡ�
	 * 
	 * @param in
	 *            ��������ݵ�������
	 * @return ������������
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
	 * �ж��Ƿ��Ѿ���¼.��û�п����������߳�
	 */
	private boolean isLogin() {
		try {
			HttpPost post = new HttpPost(SERVER_IS_LOGIN);
			addHeaderInfos(post);

			HttpResponse response = httpClient.execute(post);

			String data = readServerData(response.getEntity().getContent());
			if (data != null && data.contains("true")) {// �����¼��
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
	 * ��ȡ��֤��ͼƬ
	 * 
	 * @param handler
	 *            ���̵߳�handler
	 * @param imgview
	 *            imageview����
	 * @param in
	 *            ͼƬ�ķ��������������
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
			// ��ʾͼƬ
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (imgview != null) {
						imgview.setImageBitmap(BitmapFactory.decodeByteArray(
								bao.toByteArray(), 0, bao.size()));
					}
				}
			});
			bao.close();// �ر���
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ������Ƭ
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

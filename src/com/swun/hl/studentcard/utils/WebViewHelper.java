package com.swun.hl.studentcard.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.http.SslError;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 抽离的WebView的相同的部分
 * 
 * @author LANTINGSHUXU
 * 
 */
@SuppressWarnings("deprecation")
public class WebViewHelper {

	private Context context;
	private WebView webView;
	private ProgressBar progressBar;
	private View errorPage;

	public WebViewHelper(Context context, WebView webView,
			ProgressBar progressBar, View errorPage) {
		this.context = context;
		this.webView = webView;
		this.progressBar = progressBar;
		this.errorPage = errorPage;
	}

	/**
	 * 加载指定的url
	 * 
	 * @param url
	 *            url网址
	 * @param cookie
	 *            该网址的cookie信息。如果没有，可以传递""
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void loadData(String url, String cookie, String baseUrl,
			String setCookie) {
		WebSettings settings = webView.getSettings();

		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);

		webView.setWebViewClient(myWebViewClient);

		webView.setWebChromeClient(myChromeClient);

		// 同步cookie
		CookieSyncManager cookieSyncManager = CookieSyncManager
				.createInstance(context);
		cookieSyncManager.sync();
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.setCookie(url, "JSESSIONID=" + cookie);
		cookieManager.setCookie(baseUrl, setCookie);
		CookieSyncManager.getInstance().sync();

		webView.loadUrl(url);
	}

	private WebChromeClient myChromeClient = new WebChromeClient() {
		// Js弹出对话框
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			final JsResult result2 = result;

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("提示").setMessage(message)//
					.setPositiveButton("确认", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							result2.confirm();
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							result2.cancel();
						}
					});
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}
			if (progressBar != null) {
				progressBar.setProgress(newProgress);
			}
		}
	};

	private WebViewClient myWebViewClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();// 处理https
		}

		// 加载url失败
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			if (errorPage != null) {
				errorPage.setVisibility(View.VISIBLE);
			}
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (progressBar != null) {
				progressBar.setProgress(100);
				progressBar.setVisibility(View.GONE);
			}
		}

	};

}

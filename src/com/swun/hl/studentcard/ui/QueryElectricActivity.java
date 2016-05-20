package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;
import com.swun.hl.studentcard.utils.WebViewHelper;

/**
 * 电费查询界面
 * 
 * @author 何玲
 * 
 */
public class QueryElectricActivity extends Activity {

	// 网页浏览器
	private WebView webView;
	private ProgressBar progressBar;
	private View errorPage;
	private ViewGroup container;// 包裹WebView和ProgressBar的容器

	private WebViewHelper webViewHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_electric);
		viewFind();
		initWebView();
	}

	@Override
	public void onBackPressed() {
		finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		container.removeAllViews();
		webView.destroy();
	}

	private void initWebView() {
		webViewHelper = new WebViewHelper(this, webView, progressBar, errorPage);
		webViewHelper.loadData(StudentCardClient.SERVER_ELECTRY_QUERY,
				StudentCardClient.str_cookie,
				StudentCardClient.SERVER_IP_ADDRESS,
				StudentCardClient.str_setCookie);
	}

	private void viewFind() {
		webView = (WebView) findViewById(R.id.aty_queryElectric_webview);
		progressBar = (ProgressBar) findViewById(R.id.aty_queryElectric_progressBar);
		errorPage = findViewById(R.id.aty_queryElectric_errorPage);
		container = (ViewGroup) findViewById(R.id.aty_query_electric_container);
	}
}

package com.swun.hl.studentcard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.swun.hl.studentcard.R;
import com.swun.hl.studentcard.client.StudentCardClient;
import com.swun.hl.studentcard.utils.Anim_BetweenActivity;
import com.swun.hl.studentcard.utils.WebViewHelper;

/**
 * π“ ßΩÁ√Ê
 * 
 * @author LANTINGSHUXU
 * 
 */
public class ReportLostActivity extends Activity {
	// Õ¯“≥‰Ø¿¿∆˜
	private WebView webView;
	private ProgressBar progressBar;
	private View errorPage;

	private WebViewHelper webViewHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_lost);
		viewFind();
		initWebView();
	}

	@Override
	public void onBackPressed() {
		finish();
		Anim_BetweenActivity.leftIn_rightOut(this);
	}

	private void initWebView() {
		webViewHelper = new WebViewHelper(this, webView, progressBar, errorPage);
		webViewHelper.loadData(StudentCardClient.SERVER_REPORT_LOST,
				StudentCardClient.str_cookie,
				StudentCardClient.SERVER_IP_ADDRESS,
				StudentCardClient.str_setCookie);
	}

	private void viewFind() {
		webView = (WebView) findViewById(R.id.aty_report_lost_webview);
		progressBar = (ProgressBar) findViewById(R.id.aty_report_lost_progressBar);
		errorPage = findViewById(R.id.aty_report_lost_errorPage);
	}
}

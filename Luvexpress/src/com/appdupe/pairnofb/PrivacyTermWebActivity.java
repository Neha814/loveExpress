package com.appdupe.pairnofb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PrivacyTermWebActivity extends Activity {
	ImageView back_image;
	ProgressBar progressBar;
	WebView webview;
	RelativeLayout progress;
	ProgressDialog prDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		webview = (WebView) findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);
		back_image = (ImageView) findViewById(R.id.back_image);
		progress=(RelativeLayout)findViewById(R.id.progress);
		progressBar=(ProgressBar)findViewById(R.id.progressBar1);
//		progressBar.getIndeterminateDrawable().setColorFilter(
//				getResources().getColor(R.color.theme_color),
//				android.graphics.PorterDuff.Mode.SRC_IN);

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
//		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		webview.setWebViewClient(new WebViewClient() {

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (prDialog!=null && prDialog.isShowing()) {
					prDialog.dismiss();
				}
				prDialog = ProgressDialog.show(PrivacyTermWebActivity.this, null, "Please Wait...");
//				ColorDrawable cd= new ColorDrawable();
//				cd.setColorFilter(
//						getResources().getColor(R.color.theme_color),
//						android.graphics.PorterDuff.Mode.SRC_IN);
//				prDialog.setIndeterminateDrawable(cd);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (prDialog.isShowing()) {
					prDialog.dismiss();
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(PrivacyTermWebActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
			}
		});
//		webview.loadDataWithBaseURL(null, Constant.Privacy_URL, "text/html", "utf-8", null); 
		webview.loadUrl(getIntent().getStringExtra("URL"));

		back_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}
}
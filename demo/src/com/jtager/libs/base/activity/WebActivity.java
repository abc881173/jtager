package com.jtager.libs.base.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions.Callback;
import android.widget.RelativeLayout;

import com.hehp.app.R;
import com.jtager.libs.base.JActivity;
import com.jtager.libs.utils.JLogUtil;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public final class WebActivity extends JActivity {

	RelativeLayout vMainLayout;
	WebView vWeb;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_layout);
		initView();
		initWeb();
		initWebClient();
	}

	private void initView() {
		vWeb = (WebView) findViewById(R.id.activity_web_layout_webview);
		vMainLayout = (RelativeLayout) findViewById(R.id.activity_web_layout);
		// 适配部分手机虚拟按键问题..
		vMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						vWeb.layout(vMainLayout.getLeft(),
								vMainLayout.getTop(), vMainLayout.getRight(),
								vMainLayout.getBottom());
					}
				});
	}

	@SuppressWarnings("deprecation")
	private void initWeb() {
		vWeb.getSettings().setLoadsImagesAutomatically(true);// 0424_手机系统版本低时webview页面不加载图片
		// }
		WebSettings webSettings = vWeb.getSettings();

		// 0514_允许webView 可以远程调试start
		if (JLogUtil.debug
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		// 0528_设置加载h5页面可以支持缩放
		// webview.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
		// WebSettings settings = webview.getSettings();
		webSettings.setUseWideViewPort(true);// 设定支持viewport
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);// 设定支持缩放
		// 0528_设置加载h5页面可以支持缩放_end

		webSettings.setBlockNetworkImage(false);

		webSettings.setJavaScriptEnabled(true);
		// 可以读取文件缓存(manifest生效)
		webSettings.setAllowFileAccess(true);
		// 设置可以使用localStorage
		webSettings.setDomStorageEnabled(true);
		// 应用可以有数据库
		webSettings.setDatabaseEnabled(true);
		String dbPath = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(dbPath);
		// 设置定位的数据库路径
		webSettings.setGeolocationDatabasePath(dbPath);
		// // 启用地理定位
		// webSettings.setGeolocationEnabled(true);
		// 设置编码格式
		webSettings.setDefaultTextEncodingName("utf-8");
		// 设置了这个，页面中就不会出现两边白边了
		vWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// 支持跨域请求
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
			vWeb.getSettings().setAllowUniversalAccessFromFileURLs(true);
		// 应用不可以有缓存
		webSettings.setAppCacheEnabled(false);
		String appCaceDir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCachePath(appCaceDir);
		// 设置不读缓存
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	}

	private void initWebClient() {
		vWeb.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			@Deprecated
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				// TODO Auto-generated method stub
				return super.shouldInterceptRequest(view, url);
			}

		});

		vWeb.setWebChromeClient(new WebChromeClient() {
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});
	}
}

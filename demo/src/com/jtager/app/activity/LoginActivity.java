package com.jtager.app.activity;

import org.json.JSONObject;

import com.hehp.app.R;
import com.jtager.libs.utils.JImageUtil;
import com.jtager.libs.utils.JLogUtil;
import com.jtager.libs.utils.JImageUtil.JCompressTypes;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private static final String APPID = "1105391300";

	private EditText et1;
	private EditText et2;
	private Tencent mTencent;
	private IUiListener loginListener;
	private IUiListener userInfoListener;
	private String scope;

	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);

		setupViews();
//		initData();
	}

	@Override
	protected void onDestroy() {
		if (mTencent != null) {
			mTencent.logout(LoginActivity.this);
		}
		super.onDestroy();
	}

	private void setupViews() {
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String path = JImageUtil.compressPic(LoginActivity.this, "/mnt/sdcard/DCIM/Camera/test.jpg", JCompressTypes.STANDARD_720_1280);
				et1.setText(path);
			}
		});
		
		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JLogUtil.println("你点击了使用qq登录按钮");
				login();
			}
		});

		findViewById(R.id.button3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JLogUtil.println("开始获取用户信息");
				if (mTencent.getQQToken() == null) {
					JLogUtil.println("qqtoken == null");
				}
				userInfo = new UserInfo(LoginActivity.this, mTencent
						.getQQToken());
				userInfo.getUserInfo(userInfoListener);
			}
		});
	}

	private void initData() {
		mTencent = Tencent.createInstance(APPID, LoginActivity.this);
		// 要所有权限，不用再次申请增量权限，这里不要设置成get_user_info,add_t
		scope = "all";
		loginListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub

			}

			/**
			 * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
			 * "pf":"desktop_m_qq-10000144-android-2002-",
			 * "query_authority_cost":448, "authority_cost":-136792089,
			 * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
			 * "expires_in":7776000, "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
			 * "msg":"", "access_token":"A2455F491478233529D0106D2CE6EB45",
			 * "login_cost":499}
			 */
			@Override
			public void onComplete(Object value) {
				// TODO Auto-generated method stub

				JLogUtil.println("有数据返回..");
				if (value == null) {
					return;
				}

				try {
					JSONObject jo = (JSONObject) value;

					String msg = jo.getString("msg");

					JLogUtil.println("json=" + String.valueOf(jo));

					JLogUtil.println("msg=" + msg);
					if ("sucess".equals(msg)) {
						Toast.makeText(LoginActivity.this, "登录成功",
								Toast.LENGTH_LONG).show();

						String openID = jo.getString("openid");
						String accessToken = jo.getString("access_token");
						String expires = jo.getString("expires_in");
						mTencent.setOpenId(openID);
						mTencent.setAccessToken(accessToken, expires);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		};

		userInfoListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub

			}

			/**
			 * {"is_yellow_year_vip":"0","ret":0, "figureurl_qq_1":
			 * "http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40"
			 * , "figureurl_qq_2":
			 * "http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100"
			 * , "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
			 * "city":"黄冈","
			 * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758
			 * \/015A22DED93BD15E0E6B0DDB3E59DE2D\/50", "vip":"0","level":"0",
			 * "figureurl_2":
			 * "http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100"
			 * , "province":"湖北", "is_yellow_vip":"0","gender":"男", "figureurl":
			 * "http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"
			 * }
			 */
			@Override
			public void onComplete(Object arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					return;
				}
				try {
					JLogUtil.println("json=" + String.valueOf(arg0));
					JSONObject jo = (JSONObject) arg0;
					int ret = jo.getInt("ret");
					if (ret == 100030) {
						// 权限不够，需要增量授权
						Runnable r = new Runnable() {
							public void run() {
								mTencent.reAuth(LoginActivity.this, "all",
										new IUiListener() {

											@Override
											public void onError(UiError arg0) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onComplete(Object arg0) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onCancel() {
												// TODO Auto-generated method
												// stub

											}
										});
							}
						};

						LoginActivity.this.runOnUiThread(r);
					} else {
						String nickName = jo.getString("nickname");
						String gender = jo.getString("gender");

						Toast.makeText(LoginActivity.this, "你好，" + nickName,
								Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		};
	}

	private void login() {
		if (!mTencent.isSessionValid()) {
			mTencent.login(LoginActivity.this, scope, loginListener);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.RESULT_LOGIN) {
				Tencent.handleResultData(data, loginListener);
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}

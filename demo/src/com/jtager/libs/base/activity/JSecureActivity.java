package com.jtager.libs.base.activity;

import com.jtager.libs.base.JActivity;

import android.os.Bundle;
import android.view.WindowManager;

public class JSecureActivity extends JActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
	}
}

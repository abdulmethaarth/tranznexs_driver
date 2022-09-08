package com.webnexs.tranznexsdriver.pojoClasses;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Souvik Hazra on 26-02-2017.
 */

public class CustomFontFragmentActivity extends FragmentActivity {
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}

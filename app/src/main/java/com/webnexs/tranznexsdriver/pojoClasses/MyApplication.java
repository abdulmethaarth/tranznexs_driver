package com.webnexs.tranznexsdriver.pojoClasses;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.webnexs.tranznexsdriver.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/ubuntu.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}

	@Override
	public void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		try {
			MultiDex.install(this);
		} catch (RuntimeException multiDexException) {
			multiDexException.printStackTrace();
		}
	}
}

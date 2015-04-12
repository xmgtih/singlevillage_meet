package com.singlevillage.meet.util;

import android.content.Context;

public class GlobalVar {
	private static Context sAppContext;

	public static Context getAppContext() {
		return sAppContext;
	}

	public static void setAppContext(Context appContext) {
		sAppContext = appContext;
	}
	
}

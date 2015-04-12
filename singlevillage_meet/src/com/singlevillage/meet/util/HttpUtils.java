package com.singlevillage.meet.util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpUtils {
	public final static  String LOG_IN = "http://120.24.227.131/api/session";
	public final static  String REGISTER = "http://120.24.227.131/api/user";
	private static RequestQueue sQueue = Volley.newRequestQueue(GlobalVar.getAppContext());
	
	public static void sendStringRequest(StringRequest stringRequest)
	{
		sQueue.add(stringRequest);
	}
	public static void sendJsonRequest(JsonObjectRequest jsonRequest)
	{
		sQueue.add(jsonRequest);
	}

}


package com.singlevillage.meet.util;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
public class ErrorCodeHelper {
	
	public static int getErrCodeByThrowable(Throwable throwable) {
		int errCode = code.CODE_UNKNOW;
		if (throwable != null) {
			if (throwable instanceof ConnectTimeoutException) {
				errCode = code.CODE_CONNECTION_TIMEOUT;
			} else if (throwable instanceof SocketTimeoutException) {
				errCode = code.CODE_REQUEST_TIME_OUT;
			} else if (throwable instanceof UnknownHostException) {
				errCode = code.CODE_UNKNONW_HOST;
			} else if (throwable instanceof FileNotFoundException) {
				errCode = code.CODE_FILE_NOTFOUND_ERROR;
			} else if (throwable instanceof java.net.ConnectException) {
				if (throwable.toString().contains("Network is unreachable")) {
					errCode = code.CODE_UNKNONW_HOST;
				} else {
					errCode = code.CODE_CONNECTION_ERROR;
				}
			} else if (throwable instanceof HttpResponseException) {
				try {
					HttpResponseException exception = (HttpResponseException) throwable;
					errCode = getErrorCode(exception.getStatusCode());// 私有错误码的起点
				} catch (Throwable e) {
				}
			} else if (throwable instanceof CircularRedirectException) {
				errCode = code.CODE_CIRCULAR_REDIRECT;
			} else if (throwable instanceof ClientProtocolException) {
				errCode = code.CODE_CLIENT_PROTOCOL;
			}else if (throwable instanceof JSONException) {
				errCode = code.CODE_JSON_PARSER_ERROR;
			}else if(throwable instanceof NoHttpResponseException){
				errCode = code.CODE_NO_HTTP_RESPONSE;
			}
		}
		return errCode;
	}

	private static int getErrorCode(int statusCode) {
		int errorCode = 0;
		if (statusCode > 0) {
			errorCode = code.CODE_STANDARD - statusCode;
		} else {
			errorCode = code.CODE_STANDARD_REVERSE + statusCode;
		}
		return errorCode;
	}


	/**
	 * 根据错误吗判断错误是本地类型或者网络类型
	 * @param errorCode 错误码
	 * @return true：本地错误，false：网络错误（即CGI返回的错误码）。
	 */
	public static boolean isInternalError(int errorCode){
		//CODE_STANDARD为定义的所有本地错误的错误码
		if(code.CODE_STANDARD_MIN < errorCode && errorCode < code.CODE_STANDARD)
			return true;
		return false;
	}

	// 和后台约定，app自定义错误码范围为 -10000 到 -20000
	public static final class code {
		public final static int CODE_SUCCESS = 0;
		public final static int CODE_STANDARD = -10000;
		public final static int CODE_STANDARD_REVERSE = -11000;
		public final static int CODE_UNKNONW_HOST = -10001;
		public final static int CODE_CONNECTION_ERROR = -10002;
		public final static int CODE_CONNECTION_TIMEOUT = -10003;
		public final static int CODE_REQUEST_TIME_OUT = -10004;

		public final static int CODE_GENNERAL_IO_ERROR = -10005;
		public final static int CODE_URI_ERROR = -10006;
		public final static int CODE_ILLEGAL_ARGUMENT = -10007;

		public final static int CODE_UNCAUGHT_ERROR = -10008;
		public final static int CODE_FILE_NOTFOUND_ERROR = -10009;

		public final static int CODE_NETWORK_PROVIDER = -10010;
		// 网络无效状态--没有打开网络
		public final static int CODE_NETWORK_UNAVAILABLE = -10011;
		public final static int CODE_CLIENT_PROTOCOL = -10012;
		public final static int CODE_CIRCULAR_REDIRECT = -10013;
		public final static int CODE_NO_HTTP_RESPONSE = -10014;
		public final static int CODE_UNKNOW = -11100;
		// 不合法的返回数据， 指服务器虽然返回了数据，但该数据内容并不是正确的业务数据，只是指明了服务器或请求出现了何种异常
		public final static int CODE_ILLEGAL_RESPOND_DATA = -11101;
		public final static int CODE_JSON_PARSER_ERROR = -11102;
		
		//对于只限定只取本地数据的loader,如果本地数据不存在时返回的错误
		public final static int CODE_NO_CACHE_DATA     = - 11103;
		
		//错有定义的错误码不应该小于该值
		public final static int CODE_STANDARD_MIN = - 20000;
		
		public final static int CODE_USER_NO_INVITE = 105;//用户没有受到邀请
		public final static int CODE_USER_NO_EXISTS = 104;//用户不存在
	}

}

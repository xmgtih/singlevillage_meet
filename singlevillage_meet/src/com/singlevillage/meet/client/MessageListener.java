package com.singlevillage.meet.client;

import com.singlevillage.meet.common.tran.bean.TranObject;

/**
 * 消息监听接口
 * 
 * @author way
 * 
 */
public interface MessageListener {
	public void Message(TranObject msg);
}

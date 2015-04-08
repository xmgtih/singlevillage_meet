package com.singlevillage.meet.model;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


/**
 * 所有数据模型的基本类
 * 
 * @author andy
 * 
 */
abstract public class BaseModel
{
	protected ReferenceQueue<IModelListener> mListenerReferenceQueue = null;
	protected ConcurrentLinkedQueue<WeakReference<IModelListener>> mWeakListenerArrayList = null;
	protected Handler mUiHandler = new Handler(Looper.getMainLooper()); // ui线程
	
	public BaseModel()
	{
		mListenerReferenceQueue = new ReferenceQueue<IModelListener> ();
		mWeakListenerArrayList = new ConcurrentLinkedQueue<WeakReference<IModelListener>> ();
	}
	
	/**
	 * 注册监听
	 * 
	 * @param listener
	 */
	public void register(IModelListener listener) 
	{
		if (listener == null) 
		{
			return;
		}
		
		synchronized(this)
		{
			// 每次注册的时候清理已经被系统回收的对象
			Reference<? extends IModelListener> releaseListener = null;
			while ((releaseListener = mListenerReferenceQueue.poll()) != null) 
			{
				mWeakListenerArrayList.remove(releaseListener);
			}

			// 弱引用处理
			for (WeakReference<IModelListener> weakListener : mWeakListenerArrayList) 
			{
				IModelListener listenerItem = weakListener.get();
				if (listenerItem == listener) 
				{
					return;
				}
			}
			WeakReference<IModelListener> weakListener = new WeakReference<IModelListener>(listener, mListenerReferenceQueue);
			this.mWeakListenerArrayList.add(weakListener);
		}
	}
	
	/**
	 * 取消注册监听
	 * 
	 * @param listener
	 */
	public void unregister(IModelListener listener) 
	{
		if (listener == null) 
		{
			return;
		}

		synchronized(this)
		{
			// 弱引用处理
			for (WeakReference<IModelListener> weakListener : mWeakListenerArrayList) 
			{
				IModelListener listenerItem = weakListener.get();
				if (listenerItem == listener) 
				{
					mWeakListenerArrayList.remove(weakListener);
					return;
				}
			}
		}
	}
	
	protected void sendMessageToUI(final BaseModel model,final int errCode,int delayMillis)
	{
		synchronized(this)
		{
			mUiHandler.postDelayed(new Runnable()
			{

				@Override
				public void run() 
				{
					for (WeakReference<IModelListener> weakListener : mWeakListenerArrayList) 
					{
						IModelListener listenerItem = weakListener.get();
						Log.i("BaseModel", " sendMessage:"+listenerItem);
						if (listenerItem != null) 
						{
							listenerItem.onLoadFinish(model, errCode);
						}
					}
					
				}},delayMillis);
		}
	}
	
	//发消息给UI线程
	protected void sendMessageToUI(final BaseModel model,final int errCode)
	{
		this.sendMessageToUI(model, errCode, 0);
	}
	
	//此回调发生在UI线程中
	public static interface IModelListener 
	{
		/**
		 * 通知UI数据加载成功
		 * @param model TODO
		 * @param errCode
		 */
		public void onLoadFinish(BaseModel model,int errCode);
	}

}

package com.guobi.gfc.gbmiscutils.config;

import java.util.HashMap;
import java.util.HashSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public final class GBVersionChecker{
	private static final String TAG="GBVersionChecker";
	private final Context mContext;
	private final SharedPreferences mSharedPreferences;
	private final class VersionEntry{
		public int verCode;
		public String parentGroup;
	}
	private final HashMap<String,VersionEntry> mVerMap = new HashMap<String,VersionEntry>();
	private final HashMap<String,HashSet<String>> mGroupMap = new HashMap<String,HashSet<String>>();
	private final Object mLock = new Object();
	private boolean mEnableUpdate;
	private static GBVersionChecker _instance;
	public static final String ACTION_CLEAR_UPDATE_FLAG = TAG+".ACTION_CLEAR_UPDATE_FLAG";
	

	public static final GBVersionChecker createIntance(Context context){
		if(_instance==null){
			_instance = new GBVersionChecker(context.getApplicationContext());
		}
		return _instance;
	}
	
	public static final GBVersionChecker getInstance(){
		return _instance;
	}
	
	/**
	 * 
	 * @param context
	 */
	private GBVersionChecker(Context context){
		mContext=context;
		mSharedPreferences = context.getApplicationContext().getSharedPreferences(TAG,0);
	}
	
	/**
	 * 设置版本
	 * @param key
	 * @param value
	 */
	public final void setVersion(String key,int value){
		synchronized (mLock){
			VersionEntry entry = mVerMap.get(key);
			if( entry==null){
				entry = new VersionEntry();
				mVerMap.put(key,entry);
			}
			entry.verCode = value;
		}
	}
	
	/**
	 * 设置版本
	 * @param key
	 * @param parentGroup
	 * @param value
	 */
	public final void setVersion(String key,String parentGroup,int value){
		synchronized (mLock){
			VersionEntry entry = mVerMap.get(key);
			if( entry==null){
				entry = new VersionEntry();
				mVerMap.put(key,entry);
			}
			entry.verCode = value;
			entry.parentGroup=parentGroup;
						
			if( parentGroup!=null ){
				HashSet<String> childKeys = mGroupMap.get(parentGroup);
				if( childKeys==null ){
					childKeys=new HashSet<String>();
					mGroupMap.put(parentGroup, childKeys);
				}
				childKeys.add(key);
			}
		}
	}
	
	public final void setEnableUpdate(boolean on){
		synchronized (mLock){
			mEnableUpdate=on;
		}
	}
	
	/**
	 * 获取当前最新版本
	 * @param key
	 * @return
	 */
	public final int getVersion(String key){
		synchronized (mLock){
			final VersionEntry entry = mVerMap.get(key);
			if( entry==null){
				return 0;
			}
			return entry.verCode;
		}
	}
	
	/**
	 * 获取旧版本
	 * @param key
	 * @return
	 */
	public final int getOldVersion(String key){
		synchronized (mLock){
			return mSharedPreferences.getInt(key,0);
		}
	}
	
	/**
	 * 检查KEY的版本是否是更新的
	 * @param key
	 * @return
	 */
	public final boolean checkUpdateFlag(String key){
		synchronized (mLock){
			if(mEnableUpdate==false){
				return false;
			}
			return getVersion(key)>getOldVersion(key);
		}
	}
	
	public final boolean checkGroupUpdateFlag(String group){
		synchronized (mLock){
			if(mEnableUpdate==false){
				return false;
			}
			final HashSet<String> childKeys = mGroupMap.get(group);
			if( childKeys!=null){
				for(String key:childKeys){
					if(checkUpdateFlag(key)){
						return true;
					}
				}
			}
			return false;
		}
	}
	
	
	/**
	 * 清除更新标志
	 * 调用此方法后，checkUpdateFlag就会返回FALSE
	 * @param context
	 */
	public final void clearUpdateFlag(String key){
		synchronized (mLock){
			final VersionEntry entry = mVerMap.get(key);
			if( entry==null){
				return;
			}
						
			mSharedPreferences
			.edit()
			.putInt(key,entry.verCode)
			.apply();
						
			Intent intent = new Intent(ACTION_CLEAR_UPDATE_FLAG);
			intent.putExtra("key",key);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 清除所有数据，恢复初始状态
	 */
	public final void clear(){
		synchronized (mLock){
			mVerMap.clear();
			mGroupMap.clear();
			mSharedPreferences
			.edit()
			.clear()
			.apply();
		}
	}
	
	public final void clearAllUpdateFlag(){
		synchronized (mLock){
			for(Object key:mVerMap.keySet()){
				final VersionEntry entry = mVerMap.get((String)key);
				if( entry!=null){
					mSharedPreferences
					.edit()
					.putInt((String)key,entry.verCode)
					.apply();
				}
			}
		}
	}
}

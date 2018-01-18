package com.example.localsearch;

import java.util.List;


/**
 * [pos,len]的含义:
 * -1,-1表示整个文本串都无需高亮
 * n,m表示从索引n处开始高亮，高亮长度为m
 * @author lujibeat
 *
 */
public final class WGResultText {

	private final String mContent;
	private final int mHightLightPos;
	private final int mHightLightLen;
	
	public WGResultText(String content,int hPos,int hLen){
		mContent=content;
		mHightLightPos=hPos;
		mHightLightLen=hLen;
	}
	

	public WGResultText(String content,int[] pos){
		this(content,pos!=null?pos[0]:-1,pos!=null?pos[1]:-1);
	}
	
	public WGResultText(String content){
		this(content,-1,-1);
	}
	
	public final String getContent(){
		return mContent;
	}
	
	public final int getHightLightPos(){
		return mHightLightPos;
	}
	
	public final int getHightLightLen(){
		return mHightLightLen;
	}
}

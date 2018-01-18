package com.example.localsearch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.localsearch.LocalAppFinder.AppResult;

import android.content.Context;

/**
 * LocalSearch(Context context, String key, String type)	构造方法，key为搜索关键字，type为执行类型，见StringMatcher类
 * Void init()	初始化搜索，清空结果集
 * List<AppResult>getAppResult()	返回一个App搜索结果的List。没有结果则返回null
 * List<ContactResult>getContactResult()	返回一个联系人搜索结果的List。没有结果则返回null
 *
 * @author lujibeat
 */
public class LocalSearch {

    private String key;
    private Context mContext;
    private List<AppResult> appResult;
    private List<WGContactResult> contactResult;
    private List<LocalSmsFinder.WGSmsResult> smsResult;
    private String type;
    private LocalContactFinder finder;
    private LocalAppFinder appfinder;
    private LocalSmsFinder smsFinder;

    public LocalSearch(final Context context, String type) {
        mContext = context;
        this.type = type;
        appResult = new ArrayList<>();
        contactResult = new ArrayList<>();
        smsResult = new ArrayList<>();
        appfinder = new LocalAppFinder(mContext, type);
        finder = new LocalContactFinder(mContext, type);
        smsFinder = new LocalSmsFinder(mContext, type);

    }
    public void clearList(){
        if (appResult!=null){
            appResult.clear();
        }
        if (contactResult!=null){
            contactResult.clear();
        }
        if (smsResult!=null){
            smsResult.clear();
        }
    }

    public void setKey(String key){
        this.key=key;
    }

    /**
     * @return app搜索结果
     */
    public List<AppResult> getAppResult() {
        if (appfinder == null) {
            appfinder = new LocalAppFinder(mContext, type);
            appfinder.setKey(key);
        } else {
            appfinder.setKey(key);
        }
        appResult = appfinder.appSearch();
        return appResult;

    }

    /**
     * @return 联系人搜索结果
     */
    public List<WGContactResult> getContactResult() {
        if (finder == null) {
            finder = new LocalContactFinder(mContext, type);
            finder.setRawKey(key);
        } else {
            finder.setRawKey(key);
        }
        contactResult = finder.contactSearch();
        return contactResult;

    }

    /**
     * @return 搜索结果
     */
    public List<LocalSmsFinder.WGSmsResult> getSmsResult() {
        if (smsFinder == null) {
            smsFinder = new LocalSmsFinder(mContext, type);
            smsFinder.setRawKey(key);
        } else {
            smsFinder.setRawKey(key);
        }
        smsResult = smsFinder.smsResults();
        return smsResult;
    }

}

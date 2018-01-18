package com.example.localsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.searchutils.HanziToPinyin;
import com.example.searchutils.StringMatcher;
import com.guobi.common.wordpy.Wordpy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
/**
 * 本地App搜索类
 * @param context 
 * @param key 搜索关键字
 * @param type 搜索执行类型，详见StringMatcher类
 * @author lujibeat
 * 
 */
public final class LocalAppFinder {
    private Context mContext;
    private String key = "";
    private String type;
    private static List<AppResult> allAppResults;
    /**
     * 
     * @param context
     * @param type
     */
    public LocalAppFinder(Context context, String type){
    	mContext = context;
    	this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void Init(Context mContext){

        if(allAppResults == null){
            allAppResults = InstalledAppResult(mContext);
        }

    }
    /**
     * 搜索执行方法
     * @return List<AppResult> 
     */
    public  List<AppResult> appSearch() {
        //获取全部查找结果
//    	long start = System.currentTimeMillis();
        if(allAppResults == null){
        	allAppResults = InstalledAppResult(mContext);
        }
        
//      long end = System.currentTimeMillis();
//		Log.d("App获取时间", String.valueOf(start - end));
		
        List<AppResult> appResults = new ArrayList<AppResult>();
        int searchType = StringMatcher.SEARCH_TYPE;
        String[] keyword = key.split("");
        for(String judge : keyword){
        	if(Wordpy.IsHanZi(judge)){
        		searchType = com.example.searchutils.StringMatcher.HANZI_TYPE;
        		break;
        	}
        }
        if(type != null && type.equals("voice")){
        	searchType = com.example.searchutils.StringMatcher.ALL_TYPE;
        }
//      long start1 = System.currentTimeMillis();
        for(AppResult result : allAppResults){
        	com.example.searchutils.StringMatcher matcher = new com.example.searchutils.StringMatcher(
        			key, result.getName().getContent(), searchType);
        	if(matcher.getResult() != null){
        		result.setName(matcher.getResult());
        		appResults.add(result);
        	}
        }
//      long end1 = System.currentTimeMillis();
//		Log.d("App搜索时间", String.valueOf(start1 - end1));
		
        if(appResults.isEmpty()) {
            return null;
        }
        return appResults;
    }
    
    /**
     * 在系统中查找APP的方法，返回所有可用app
     * @return List<AppResult>
     */
    
    public static List<AppResult> InstalledAppResult(Context mContext) {
        List<AppResult> result = new ArrayList<AppResult>();  
//       long start2 = System.currentTimeMillis();
        PackageManager pm = mContext.getPackageManager(); // 获得PackageManager对象   
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
        // 通过查询，获得所有ResolveInfo对象.   
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.GET_INTENT_FILTERS);
//        Log.d("APP个数", String.valueOf(resolveInfos.size()));
        // 调用系统排序 ， 根据name排序   
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序   
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
//        long end2 = System.currentTimeMillis();
//		Log.d("App获取系统时间", String.valueOf(start2 - end2));

        if (resolveInfos != null&&!resolveInfos.isEmpty()) {

            for(ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm);
                AppResult appResult = new AppResult(
                        activityName,
                        new WGResultText(appLabel),
                        pkgName,
                        icon
                );
                result.add(appResult);

            }
        }

        pm=null;
        return result;
    }
    /**
     * AppResult类型，getName获取APP名，
     * getPackageName获取安装包名，
     * getIcon获取版本名
     * @author lujibeat
     *
     */
    public static final class AppResult{
        
        private WGResultText appName; 
        private String packageName;
        private Drawable icon; 
        private String activityName;
        
        public AppResult(String activityName, WGResultText name, String packageName, Drawable icon){
        	this.appName = name;
        	this.packageName = packageName;
        	this.icon = icon;
        	this.activityName = activityName;
        }
        
        public String getActivityName(){
            return activityName;
        }

        public void setName(WGResultText name){
            this.appName = name;
        }

        public WGResultText getName() {
            return appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public Drawable getIcon() {
            return icon;
        }
    }

}  
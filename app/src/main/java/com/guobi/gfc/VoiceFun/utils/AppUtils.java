package com.guobi.gfc.VoiceFun.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.example.localsearch.WGResultText;
import com.guobi.gfc.VoiceFun.jcseg.ASegment;
import com.guobi.gfc.VoiceFun.jcseg.ComplexSeg;
import com.guobi.gfc.VoiceFun.jcseg.Dictionary;
import com.guobi.gfc.VoiceFun.jcseg.ResultWord;
import com.guobi.gfc.VoiceFun.jcseg.core.ILexicon;
import com.guobi.gfc.VoiceFun.phrase.SMSPhraseAnalyzer;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 2017/3/9.
 */

public class AppUtils {

    public static final int ANY = 0;
    public static final int LIKE = 1;
    public static final int FIRST = 2;
    public static final int FIRST_GROUP = 3;

    private static ASegment mSegment;
    private static Dictionary mDictionary;
    private static ComponentName mContactSyncService;

    public static final int getSuggestAppsBySpeech(Context context, String voice, List<String> result, int flag, float score) throws Exception {
        if (allAppResults == null || allAppResults.size()==0) {
            InstalledAppResult(context);
        }
        createSegment(context);
        GBLogUtils.DEBUG_DISPLAY("--getSuggestAppsBySpeech startContactSyncService", "+++++++++++++++++++++++");
        mDictionary.setScore(score);
        GBLogUtils.DEBUG_DISPLAY("--getSuggestAppsBySpeech mDictionary.setScore", "+++++++++++++++++++++++");
        mSegment.reset(new StringReader(voice));
        GBLogUtils.DEBUG_DISPLAY("--getSuggestAppsBySpeech mSegment.reset(new StringReader(voice));", "+++++++++++++++++++++++");

        result.clear();
        ResultWord word = null;
        int count = 0;
        int end = -1;
//			GBLogUtils.DEBUG_DISPLAY("--getSuggestContactsBySpeech ResultWord word = null;", "+++++++++++++++++++++++"+);
        while ((word = mSegment.next()) != null) {
            GBLogUtils.DEBUG_DISPLAY("--getSuggestAppsBySpeech while ((word = mSegment.next()) != null) ", "+++++++++++++++++++++++" + word.getValue());
            count++;
            if (word.getType() == ILexicon.VOICE_XING_MING) {
                String name = word.getValue();
                if (flag == FIRST_GROUP && result.size() >= 1) {
                    String name0 = voice.substring(word.getPosition(), word.getPosition() + word.getLength0());
                    if (PinyinUtils.xingming(name0, name) >= SMSPhraseAnalyzer.W_SCORE_2) {
                        result.add(word.getValue());
                    } else {
                        return end;
                    }
                } else {
                    result.add(word.getValue());
                }
                end = word.getPosition() + word.getLength0();

            } else if (word.getType() == ILexicon.CJK_WORDS) {

            } else {
                if (flag == FIRST_GROUP) {
                    if (count >= 2) {
                        return end;
                    }
                }
            }
            if (flag == FIRST) {
                if (result.size() >= 1 || count >= 2) {
                    return end;
                }
            }
        }

        if (flag == LIKE) {
            if (count - result.size() <= result.size() - 1) {
                return end;
            } else {
                result.clear();
                return -1;
            }
        } else {  // flag == ANY
            return end;
        }


    }

    public static void createSegment(Context context) throws Exception {

            if (mSegment == null) {
                Dictionary dic = mDictionary = new Dictionary();
                mSegment = new ComplexSeg(null, dic, false);


                try {
                    PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
                    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    // 通过查询，获得所有ResolveInfo对象.
                    List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.GET_INTENT_FILTERS);
                    Log.i("APP个数", String.valueOf(resolveInfos.size()));
                    // 调用系统排序 ， 根据name排序
                    // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
                    Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

                    for (ResolveInfo reInfo : resolveInfos) {
                        String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name

                      //  Log.i("chenqian", "activityName :" + activityName);
                        String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                       // Log.i("chenqian", "pkgNamepkgName  :" + pkgName);
                        String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                       // Log.i("chenqian", "appLabelappLabel  :" + appLabel);
                        Drawable icon = reInfo.loadIcon(pm);
                      //  Log.i("chenqian", "");

                        if (!TextUtils.isEmpty(appLabel) && appLabel.length() <= 20) {

                            Log.i("chenqian","createSegment: AppName---:"+appLabel);
                            dic.add(0, appLabel, ILexicon.VOICE_XING_MING);
                            //Log.i("chenqian", "app name:"+appLabel);
                        }
                    }


                    dic.add(0, "和", 10000, ILexicon.CJK_WORDS);
                    dic.add(0, "跟", 10000, ILexicon.CJK_WORDS);
                    dic.add(0, "与", 10000, ILexicon.CJK_WORDS);
                    dic.optimize();

                } catch (Exception e) {
                    e.printStackTrace();
                    GBLogUtils.DEBUG_DISPLAY("-- createSegment e", e.getMessage());
                } finally {

                }
            }


    }

    public static List<AppResult> findApp(Context context ,String appName) {

            List<AppResult> result = new ArrayList<>();
            try {
                if (allAppResults == null||allAppResults.size()==0) {
                    InstalledAppResult(context);
                }
                Log.i("chenqian","findApp .size :"+allAppResults.size());
                for (AppResult c: allAppResults) {
                  //  Log.i("chenqian","findApp: appName:"+c.appName.getContent());
                    String activityName = c.activityName;
                    String packageName = c.packageName;
                    String appNames = c.appName.getContent();
                    Drawable icon = c.icon;
                    if (appNames.equals(appName)) {
                        result.add(c);
                    }
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }

        return null;
    }

    public static List<AppResult> allAppResults = new ArrayList<>();

    private static List<AppResult> InstalledAppResult(Context context) {

        // long start2 = System.currentTimeMillis();
        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.GET_INTENT_FILTERS);
        Log.i("chenqian", "APP 个数 ："+String.valueOf(resolveInfos.size()));
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo reInfo : resolveInfos) {
            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name

           // Log.i("chenqian", "pkgNamepkgName :" + activityName);
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
           // Log.i("chenqian", "pkgNamepkgName  :" + pkgName);
            String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
           // Log.i("chenqian", "appLabelappLabel  :" + appLabel);
            Drawable icon = reInfo.loadIcon(pm);
           // Log.i("chenqian", "");
            AppResult appResult = new AppResult(activityName, new WGResultText(appLabel.trim()), pkgName, icon);
            allAppResults.add(appResult);

        }
        pm = null;

        Log.i("chenqian","allAppResult 后app 个数  :"+allAppResults.size());

        for (AppResult app: allAppResults) {
          //  Log.i("chenqian","allAppResult name"+app.appName.getContent());
        }
        return allAppResults;
    }

    public static class AppResult {

        public WGResultText appName;
        public String packageName;
        public Drawable icon;
        public String activityName;

        public AppResult(String activityName, WGResultText name, String packageName, Drawable icon) {
            this.appName = name;
            this.packageName = packageName;
            this.icon = icon;
            this.activityName = activityName;
        }


    }
}
package com.winguo.personalcenter.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winguo.R;
import com.winguo.base.BaseActivity;
import com.winguo.base.BaseTitleActivity;
import com.winguo.bean.HelpVoice;
import com.winguo.utils.CommonUtil;
import com.winguo.utils.Intents;
import com.winguo.view.ExpandCollapseLayoutListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助说明
 */

public class HelpVoiceActivity extends BaseTitleActivity  {

    private String titleTop;
//    private TextView title;
//    private LinearLayout topBack;
//    private ImageView feedBack;
    List<String> telData = new ArrayList<>();
    List<String> messData = new ArrayList<>();
    List<String> appData = new ArrayList<>();
    List<String> voiceToData = new ArrayList<>();
    List<String> serviceData = new ArrayList<>();
    private ExpandCollapseLayoutListView tel;
    private ExpandCollapseLayoutListView app;
    private ExpandCollapseLayoutListView voiceTo;
    private ExpandCollapseLayoutListView roundService;
    private ExpandCollapseLayoutListView mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_voice_layout);
        setBackBtn();
        initDatas();
        initViews();
    }

    private void initViews() {
//        topBack = (LinearLayout) findViewById(R.id.top_back_about);
//        title = (TextView) findViewById(R.id.top_layout_title_about);
//        feedBack = (ImageView) findViewById(R.id.top_user_feedback_about);
        LinearLayout llContainer = (LinearLayout) findViewById(R.id.helpVoice_container);
        llContainer.addView(tel);
        llContainer.addView(mess);
        llContainer.addView(app);
        llContainer.addView(voiceTo);
        llContainer.addView(roundService);
    }


    private void initDatas() {
//        CommonUtil.stateSetting(this,R.color.white_top_color);
        Intent intent = getIntent();
        titleTop = intent.getStringExtra("title");

        getTelResource();
        getAppResource();
        getMessResource();
        getVoiceToResource();
        getRoundServiceResource();

    }
    private  void getTelResource() {
        String[] res = getResources().getStringArray(R.array.help_voice_tel);
        for (int i = 0; i <res.length ; i++) {
            telData.add(res[i]);
        }
        tel = new ExpandCollapseLayoutListView(this,new HelpVoice(R.drawable.winguo_help_call,getString(R.string.help_voice_tel),res[0],telData));
    }

    private void getAppResource() {

        String[] res = getResources().getStringArray(R.array.help_voice_app);
        for (int i = 0; i <res.length ; i++) {
            appData.add(res[i]);
        }
        app = new ExpandCollapseLayoutListView(this,new HelpVoice(R.drawable.winguo_help_app,getString(R.string.help_voice_app),res[0],appData));
    }
    private void getMessResource() {
        String[] res = getResources().getStringArray(R.array.help_voice_message);
        for (int i = 0; i <res.length ; i++) {
            messData.add(res[i]);
        }
         mess =  new ExpandCollapseLayoutListView(this,new HelpVoice(R.drawable.winguo_help_sms,getString(R.string.help_voice_message),res[0],messData));
    }
    private void getVoiceToResource() {
        String[] res = getResources().getStringArray(R.array.help_voice_to);
        for (int i = 0; i <res.length ; i++) {
            voiceToData.add(res[i]);
        }
        voiceTo = new ExpandCollapseLayoutListView(this,new HelpVoice(R.drawable.winguo_help_voice,getString(R.string.help_voice_to),res[0],voiceToData));
    }
    private void getRoundServiceResource() {
        String[] res = getResources().getStringArray(R.array.help_voice_service);
        for (int i = 0; i <res.length ; i++) {
            serviceData.add(res[i]);
        }
        roundService = new ExpandCollapseLayoutListView(this,new HelpVoice(R.drawable.winguo_help_round,getString(R.string.help_voice_service),res[0],serviceData));
    }

}

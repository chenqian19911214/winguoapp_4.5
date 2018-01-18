package com.winguo.personalcenter.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.guobi.account.GBAccountMgr;
import com.tencent.bugly.beta.Beta;
import com.winguo.R;
import com.winguo.adapter.RecyclerAdapter;
import com.winguo.adapter.RecyclerCommonAdapter;
import com.winguo.adapter.RecylcerViewHolder;
import com.winguo.adapter.SpacesItemDecoration;
import com.winguo.base.BaseTitleActivity;
import com.winguo.login.LoginActivity;
import com.winguo.personalcenter.safecenter.AccountSafeActivity;
import com.winguo.utils.Constants;
import com.winguo.utils.Intents;
import com.winguo.utils.SPUtils;
import com.winguo.utils.ToastUtil;
import com.winguo.view.CustomDialog2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/5.
 * 设置中心
 */

public class SetCenterActivity extends BaseTitleActivity implements View.OnClickListener, RecyclerAdapter.OnItemClickListener {

    private List<String> menu_data = new ArrayList<>();
    private LinearLayout ll_quit_login;
    private QuitBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_center);
        moreMenuData();
        setBackBtn();
        initViews();
        initListener();
    }
    /**
     * 从资源文件（comm_faction_array.xml）中读取存放的（常用功能--图片 文本）数据
     */
    private void moreMenuData() {
        receiver = new QuitBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.QUIT_SUCCESS);
        registerReceiver(receiver,filter);
        //从res 资源文件中读取 存放数据（文本）
        String[] comm_iconNames = getResources().getStringArray(R.array.setting_center_menu_names);
        for (int i = 0; i < comm_iconNames.length; i++) {
            menu_data.add(comm_iconNames[i]);
        }
        //是否已经绑定推荐人  绑定过则执行下列方法
       // menu_data.remove(comm_iconNames.length-1);
    }

    private void initViews() {
        ll_quit_login = (LinearLayout) findViewById(R.id.ll_quit_login);
        RecyclerView setting_center_rv = (RecyclerView) findViewById(R.id.setting_center_rv);
        setting_center_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        setting_center_rv.setItemAnimator(new DefaultItemAnimator());
        setting_center_rv.addItemDecoration(new SpacesItemDecoration(3));
        RecyclerCommonAdapter adapter =  new RecyclerCommonAdapter<String>(this,R.layout.setting_item_layout,menu_data) {
            @Override
            protected void convert(RecylcerViewHolder holder, String s, int position) {
                holder.setText(R.id.setting_item_title,s);
            }
        };
        setting_center_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        if (SPUtils.contains(this,"accountName")) {
            ll_quit_login.setVisibility(View.VISIBLE);
        }else {
            ll_quit_login.setVisibility(View.GONE);
        }

    }

    private void quitDialog() {
        final CustomDialog2 dialog=new CustomDialog2(this);
        dialog.setDialogTitle(getResources().getString(R.string.quit_dialog_title));
        dialog.setNegativeButton(getResources().getString(R.string.quit_negative_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ll_quit_login.setVisibility(View.GONE);
                //清除本地数据
                Intent filter = new Intent();
                filter.setAction(Constants.QUIT_SUCCESS);
                sendBroadcast(filter);
            }
        });
        dialog.setPositiveButton(getResources().getString(R.string.quit_positive_text), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
      /*QuitLoginDialog.Builder builder = new QuitLoginDialog.Builder(PersonalActivity.this);
        builder.setMessage("确定退出？");
        builder.setTitle("退出登录");
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                GBAccountMgr.getInstance().doClears();
                ll_quit_login.setVisibility(View.GONE);
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();*/

    }

    private void initListener() {
       ll_quit_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_quit_login:
                //退出登录
                quitDialog();
                break;
        }
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                //个人信息
                if (SPUtils.contains(this, "accountName")) {
                    startActivity(new Intent(this, PersonalActivity.class));
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.ACCOUNT_SAFE);
                    startActivity(it);
                }
                break;
            case 1:
                //账户安全
                if (SPUtils.contains(this, "accountName")) {
                    startActivity(new Intent(this, AccountSafeActivity.class));
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.ACCOUNT_SAFE);
                    startActivity(it);
                }
                break;
            case 2:
                //帮助中心
                Intents.helpWin(SetCenterActivity.this);
                break;
            case 3:
                //检查更新
                Beta.checkUpgrade();
                break;
            case 4:
                //意见反馈
                if (SPUtils.contains(this, "accountName")) {
                    startActivity(new Intent(this, FeedBackActivity.class));
                } else {
                    Intent it = new Intent(this, LoginActivity.class);
                    it.putExtra("putExtra", Constants.LOGIN_WAY_FEEDBACK);
                    startActivity(it);
                }
                break;
            case 5:
                //关于我们
                Intent we = new Intent(SetCenterActivity.this, AboutWoActivity.class);
                //we.putExtra("title", "关于我们");
                startActivity(we);
                break;
            case 6:
                //推荐者绑定
                startActivity(new Intent(SetCenterActivity.this,PresenterBindActivity.class));
                break;
        }

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
    public class QuitBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.QUIT_SUCCESS)) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

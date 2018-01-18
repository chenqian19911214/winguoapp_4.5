package com.guobi.wallet;

import com.umeng.analytics.MobclickAgent;
import com.winguo.R;
import com.winguo.base.GBBaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class SimpleYesNoDialog extends GBBaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hybrid4_simple_yes_no_dlg);
        Button yes = (Button) findViewById(R.id.dialog_yes);
        Button no = (Button) findViewById(R.id.dialog_no);
        final Intent intent = this.getIntent();
        final String title = intent.getStringExtra("title");
        if (title != null) {
            setTitle(title);
        }
        final String msg = intent.getStringExtra("msg");
        ((TextView) findViewById(R.id.simple_message)).setText(msg);

        if (msg.contentEquals(getResources().getString(R.string.hybrid4_account_bankcard_match_error_msg))) {
            no.setVisibility(View.GONE);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });
        }


    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void onClickYes(View view) {
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void onClickNo(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}

package com.guobi.winguoapp.voice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guobi.gblocation.GBDLocation;
import com.guobi.gfc.VoiceFun.phrase.CallPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.phrase.LauncherPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.phrase.SMSPhraseAnalyzer;
import com.guobi.gfc.VoiceFun.regex.CallPattern;
import com.guobi.gfc.VoiceFun.regex.MsgMatcher;
import com.guobi.gfc.VoiceFun.regex.Pattern;
import com.guobi.gfc.VoiceFun.regex.SmsPattern;
import com.guobi.gfc.VoiceFun.utils.AppUtils;
import com.guobi.gfc.gbmiscutils.config.GBVersionChecker;
import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.guobi.winguoapp.BrowserUtils;
import com.winguo.R;
import com.winguo.personalcenter.setting.HelpVoiceActivity;
import com.winguo.activity.Search2Activity;
import com.winguo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 逻辑过程:
 * 语音识别初始化（获取语音监听SpeechListener）
 * -->SpeechListener的OnResult 获取（语音转换）文本结果
 * --> 开始匹配 MsgMatcher.match(s)
 * -->MsgMatcher.match(s)中 匹配监听(OnMatchListener)调用matchOver() 开启线程 获取（联系人）电话号码 或（短信内容）
 * -->分析成功后执行pattern.startIntent（）方法跳转对应的页面
 */
public final class VoiceFunActivity extends Activity implements OnClickListener, MsgMatcher.OnMatchListener {

    public static final int REQUESTCODE = 0xB00998;
    public static final int DIAL_REQUESTCODE = 0xB00154;
    public static final int HELP_REQUESTCODE = 0x254;
    public static final int REQUEST_CODE_PERMISSIONS_READ_CONTACT = 130;
    public static final int REQUEST_CODE_PERMISSIONS_RADIO = 131;

    private static final String TAG = VoiceFunActivity.class.getSimpleName();
    private static final String Tag = "voicefun";

    private static final int CALL_VER = 2;
    private static final int SMS_VER = 1;

    /**
     * 云端语音识别，本地语音识别完全不行，固定的几个词在匹配
     */
    public Speech mSpeech;
    private Button mAgainButton;
    private TextView mDesTextView;
    private TextView mNoSpeechTv;

    private ImageView mPromptView;
    //private View mHelpProView;
    //private View mHelpView;

    //	private ProgressBar mProgressBar
    private AnimationDrawable mAniDrawable;

    private int mMode = 0;

    private String mContent; //语音识别词
    private boolean mIsSpeaked;
    private boolean mIsAnalysis;
    private MsgMatcher mMatcher;
    private CallPhraseAnalyzer mCallPhraseAnalyzer;
    private SMSPhraseAnalyzer mSmsPhraseAnalyzer;


    private GBDLocation loc;
    private ImageView mHelpView;
    private View mHelpProView;
    private LauncherPhraseAnalyzer mLauncherPhraseAnalyzer;
    //	private boolean mUpdateFlag;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate  ");
        // 界面之外的事件获取标识
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        //获取录音权限

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_PERMISSIONS_RADIO);
            }

        } else {
            setContentView(R.layout.voicefun_main_layout);
            //重设布局的宽和高  默认Dialog主题会缩放一半
          /*  WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
            android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
            p.height = (int) (d.getHeight() * 0.92); // 高度设置为屏幕的0.8
            p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.7
            getWindow().setAttributes(p);*/

            loc = GBDLocation.getInstance();
            loc.startLocation(null);
            initViews();
            initSpeech();
            initMatch();
        }


    }

    /**
     * 初始化词组分析器 匹配器
     */
    private void initMatch() {
        mCallPhraseAnalyzer = new CallPhraseAnalyzer(this.getApplicationContext());
        mSmsPhraseAnalyzer = new SMSPhraseAnalyzer(this.getApplicationContext());
        String phase = getIntent().getStringExtra("analysis");
        if (phase != null && phase.equals("true")) {
            mMatcher = new MsgMatcher();
            mMatcher.addPattern(SmsPattern.DEFAULT_PATTERNS);
            mMatcher.addPattern(CallPattern.DEFAULT_PATTERNS);
            mMatcher.setOnMatchListener(this);
            mIsAnalysis = true;
        }
    }

    private void initViews() {
        LinearLayout isGuideLayout = (LinearLayout) findViewById(R.id.first_speak_guide);
        findViewById(R.id.voicefun_jump_img).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.voicefun_speech_cancel);
        cancelBtn.setOnClickListener(this);

        Button againBtn = (Button) findViewById(R.id.voicefun_speech_speak_again);
        againBtn.setOnClickListener(this);

        mAgainButton = againBtn;
        //等初始化ok才行
        mAgainButton.setEnabled(false);
        mHelpView = (ImageView) findViewById(R.id.voicefun_prompt_help_view);
        mHelpView.setOnClickListener(this);


        mDesTextView = (TextView) findViewById(R.id.voicefun_prompt_textView);
        mNoSpeechTv = (TextView) findViewById(R.id.voicefun_prompt_nospeach_tv);
        mNoSpeechTv.setOnClickListener(this);
        mPromptView = (ImageView) findViewById(R.id.voicefun_prompt_imageView);

//		mProgressBar = (ProgressBar) findViewById (R.id.voicefun_connect_progressBar);

    }

    /**
     * 初始化语音识别
     */
    public void initSpeech() {
        mSpeech = SpeechFactory.createSpeech(getApplicationContext(), mSpeechListener);
        mSpeech.init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_PERMISSIONS_RADIO == requestCode) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                setContentView(R.layout.voicefun_main_layout);
              /*  WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
                android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
                p.height = (int) (d.getHeight() * 0.92); // 高度设置为屏幕的0.8
                p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.7
                getWindow().setAttributes(p);
*/
                loc = GBDLocation.getInstance();
                loc.startLocation(null);
                initViews();
                initSpeech();
                initMatch();

            } else {
                //Intents.noPermissionStatus(this,"录音权限需要打开");
                ToastUtil.showToast(this, "录音权限未打开，请先开启此权限！");
                finish();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voicefun_speech_speak_again:
                if (mMode == 0) {
                    System.out.println("stopListening");
                    mSpeech.stopListening();
                    break;
                }
            case R.id.voicefun_prompt_nospeach_tv:
//			if (!mIsAnalysis) {
                mMode = 0;
                //startRecord();
                mSpeech.startListening();

                mNoSpeechTv.setVisibility(View.GONE);
                mDesTextView.setVisibility(View.VISIBLE);
                mPromptView.setVisibility(View.VISIBLE);
                mAgainButton.setVisibility(View.VISIBLE);

                mAgainButton.setText(R.string.voicefun_text_speak_over);
                break;
//			}

            case R.id.voicefun_speech_cancel:
                if (mCurThread != null) {
                    mCurThread.mIsCanceled = true;
                    mCurThread = null;
                }
                mSpeech.cancel();
                finish();
                break;
            case R.id.voicefun_prompt_help_view:
                startActivity(new Intent(this, HelpVoiceActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 界面之外的事件处理
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            finish();
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 语音查询失败
     *
     * @param name
     */
    public void findFailed(String name) {
        StringBuffer buffer = new StringBuffer(getString(R.string.voicefun_prompt_cannot_find_contact));
        buffer.append(name);

        final String msg = buffer.toString();

		/*if (mTTSEngine != null) {  //文本转语音
            mTTSEngine.speak(msg);
		}*/
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(VoiceFunActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUESTCODE://跳转发短信 请求码结果
                // setResult(resultCode, data);
                //finish();
                break;
            case RESULT_CANCELED://取消（打电话 发短息动作） 请求码结果
                ToastUtil.showToast(this, "取消成功");
                break;
            case DIAL_REQUESTCODE: {
                Intent intent = new Intent();
                intent.putExtra("result", "accepted");
                intent.putExtra("rawinput", mContent);
                setResult(Activity.RESULT_OK);
                finish();
                break;
            }
            case HELP_REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    //startRecord();
                    mSpeech.startListening();

                    mNoSpeechTv.setVisibility(View.GONE);
                    mDesTextView.setVisibility(View.VISIBLE);
                    mPromptView.setVisibility(View.VISIBLE);
                    mAgainButton.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
//		}
    }

    /**
     * 语音监听接口
     */
    private SpeechListener mSpeechListener = new SpeechListener() {

        @Override
        public void onInit(boolean success) {
            if (success) {
                mSpeech.startListening();
            } else {
                Toast.makeText(VoiceFunActivity.this, "Unknow error!", Toast.LENGTH_SHORT).show();
                VoiceFunActivity.this.finish();
            }
        }

        @Override
        public void onReadyForSpeech() {
            mAgainButton.setEnabled(true);
            mDesTextView.setText(R.string.voicefun_prompt_speak_prepare);
            mMode = 0;
            mAgainButton.setText(R.string.voicefun_text_speak_over);

            if (mAniDrawable != null) {
                mAniDrawable.stop();
                mAniDrawable = null;
            }
            mPromptView.setImageResource(R.drawable.voicefun_wait_anim);

            Drawable drawable = mPromptView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                mAniDrawable = (AnimationDrawable) drawable;
                mAniDrawable.start();
            }
        }

        @Override
        public void onBeginOfSpeech() {
            mIsSpeaked = true;
            if (mAniDrawable != null) {
                mAniDrawable.stop();
                mAniDrawable = null;
            }

            mPromptView.setImageResource(R.drawable.voicefun_speaking_anim);
            AnimationDrawable animationDrawable = (AnimationDrawable) mPromptView.getDrawable();
            animationDrawable.start();

            mAniDrawable = animationDrawable;

        }

        @Override
        public void onEndOfSpeech() {
            if (mAniDrawable != null) {
                mAniDrawable.stop();
                mAniDrawable = null;
            }
            mPromptView.setImageResource(R.drawable.voicefun_recognizing_anim);
            Drawable drawable = mPromptView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                mAniDrawable = (AnimationDrawable) drawable;
                mAniDrawable.start();
            }

            mDesTextView.setText(getString(R.string.voicefun_prompt_recognizing));
            mAgainButton.setVisibility(View.GONE);
        }

        @Override
        public void onSpeechError(String msg) {
            if (msg != null && msg.length() > 0) {
                mNoSpeechTv.setText(msg);
                mNoSpeechTv.setVisibility(View.VISIBLE);
                mDesTextView.setVisibility(View.INVISIBLE);
                mPromptView.setVisibility(View.INVISIBLE);
                mAgainButton.setVisibility(View.VISIBLE);

                mAgainButton.setText(getString(R.string.voicefun_text_speak_again));
                mAgainButton.setEnabled(true);
                mMode = 1;
                return;
            }
        }

        //语音监听 解析到结果result
        @Override
        public void onResult(String s, boolean islast) {

            if (s.length() > 2) {
                String head = s.substring(0, 2);
                Log.i("chenqian", "s head:  " + head);
                //"打开" 、"启动" 、"运行" app
                if (head.equals("打开") || head.equals("启动") || head.equals("运行")) {
                    Log.i("chenqian", "s ;" + s);

                    String appname = s.substring(2);
                    Log.i("chenqian", "模糊之前appname ;" + appname);

                    try {
                        List<String> result = new ArrayList<>();
                        AppUtils.getSuggestAppsBySpeech(VoiceFunActivity.this, appname, result, 0, 0.65f);
                        Log.i("chenqian", "size ;" + result.size());

                        int size = result.size();

                        if (size == 0) {
                           // Toast.makeText(VoiceFunActivity.this, "没有找到：" + appname, Toast.LENGTH_SHORT).show();
                            startWebSearch(s);
                            finish();
                        }

                        for (int i = 0; i < size; i++) {
                            Log.i("chenqian", "模糊查询之后AppUtils appname ;" + result.get(i).toString());
                        }

                        List<AppUtils.AppResult> app = AppUtils.findApp(VoiceFunActivity.this, result.get(0).toString());

                        String packageName = app.get(0).packageName;
                        String activityName = app.get(0).activityName;
                        Log.i("chenqian", "App appname app packageName;" + packageName + "   activityName:" + activityName);
                        Intent appIc = getPackageManager().getLaunchIntentForPackage(packageName);
                        startActivity(appIc);
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //不是"打开" 、"启动" 、"运行" app ----->  执行"打电话"、"发短信"、"web搜索"
                    if (mMatcher != null) {
                        mContent = s;
                        mMatcher.match(s);
                        Log.i("result", "---------------------------------" + s);
                        return;
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("result", "input");
                        intent.putExtra("rawinput", s);
                        Log.i("result no matcher", "---------------------------------" + s);
                        setResult(RESULT_OK, intent);
//				        startWebSearch(s);
                        finish();
                        //Toast.makeText(VoiceFunActivity.this, s, 500).show();
                    }
                }
            } else {
                //语音文字少于2个字符 导致（String head = s.substring(0, 2);）差分时索引越界 直接网页搜索
                startWebSearch(s);

            }

            if (mAniDrawable != null) {
                mAniDrawable.stop();
                mAniDrawable = null;
            }
            mPromptView.setVisibility(View.VISIBLE);
            mAgainButton.setVisibility(View.VISIBLE);

        }

        @Override
        public void onVolumeChanged(int volume) {

        }

    };

    //检查版本更新
    private boolean hasNewFun() {

        if (GBVersionChecker.getInstance() == null) {
            GBVersionChecker.createIntance(this).setVersion(Tag, 1);
        }
        return GBVersionChecker.getInstance().checkUpdateFlag(Tag);
        //return true;
    }


    private AnalyzeThread mCurThread;

    /**
     * 匹配监听
     *
     * @param pattern
     * @param msg
     */
    @Override
    public void matchOver(Pattern pattern, ArrayList<Point> msg) {
        if (pattern != null) {
            if (mCurThread != null) {
                mCurThread.mIsCanceled = true;
                mCurThread = null;
            }
            mCurThread = new AnalyzeThread(pattern, msg);
            mCurThread.start();
            Log.i("result matchOver1", "--------" + mContent);
        } else {
            //网络 搜索跳转
            //startSearch(mContent);
            Log.i("result matchOver2", "--------" + mContent);
            startWebSearch(mContent);
        }
    }



    /**
     * 语义分析线程
     */
    public class AnalyzeThread extends Thread implements MsgMatcher.OnAnalyzeListener {

        boolean mIsCanceled = false;
        Pattern pattern;
        ArrayList<Point> array;

        public AnalyzeThread(Pattern pattern, ArrayList<Point> array) {
            this.pattern = pattern;
            this.array = array;
        }

        /**
         * 开启线程匹配
         */
        @Override
        public void run() {
            if (mIsCanceled)
                return;
            if (pattern instanceof CallPattern) {
                pattern.doAction(this, array, mContent, mCallPhraseAnalyzer);
            } else if (pattern instanceof SmsPattern) {
                pattern.doAction(this, array, mContent, mSmsPhraseAnalyzer);
            }
        }

        @Override
        public void analyzeSusccess(Object info, String content) {
            if (mIsCanceled) {
                return;
            }
            //匹配成功 跳转对应的页面
            pattern.startIntent(VoiceFunActivity.this, info, content, mContent);
        }

        @Override
        public void analyzeFailed(String content) {
            if (mIsCanceled)
                return;
            //启动网络搜索 content
            //startSearch(content);
            Log.i("result analyzeFailed", "--------" + content);
            startWebSearch(content);
        }

        @Override
        public void findFailed(String name) {
            if (mIsCanceled)
                return;
            VoiceFunActivity.this.findFailed(name);
        }

        @Override
        public boolean isCanceled() {
            return mIsCanceled;
        }
    }

    /**
     * 关键词组(打电话|发短息) 分析（失败|null）
     * -->网络 本地搜索关键字
     *
     * @param voiceRes 语音—>文本结果
     */
    private void startSearch(String voiceRes) {

        Intent it = new Intent(this, Search2Activity.class);
        it.putExtra("key", voiceRes);
        it.putExtra("type", "voice");
        startActivity(it);
        finish();
    }

    /**
     * 拼接关键字 url 系统浏览器
     *
     * @param content
     */
    private void startWebSearch(String content) {
        GBLogUtils.DEBUG_DISPLAY("result web", "---------------------------------" + content);
        BrowserUtils.startWinguoWebSearch(VoiceFunActivity.this, content, loc.getLatitude(), loc.getLongitude(), loc.getAddress());
        VoiceFunActivity.this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSpeech != null) {
            mSpeech.cancel();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCurThread != null) {
            mCurThread.mIsCanceled = true;
            mCurThread = null;
        }
        if (mSpeech != null) {
            mSpeech.destory();
        }
    }
}

package com.guobi.winguoapp.voice;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.winguo.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SpeechiFlytek extends Speech {

    public SpeechRecognizer mIat;

    public SpeechiFlytek(Context context, SpeechListener listener) {
        super(context, listener);
    }
    public SpeechiFlytek(Context context) {

        super(context);
        mIat = SpeechRecognizer.getRecognizer();
        setParam();
    }
    @Override
    public boolean init() {
        synchronized (this) {
            mIat = SpeechRecognizer.getRecognizer();
            if (mIat == null) {
                mIat = SpeechRecognizer.createRecognizer(mContext,
                        mInitListener);
            } else {
                setParam();
                mListener.onInit(true);
            }
        }
        return true;
    }

    @Override
    public void startListening() {
        synchronized (this) {
            if (mIat != null) {
                mListener.onReadyForSpeech();
                mIat.startListening(recognizerListener);
            }
        }
    }

    @Override
    public void stopListening() {
        synchronized (this) {
            if (mIat != null) {
                mIat.stopListening();
                mListener.onEndOfSpeech();
            }
        }
    }

    @Override
    public void cancel() {
        synchronized (this) {
            if (mIat != null) {
                if (mIat.isListening()) {
                    mIat.cancel();
                }
            }
        }
    }

    @Override
    public void destory() {
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            System.out.println("xunfei: onInit");

            if (code == ErrorCode.SUCCESS) {
                //ContactUtils.updateContactForSpeech(mContext);
                setParam();
                mListener.onInit(true);
            } else {
                mListener.onInit(false);
            }
        }
    };

    public void setParam() {
        synchronized (this) {
            String lag = "mandarin";

            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
            // 设置语音前端点
            mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
            // 设置语音后端点
            mIat.setParameter(SpeechConstant.VAD_EOS, "1800");

            mIat.setParameter(SpeechConstant.ASR_PTT, "0");

            mIat.setParameter(SpeechConstant.NET_TIMEOUT, "6000");
            // 设置音频保存路径
            mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                    Environment.getExternalStorageDirectory().getPath() + "/iflytek/wavaudio.pcm");
        }

    }

    /**
     * 听写监听器。
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {

        private StringBuffer recResult = new StringBuffer();
        private int maxVolume = 0;

        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
            System.out.println("xunfei: onVolumeChanged: " + volume);
            if (volume > maxVolume) {
                maxVolume = volume;
            }
            mListener.onVolumeChanged(volume);
        }

        @Override
        public void onBeginOfSpeech() {
            System.out.println("xunfei: onBeginOfSpeech");

            recResult.setLength(0);
            maxVolume = 0;

            mListener.onBeginOfSpeech();
        }

        @Override
        public void onError(SpeechError error) {
            System.out.println("xunfei: onError");

            String msg = "";
            switch (error.getErrorCode()) {
                case 10118:
                    msg = mContext.getString(R.string.voicefun_prompt_no_speech);
                    break;
                case 20002:
                    msg = mContext.getString(R.string.voicefun_prompt_net_time_out);
                    break;
                case 20001:
                    msg = mContext.getString(R.string.voicefun_prompt_no_net);
                    break;
                case 20003:
                    msg = mContext.getString(R.string.voicefun_prompt_net_exception);
                    break;
                case 20004:
                    msg = mContext.getString(R.string.voicefun_error_INVALID_RESULT);
                    break;
                case 20005:
                    msg = mContext.getString(R.string.voicefun_error_NO_MATCH);
                    break;
                case 20006:
                    msg = mContext.getString(R.string.voicefun_error_AUDIO_RECORD);
                    break;
                case 20007:
                    msg = mContext.getString(R.string.voicefun_error_NO_SPPECH);
                    break;
                case 20008:
                    msg = mContext.getString(R.string.voicefun_error_SPEECH_TIMEOUT);
                    break;
                case 20009:
                    msg = mContext.getString(R.string.voicefun_error_EMPTY_UTTERANCE);
                    break;
                case 20020:
                    msg = mContext.getString(R.string.voicefun_error_MEMORY_WRANING);
                    break;
                case 20999:
                    msg = mContext.getString(R.string.voicefun_error_UNKNOWN);
                    break;
                case 21003:
                    msg = mContext.getString(R.string.voicefun_error_LOCAL_NO_INIT);
                    break;
                case 22001:
                    msg = mContext.getString(R.string.voicefun_error_LOCAL_NO_INIT);
                    break;
                case 22005:
                    msg = mContext.getString(R.string.voicefun_error_VERSION_LOWER);
                    break;
                default:
                    msg = error.getPlainDescription(true);
                    break;
            }

            mListener.onSpeechError(msg);
        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("xunfei: onEndOfSpeech");

            mListener.onEndOfSpeech();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {

            String text = parseIatResult(results.getResultString());

            if (isLast) {

                if (("。，").indexOf(text) == -1) {
                    recResult.append(text);
                }

                mListener.onEndOfSpeech();

                String s = recResult.toString();
                if (s == null || s.length() <= 0) {
                    mListener.onSpeechError(mContext.getString(R.string.voicefun_prompt_no_speech));
                } else {
                    mListener.onResult(s, true);
                }
            } else {
                recResult.append(text);
            }
        }

//		@Override
//		public void onVolumeChanged(int volume) {
//			System.out.println("xunfei: onVolumeChanged: " + volume);
//
//			if (volume > maxVolume) {
//				maxVolume = volume;
//			}
//			mListener.onVolumeChanged(volume);
//		}

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}

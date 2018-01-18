package com.guobi.winguoapp.voice;

import android.content.Context;

import com.aispeech.AIError;
import com.aispeech.AIResult;
import com.aispeech.common.AIConstant;
import com.aispeech.common.JSONResultParser;
import com.aispeech.export.engines.AICloudASREngine;
import com.aispeech.export.engines.AICloudTTSEngine;
import com.aispeech.export.listeners.AIASRListener;
import com.aispeech.speech.SpeechReadyInfo;
import com.winguo.R;


public class SpeechAI extends Speech {

    private AICloudASREngine mEngine;
    private AICloudTTSEngine mTTSEngine;

    public SpeechAI(Context context, SpeechListener listener) {
        super(context, listener);
    }


    @Override
    public boolean init() {

        mEngine = AICloudASREngine.getInstance();

        mEngine.init(mContext, new AISpeechListener(), AppKey.APPKEY, AppKey.SECRETKEY);
        mEngine.setNBest(3);
        mEngine.setUseEmotion(true);
        mEngine.setUseSex(true);

        mTTSEngine = AICloudTTSEngine.getInstance();
        mTTSEngine.init(mContext, null, AppKey.APPKEY, AppKey.SECRETKEY);
        mTTSEngine.setLanguage(AIConstant.CN_TTS);
        mTTSEngine.setRes("syn_chnsnt_zhilingf");

        return true;
    }

    @Override
    public void startListening() {
        mEngine.start();
    }

    @Override
    public void stopListening() {
        mEngine.stopRecording();
        mTTSEngine.stop();
    }

    @Override
    public void cancel() {
        mEngine.cancel();
        mTTSEngine.stop();
    }

    @Override
    public void destory() {
        if (mEngine != null) {
            mEngine.destory();
            mEngine = null;
        }

        if (mTTSEngine != null) {
            mTTSEngine.destory();
            mTTSEngine = null;
        }
    }


    private class AISpeechListener implements AIASRListener {

        @Override
        public void onBeginningOfSpeech() {
            System.out.println("ai: onBeginningOfSpeech");
            mListener.onBeginOfSpeech();
        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("ai: onEndOfSpeech");
            //mTTSEngine.speak(getString(R.string.voicefun_prompt_handling));
            mListener.onEndOfSpeech();
        }

        @Override
        public void onError(AIError error) {
            System.out.println("ai: onError");

            String msg = "";
            switch (error.getErrId()) {
                case AIError.ERR_NO_SPEECH:
                    msg = mContext.getString(R.string.voicefun_prompt_no_speech);
                    break;
                case 70200:
                    msg = mContext.getString(R.string.voicefun_prompt_network_abnormal);
                    break;
                default:
                    msg = error.getError();
                    break;
            }

            mListener.onSpeechError(msg);
        }

        @Override
        public void onInit(int status) {
            System.out.println("ai: onInit");

            mListener.onInit(status == AIConstant.OPT_SUCCESS);
        }

        @Override
        public void onReadyForSpeech(SpeechReadyInfo params) {
            System.out.println("ai: onReadyForSpeech");
            //mIsSpeaked = false;
            mListener.onReadyForSpeech();
        }

        @Override
        public void onResults(AIResult results) {
            System.out.println("ai: onResults");
            if (results.isLast() && results.getResultType() == AIConstant.AIENGINE_MESSAGE_TYPE_JSON) {

                JSONResultParser parser = new JSONResultParser(results.getResultObject().toString());
                StringBuffer buffer = new StringBuffer();

                for (String s : parser.getNBestRec()) {
                    buffer.append(s);

                    System.out.println("ai: onResults0: " + s);

                    if (s == null || s.isEmpty()) {
                        mListener.onSpeechError(mContext.getString(R.string.voicefun_prompt_no_speech));
                        break;
                    } else {
                        mListener.onResult(s, true);

                        mEngine.cancel();
                        mTTSEngine.stop();
                        break;
                    }
                }
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            mListener.onVolumeChanged((int) rmsdB);
        }
    }

}

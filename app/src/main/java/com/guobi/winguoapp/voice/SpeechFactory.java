package com.guobi.winguoapp.voice;


import android.content.Context;

public class SpeechFactory {

    public static Speech createSpeech(Context context, SpeechListener listener) {

        if (VoiceFunEngineConfig.REG_ENGINE_MODE == VoiceFunEngineConfig.REG_ENGINE_AISPEECH) {
            return new SpeechAI(context, listener);
        } else {
            return new SpeechiFlytek(context, listener);
        }
    }

}

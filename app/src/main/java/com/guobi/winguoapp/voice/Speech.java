package com.guobi.winguoapp.voice;

import android.content.Context;

public abstract class Speech {


    public abstract boolean init();

    public abstract void startListening();

    public abstract void stopListening();

    public abstract void cancel();

    public abstract void destory();


    public Speech(Context context, SpeechListener listener) {
        mContext = context;
        mListener = listener;
    }
    public Speech(Context context) {
        mContext = context;

    }

    protected Context mContext;
    protected SpeechListener mListener;


}

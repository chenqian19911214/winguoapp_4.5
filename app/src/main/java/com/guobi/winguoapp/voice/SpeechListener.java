package com.guobi.winguoapp.voice;

public interface SpeechListener {

     void onInit(boolean success);

     void onReadyForSpeech();

     void onBeginOfSpeech();

     void onEndOfSpeech();

     void onSpeechError(String message);

     void onResult(String result, boolean islast);

     void onVolumeChanged(int volume);

}

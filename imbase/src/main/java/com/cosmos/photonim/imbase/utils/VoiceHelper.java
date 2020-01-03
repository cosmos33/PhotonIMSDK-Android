package com.cosmos.photonim.imbase.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import top.oply.opuslib.OpusEvent;
import top.oply.opuslib.OpusPlayer;
import top.oply.opuslib.OpusRecorder;
import top.oply.opuslib.OpusService;

public class VoiceHelper {
    private Context context;
    private OpusReceiver mReceiver;
    private OnVoiceListener onVoiceListener;
    private String fileName;
    private boolean cancel = false;

    public VoiceHelper(Context context, OnVoiceListener onVoiceListener) {
        this.context = context;
        this.onVoiceListener = onVoiceListener;
        OpusRecorder.getInstance().setEventSender(new OpusEvent(context));
        mReceiver = new OpusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(OpusEvent.ACTION_OPUS_UI_RECEIVER);
        context.registerReceiver(mReceiver, filter);
    }

    public void destory() {
        context.unregisterReceiver(mReceiver);
    }

    public void record(String fileName, OpusRecorder.OnRecordStatusListener onRecordStatus) {
        cancel = false;
        this.fileName = fileName;
//        OpusService.record(context, fileName);
        OpusRecorder.getInstance().startRecording(fileName, onRecordStatus);
    }

    public void stopRecord() {
//        OpusService.stopRecording(context);
        OpusRecorder.getInstance().stopRecording();
    }


    public void play(String fileName) {
        OpusPlayer.getInstance().play(fileName);
//        OpusService.play(context, fileName);
    }

    public void cancelPlay() {
        OpusPlayer.getInstance().stop();
    }

    public void stopPlay() {
        OpusService.stopPlaying(context);
    }

    public void cancelRecord() {
//        OpusService.stopPlaying(context);
        cancel = true;
        OpusRecorder.getInstance().stopRecording();
        FileUtils.removeFile(fileName);
    }

    class OpusReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int type = bundle.getInt(OpusEvent.EVENT_TYPE, 0);
            switch (type) {
                case OpusEvent.CONVERT_FINISHED:
                    break;
                case OpusEvent.CONVERT_FAILED:
                    break;
                case OpusEvent.CONVERT_STARTED:
                    break;
                case OpusEvent.RECORD_FAILED:
                    if (onVoiceListener != null) {
                        onVoiceListener.onRecordFailed();
                    }
                    break;
                case OpusEvent.RECORD_FINISHED:
                    if (onVoiceListener != null && !cancel) {
                        onVoiceListener.onRecordFinish();
                    }
                    break;
                case OpusEvent.RECORD_STARTED:
                    break;
                case OpusEvent.RECORD_PROGRESS_UPDATE:
                    break;
                case OpusEvent.PLAY_PROGRESS_UPDATE:
                    break;
                case OpusEvent.PLAY_GET_AUDIO_TRACK_INFO:
                    break;
                case OpusEvent.PLAYING_FAILED:
                    break;
                case OpusEvent.PLAYING_FINISHED:
                    break;
                case OpusEvent.PLAYING_PAUSED:
                    break;
                case OpusEvent.PLAYING_STARTED:
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnVoiceListener {
        void onRecordFinish();

        void onRecordFailed();
    }
}

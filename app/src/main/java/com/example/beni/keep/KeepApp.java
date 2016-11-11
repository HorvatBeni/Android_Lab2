package com.example.beni.keep;

import android.app.Application;
import android.util.Log;

import com.example.beni.keep.net.NoteRestClient;
import com.example.beni.keep.service.NoteManager;


public class KeepApp extends Application {
    public static final String TAG = KeepApp.class.getSimpleName();
    private NoteManager mNoteManager;
    private NoteRestClient mNoteRestClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mNoteManager = new NoteManager(this);
        mNoteRestClient = new NoteRestClient(this);
        mNoteManager.setNoteRestClient(mNoteRestClient);
    }

    public NoteManager getNoteManager() {
        return mNoteManager;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate");
    }
}

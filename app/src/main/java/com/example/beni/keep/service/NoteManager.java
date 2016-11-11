package com.example.beni.keep.service;

import android.content.Context;
import android.util.Log;

import com.example.beni.keep.content.Note;
import com.example.beni.keep.net.NoteRestClient;
import com.example.beni.keep.util.OkCancellableCall;
import com.example.beni.keep.util.OnErrorListener;
import com.example.beni.keep.util.OnSuccessListener;

import java.util.List;

public class NoteManager {
    private static final String TAG = NoteManager.class.getSimpleName();

    private List<Note> mNotes;
    private OnNoteUpdateListener mOnUpdate;

    private final Context mContext;
    private NoteRestClient mNoteRestClient;

    public NoteManager(Context context) {
        mContext = context;
    }

    public List<Note> getNotes() throws Exception { //sync method
        Log.d(TAG, "getNotes...");
        mNotes = mNoteRestClient.getAll();
        return mNotes;
    }

    public NoteLoader getNoteLoader() {
        Log.d(TAG, "getNoteLoader...");
        return new NoteLoader(mContext, mNoteRestClient);
    }

    public void addNote() {
//        mNotes.add(new Note("Added note " + mNotes.size()));
//        if (mOnUpdate != null) {
//            mOnUpdate.updated();
//        }
    }

    public void setOnUpdate(OnNoteUpdateListener onUpdate) {
        mOnUpdate = onUpdate;
    }

    public void setNoteRestClient(NoteRestClient noteRestClient) {
        mNoteRestClient = noteRestClient;
    }

    public OkCancellableCall getNotesAsync(OnSuccessListener<List<Note>> onSuccessListener, OnErrorListener onErrorListener) {
        return mNoteRestClient.getAllAsync(onSuccessListener, onErrorListener);
    }

    public interface OnNoteUpdateListener {
        void updated();
    }
}

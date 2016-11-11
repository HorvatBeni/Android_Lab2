package com.example.beni.keep;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.beni.keep.content.Note;
import com.example.beni.keep.util.OkAsyncTask;
import com.example.beni.keep.util.OkAsyncTaskLoader;
import com.example.beni.keep.util.OkCancellableCall;
import com.example.beni.keep.util.OnErrorListener;
import com.example.beni.keep.util.OnSuccessListener;
import com.example.beni.keep.widget.NoteListAdapter;

import java.util.List;

public class NoteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Note>> {

    public static final String TAG = NoteListFragment.class.getSimpleName();
    private static final int NOTE_LOADER_ID = 1;
    private KeepApp mApp;
    private NoteListAdapter mNoteListAdapter;
    private AsyncTask<String, Void, List<Note>> mGetNotesAsyncTask;
    private ListView mNoteListView;
    private View mContentLoadingView;
    private boolean mContentLoaded = false;
    private OkCancellableCall mGetNotesAsyncCall;

    public NoteListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        mApp = (KeepApp) context.getApplicationContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View layout = inflater.inflate(R.layout.fragment_notes, container, false);
        mNoteListView = (ListView) layout.findViewById(R.id.note_list);
        mContentLoadingView = layout.findViewById(R.id.content_loading);
        showLoadingIndicator();
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        //getLoaderManager().initLoader(NOTE_LOADER_ID, null, this); //:) :(
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        //startGetNotesAsyncTask(); //:(
        startGetNotesAsyncCall(); //:)
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        //ensureGetNotesAsyncTaskCancelled();
        ensureGetNotesAsyncCallCancelled();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void startGetNotesAsyncTask() {
        if (mContentLoaded) {
            Log.d(TAG, "startGetNotesAsyncTask - content already loaded, return");
            return;
        }
        mGetNotesAsyncTask = new OkAsyncTask<String, Void, List<Note>>() {

            @Override
            protected void onPreExecute() {
                showLoadingIndicator();
                Log.d(TAG, "GetNotesAsyncTask - showLoadingIndicator");
            }

            @Override
            protected List<Note> tryInBackground(String... params) throws Exception {
                Log.d(TAG, "GetNotesAsyncTask - tryInBackground");
                return mApp.getNoteManager().getNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                Log.d(TAG, "GetNotesAsyncTask - onPostExecute");
                if (backgroundException != null) {
                    Log.e(TAG, "Get notes failed");
                    showError(backgroundException);
                } else {
                    showContent(notes);
                }
            }
        }.execute();
    }

    private void ensureGetNotesAsyncTaskCancelled() {
        if (mGetNotesAsyncTask != null && !mGetNotesAsyncTask.isCancelled()) {
            Log.d(TAG, "ensureGetNotesAsyncTaskCancelled - cancelling the task");
            mGetNotesAsyncTask.cancel(true);
        } else {
            Log.d(TAG, "ensureGetNotesAsyncTaskCancelled - task already completed or cancelled");
        }
    }

    @Override
    public Loader<List<Note>> onCreateLoader(int id, Bundle args) {
        showLoadingIndicator();
        return mApp.getNoteManager().getNoteLoader();
    }

    @Override
    public void onLoadFinished(Loader<List<Note>> loader, List<Note> notes) {
        if (loader instanceof OkAsyncTaskLoader) {
            Exception loadingException = ((OkAsyncTaskLoader) loader).loadingException;
            if (loadingException != null) {
                Log.e(TAG, "Get notes failed");
                showError(loadingException);
                return;
            }
            showContent(notes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Note>> loader) {
        // not used
    }

    private void startGetNotesAsyncCall() {
        if (mContentLoaded) {
            Log.d(TAG, "startGetNotesAsyncCall - content already loaded, return");
            return;
        }
        mGetNotesAsyncCall = mApp.getNoteManager().getNotesAsync(
                new OnSuccessListener<List<Note>>() {
                    @Override
                    public void onSuccess(final List<Note> notes) {
                        Log.d(TAG, "startGetNotesAsyncCall - success");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showContent(notes);
                            }
                        });
                    }
                }, new OnErrorListener() {
                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "startGetNotesAsyncCall - error");
                        showError(e);
                    }
                }
        );
    }

    private void ensureGetNotesAsyncCallCancelled() {
        if (mGetNotesAsyncCall != null) {
            Log.d(TAG, "ensureGetNotesAsyncCallCancelled - cancelling the task");
            mGetNotesAsyncCall.cancel();
        }
    }


    private void showError(Exception e) {
        Log.e(TAG, "showError", e);
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setCancelable(true)
                .create()
                .show();
    }

    private void showLoadingIndicator() {
        Log.d(TAG, "showLoadingIndicator");
        mNoteListView.setVisibility(View.GONE);
        mContentLoadingView.setVisibility(View.VISIBLE);
    }

    private void showContent(final List<Note> notes) {
        Log.d(TAG, "showContent");
        mNoteListAdapter = new NoteListAdapter(this.getContext(), notes);
        mNoteListView.setAdapter(mNoteListAdapter);
        mContentLoadingView.setVisibility(View.GONE);
        mNoteListView.setVisibility(View.VISIBLE);
    }

}

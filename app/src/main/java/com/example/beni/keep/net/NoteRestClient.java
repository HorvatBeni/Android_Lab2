package com.example.beni.keep.net;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.example.beni.keep.R;
import com.example.beni.keep.content.Note;
import com.example.beni.keep.util.OkCancellableCall;
import com.example.beni.keep.util.OnErrorListener;
import com.example.beni.keep.util.OnSuccessListener;
import com.example.beni.keep.util.ResourceListReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoteRestClient {
    private static final String TAG = NoteRestClient.class.getSimpleName();

    private final OkHttpClient mOkHttpClient;
    private final String mApiUrl;
    private final String mNoteUrl;

    public NoteRestClient(Context context) {
        mOkHttpClient = new OkHttpClient();
        mApiUrl = context.getString(R.string.api_url);
        mNoteUrl = mApiUrl.concat("/Note");
        Log.d(TAG, "NoteRestClient created");
    }

    public List<Note> getAll() throws IOException { //sync operation
        Request request = new Request.Builder().url(mNoteUrl).build();
        Call call = null;
        try {
            call = mOkHttpClient.newCall(request);
            Log.d(TAG, "getAll started");
            Response response = call.execute();
            Log.d(TAG, "getAll completed");
            JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
            return new ResourceListReader<Note>(new NoteReader()).read(reader);
        } catch (IOException e) {
            Log.e(TAG, "getAll failed", e);
            throw e;
        }
    }

    public OkCancellableCall getAllAsync(final OnSuccessListener<List<Note>> osl, final OnErrorListener oel) { //async operation
        Request request = new Request.Builder().url(mNoteUrl).build();
        Call call = null;
        try {
            call = mOkHttpClient.newCall(request);
            Log.d(TAG, "getAllAsync enqueued");
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "getAllAsync failed", e);
                    oel.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "getAllAsync completed");
                    JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
                    osl.onSuccess(new ResourceListReader<Note>(new NoteReader()).read(reader));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "getAllAsync failed", e);
            oel.onError(e);
        } finally {
            return new OkCancellableCall(call);
        }
    }
}

package com.example.beni.keep.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.beni.keep.R;
import com.example.beni.keep.content.Note;

import java.util.List;

public class NoteListAdapter extends BaseAdapter {
    public static final String TAG = NoteListAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Note> mNotes;

    public NoteListAdapter(Context context, List<Note> notes) {
        mContext = context;
        mNotes = notes;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View noteLayout = LayoutInflater.from(mContext).inflate(R.layout.note_detail, null);
        ((TextView) noteLayout.findViewById(R.id.note_text)).setText(mNotes.get(position).getText());
        Log.d(TAG, "getView " + position);
        return noteLayout;
    }

    public void refresh() {
        notifyDataSetChanged();
    }
}

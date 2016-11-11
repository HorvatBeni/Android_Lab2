package com.example.beni.keep.net;

import android.util.JsonReader;
import android.util.Log;

import com.example.beni.keep.content.Note;
import com.example.beni.keep.util.ResourceReader;

import java.io.IOException;

public class NoteReader implements ResourceReader<Note> {
    private static final String TAG = NoteReader.class.getSimpleName();

    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "text";
    public static final String NOTE_STATUS = "status";
    public static final String NOTE_UPDATED = "updated";

    @Override
    public Note read(JsonReader reader) throws IOException {
        Note note = new Note();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(NOTE_ID)) {
                note.setId(reader.nextString());
            } else if (name.equals(NOTE_TEXT)) {
                note.setText(reader.nextString());
            } else if (name.equals(NOTE_STATUS)) {
                note.setStatus(Note.Status.valueOf(reader.nextString()));
            } else if (name.equals(NOTE_UPDATED)) {
                note.setUpdated(reader.nextLong());
            } else {
                reader.skipValue();
                Log.w(TAG, String.format("Note property '%s' ignored", name));
            }
        }
        reader.endObject();
        return note;
    }
}

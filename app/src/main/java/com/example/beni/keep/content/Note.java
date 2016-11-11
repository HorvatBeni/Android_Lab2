package com.example.beni.keep.content;

public class Note {
    public enum Status {
        active,
        archived
    }

    private String mId;
    private String mText;
    private Status mStatus = Status.active;
    private long mUpdated;

    public Note() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public long getUpdated() {
        return mUpdated;
    }

    public void setUpdated(long updated) {
        mUpdated = updated;
    }
}

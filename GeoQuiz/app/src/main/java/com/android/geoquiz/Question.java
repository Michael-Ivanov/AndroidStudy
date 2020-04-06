package com.android.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean isAnswered;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        isAnswered = false;
    }

    public int getmTextResId() {
        return mTextResId;
    }

    public void setmTextResId(int mTextResId) {
        this.mTextResId = mTextResId;
    }

    public boolean ismAnswerTrue() {
        return mAnswerTrue;
    }

    public void setmAnswerTrue(boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }

    public boolean isAnswered() {
        return isAnswered;
    }
    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

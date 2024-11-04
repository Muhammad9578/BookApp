package com.mtechsoft.bookapp.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Faheem on 04/07/2019.
 */

public abstract class BackgroundTask extends AsyncTask<Void, Void, Void> {

    final String TAG = "BackgroundTask";
    protected Context context;
    ProgressDialog mDialog;
    private String message = "Loading...", title = "Please wait";
    private boolean shouldShowProgress = true;

    public boolean shouldShowProgress() {
        return shouldShowProgress;
    }

    public void setShouldShowProgress(boolean shouldShowProgress) {
        this.shouldShowProgress = shouldShowProgress;
    }

    public BackgroundTask(Context context) {

        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = getMessage();
        if(shouldShowProgress) {
            mDialog = new ProgressDialog(context);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog = ProgressDialog.show(context, getTitle(), message);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(shouldShowProgress() && mDialog != null)
            mDialog.dismiss();

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}


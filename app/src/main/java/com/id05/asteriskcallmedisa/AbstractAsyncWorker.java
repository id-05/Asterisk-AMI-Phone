package com.id05.asteriskcallmedisa;

import android.os.AsyncTask;

import static java.lang.Thread.sleep;

abstract class AbstractAsyncWorker<String> extends AsyncTask<Void, Void, String> {
    private ConnectionCallback callback;
    private Throwable t;
    private String param;
    private String comand;

    AbstractAsyncWorker(ConnectionCallback callback, java.lang.String comand, java.lang.String param) {
        this.callback = callback;
        this.param = (String) param;
        this.comand = (String) comand;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) {
            callback.onBegin();
        }
    }

    protected abstract String doAction() throws Exception;

    @Override
    protected String doInBackground(Void... params) {
        try {

            return doAction();
        } catch (Exception e) {
            t = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String v) {
        super.onPostExecute(v);
        if (callback != null) {
            callback.onEnd();
        }
        try {
            sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        generateCallback(comand, param);
    }

    private void generateCallback(String data, String param) {
        if (callback == null) return;
        if (data != null) {
            callback.onSuccess((java.lang.String) data,  (java.lang.String) param);
        } else if (t != null) {
            callback.onFailure(t);
        } else {
            callback.onFailure(new NullPointerException("Result is empty but error empty too"));
        }
    }
}
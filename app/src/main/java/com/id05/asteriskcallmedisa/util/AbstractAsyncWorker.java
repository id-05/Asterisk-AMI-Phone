package com.id05.asteriskcallmedisa.util;

import android.os.AsyncTask;
import com.id05.asteriskcallmedisa.data.AmiState;
import static java.lang.Thread.sleep;

public abstract class AbstractAsyncWorker extends AsyncTask<Void, Void, AmiState> {
    private final ConnectionCallback callback;
    private final AmiState amistate;

    protected AbstractAsyncWorker(ConnectionCallback callback, AmiState amistate) {
        this.callback = callback;
        this.amistate = amistate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) {
            callback.onBegin();
        }
    }

    protected abstract AmiState doAction() throws Exception;

    @Override
    protected AmiState doInBackground(Void... params) {
        try {
            amistate.setResultOperation(true);
            amistate.setDescription("");
            return doAction();
        } catch (Exception e) {
            amistate.setResultOperation(false);
            amistate.setDescription(e.getMessage());
            return amistate;
        }
    }

    @Override
    protected void onPostExecute(AmiState v) {
        super.onPostExecute(v);
        if (callback != null) {
            callback.onEnd();
        }

        generateCallback(amistate);
    }

    private void generateCallback(AmiState amistate) {
        if (callback == null) return;
        if (amistate.getResultOperation()) {
            callback.onSuccess(amistate);
        } else if (!amistate.getResultOperation()) {
            callback.onFailure(amistate);
        }
    }
}
package com.ksyun.ks3.services.handler;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.services.request.object.PutObjectFetchResult;

import cz.msebera.android.httpclient.Header;

public abstract class PutObjectFetchResponseHandler extends Ks3HttpResponceHandler {

    public abstract void onTaskFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onTaskSuccess(int statesCode, Header[] responceHeaders, PutObjectFetchResult result);

    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders, byte[] response) {
        onTaskSuccess(statesCode, responceHeaders, parseKJsop(response));
    }

    @Override
    public void onFailure(int statesCode, Header[] responceHeaders, byte[] response, Throwable throwable) {
        Ks3Error error = new Ks3Error(statesCode, response, throwable);
        onTaskFailure(statesCode, error, responceHeaders, response == null ? "" : new String(response), throwable);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void finalize() throws Throwable {
        Log.d("PutObjectFetchResponseHandler", "PutObjectFetchResponseHandler finalize:" + this);
        super.finalize();
    }

    @SuppressLint("LongLogTag")
    private PutObjectFetchResult parseKJsop(byte[] response) {

        PutObjectFetchResult result = null;
        try {
            result = new Gson().fromJson(response.toString(), PutObjectFetchResult.class);
        } catch (Exception e) {
            Log.d("PutObjectFetchResponseHandler Response", "PutObjectFetchResponseHandler error:" + this);
        }
        return result;
    }
}

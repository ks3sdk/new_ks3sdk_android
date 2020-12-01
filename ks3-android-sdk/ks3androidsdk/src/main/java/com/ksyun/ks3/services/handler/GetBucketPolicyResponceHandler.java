package com.ksyun.ks3.services.handler;

import com.google.gson.Gson;
import com.ksyun.ks3.exception.Ks3Error;
import com.ksyun.ks3.model.result.BucketPolicy;

import cz.msebera.android.httpclient.Header;

public abstract class GetBucketPolicyResponceHandler extends Ks3HttpResponceHandler {

    public abstract void onFailure(int statesCode, Ks3Error error, Header[] responceHeaders, String response, Throwable paramThrowable);

    public abstract void onSuccess(int statesCode, Header[] responceHeaders, BucketPolicy policy);


    @Override
    public final void onSuccess(int statesCode, Header[] responceHeaders, byte[] response) {
        this.onSuccess(statesCode, responceHeaders, parseKJsop(responceHeaders, response));
    }

    @Override
    public final void onFailure(int statesCode, Header[] responceHeaders, byte[] response, Throwable throwable) {
        com.ksyun.ks3.exception.Ks3Error error = new Ks3Error(statesCode, response, throwable);
        this.onFailure(statesCode, error, responceHeaders, response == null ? "" : new String(response), throwable);
    }

    @Override
    public final void onProgress(long bytesWritten, long totalSize) {
    }

    @Override
    public final void onStart() {
    }

    @Override
    public final void onFinish() {
    }

    @Override
    public final void onCancel() {
    }

    private BucketPolicy parseKJsop(cz.msebera.android.httpclient.Header[] responceHeaders, byte[] response) {

        BucketPolicy bucketPolicy = null;
        try {
            String jsonBody = new String(response);
            bucketPolicy = new Gson().fromJson(jsonBody, BucketPolicy.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bucketPolicy;
    }
}

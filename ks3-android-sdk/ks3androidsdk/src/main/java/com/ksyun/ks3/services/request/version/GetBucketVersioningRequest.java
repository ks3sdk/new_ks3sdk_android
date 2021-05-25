package com.ksyun.ks3.services.request.version;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.Ks3HttpRequest;

import cz.msebera.android.httpclient.util.Asserts;

public class GetBucketVersioningRequest extends Ks3HttpRequest {

    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.GET);
        this.addParams("versioning","");
    }

    @Override
    protected String validateParams() throws Ks3ClientException {
        Asserts.notNull(this.getBucketname(), "bucket == null");
        return null;
    }
}

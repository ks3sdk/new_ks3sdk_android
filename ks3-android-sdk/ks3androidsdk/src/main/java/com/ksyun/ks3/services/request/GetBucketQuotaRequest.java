package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;

public class GetBucketQuotaRequest extends Ks3HttpRequest {

    private static final long serialVersionUID = 15097876149235360L;
    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("quota", "");
    }

    @Override
    protected String validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
        return null;
    }

    public GetBucketQuotaRequest(String bucketName) {
        this.setBucketname(bucketName);
    }
}

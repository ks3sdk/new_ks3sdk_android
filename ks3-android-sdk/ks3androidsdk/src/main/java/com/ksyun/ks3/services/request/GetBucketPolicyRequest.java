package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;

public class GetBucketPolicyRequest extends Ks3HttpRequest {

    private static final long serialVersionUID = 15097976989234360L;
    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("policy", "");
    }

    @Override
    protected void validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
    }

    public GetBucketPolicyRequest(String bucketName) {
        this.setBucketname(bucketName);
    }
}

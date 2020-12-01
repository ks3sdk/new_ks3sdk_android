package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;

public class DeleteBucketQuotaRequest extends Ks3HttpRequest {
    private static final long serialVersionUID = 1509187613918335260L;
    String acl;
    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.DELETE);
        this.addParams("quota", "");
    }

    @Override
    protected void validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
    }

    public DeleteBucketQuotaRequest(String bucketName) {
        this.setBucketname(bucketName);
    }
}

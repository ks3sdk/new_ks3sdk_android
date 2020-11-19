package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;

public class DeleteBucketReplicationConfigRequest extends Ks3HttpRequest {
    private static final long serialVersionUID = 1509787613917335360L;
    String acl;
    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.DELETE);
        this.addParams("crr", "");
    }

    @Override
    protected void validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
    }

    public DeleteBucketReplicationConfigRequest(String bucketName) {
        this.setBucketname(bucketName);
    }
}

package com.ksyun.ks3.services.request.tag;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.util.StringUtils;

public class GetObjectTaggingRequest extends Ks3HttpRequest {

    public GetObjectTaggingRequest(String bucketName, String objectName) {
        setBucketname(bucketName);
        setObjectkey(objectName);
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("tagging", "");
    }

    @Override
    protected void validateParams() throws Ks3ClientException {
        if (StringUtils.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");

        if (StringUtils.isBlank(this.getObjectkey())) {
            throw new Ks3ClientException("object key can not be null");
        }
    }

}

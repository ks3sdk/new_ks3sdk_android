package com.ksyun.ks3.services.request;

import com.google.gson.Gson;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.result.BucketPolicy;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.StringUtils;

import java.io.ByteArrayInputStream;

public class PutBuckePolicyRequest extends Ks3HttpRequest {

    private static final long serialVersionUID = 28505422321283770L;

    public BucketPolicy getBucketPolicy() {
        return bucketPolicy;
    }

    public void setBucketPolicy(BucketPolicy bucketPolicy) {
        this.bucketPolicy = bucketPolicy;
    }

    private BucketPolicy bucketPolicy;

    public PutBuckePolicyRequest(String bucketName) {
        super.setBucketname(bucketName);
    }

    public PutBuckePolicyRequest(String bucketName, BucketPolicy bucketPolicy) {
        this(bucketName);
        this.bucketPolicy = bucketPolicy;
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("policy", "");

        String bucketPolicyBody = new Gson().toJson(bucketPolicy);
        this.addHeader(HttpHeaders.ContentType,"application/json");
        this.addHeader(HttpHeaders.ContentMD5, Md5Utils.md5AsBase64(bucketPolicyBody.getBytes()));
        this.addHeader(HttpHeaders.ContentLength, String.valueOf(bucketPolicyBody.getBytes().length));
        this.setRequestBody(new ByteArrayInputStream(bucketPolicyBody.getBytes()));


    }

    public void validateParams() {

        if (StringUtils.isBlank(this.getBucketname())) {
            throw new Ks3ClientException("bucket name is not correct");
        }
        if (this.bucketPolicy == null) {
            throw new Ks3ClientException("policy is not correct");
        }
        if (this.bucketPolicy.getStatement().size() == 0) {
            throw new Ks3ClientException("policy statement is not correct");
        }
    }

}

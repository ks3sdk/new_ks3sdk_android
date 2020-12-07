package com.ksyun.ks3.services.request;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.result.BucketQuota;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.StringUtils;
import com.ksyun.ks3.util.XmlWriter;

import java.io.ByteArrayInputStream;

public class PutBuckeQuotaRequest extends Ks3HttpRequest {

    private static final long serialVersionUID = 28505402331223772L;

    private BucketQuota bucketQuota;

    public BucketQuota getBucketQuota() {
        return bucketQuota;
    }

    public void setBucketQuota(BucketQuota bucketQuota) {
        this.bucketQuota = bucketQuota;
    }

    public PutBuckeQuotaRequest(String bucketName) {
        super.setBucketname(bucketName);
    }

    public PutBuckeQuotaRequest(String bucketName, BucketQuota bucketQuota) {
        this(bucketName);
        this.bucketQuota = bucketQuota;
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("quota", "");
        XmlWriter writer = new XmlWriter();
        writer.start("Quota");
        writer.start("StorageQuota").value(String.valueOf(this.bucketQuota.getStorageQuota())).end();
        writer.end();
        String xml = writer.toString();
        this.addHeader(HttpHeaders.ContentMD5, Md5Utils.md5AsBase64(xml.getBytes()));
        this.addHeader(HttpHeaders.ContentLength, String.valueOf(xml.length()));
        this.setRequestBody(new ByteArrayInputStream(xml.getBytes()));

    }

    public void validateParams() {
        if (StringUtils.isBlank(this.getBucketname())) {
            throw new Ks3ClientException("bucket name is not correct");
        }
        if (this.bucketQuota == null) {
            throw new Ks3ClientException("quota is not correct");
        }
    }

}

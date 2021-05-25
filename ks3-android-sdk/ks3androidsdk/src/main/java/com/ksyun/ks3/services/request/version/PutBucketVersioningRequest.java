package com.ksyun.ks3.services.request.version;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.XmlWriter;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.util.Asserts;

public class PutBucketVersioningRequest extends Ks3HttpRequest{

    private BucketVersioningConfiguration versioningConfiguration;

    public PutBucketVersioningRequest(BucketVersioningConfiguration versioningConfiguration) {
        this.versioningConfiguration = versioningConfiguration;
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("versioning", "");
        XmlWriter writer = new XmlWriter();
        writer.startWithNs("VersioningConfiguration");
        writer.start("Status");
        writer.value(versioningConfiguration.getStatus());
        writer.end();
        writer.end();
        String xml = writer.toString();
        this.addHeader(HttpHeaders.ContentMD5, Md5Utils.md5AsBase64(xml.getBytes()));
        this.addHeader(HttpHeaders.ContentLength,String.valueOf(xml.getBytes()));
        this.setRequestBody(new ByteArrayInputStream(xml.getBytes()));

    }

    @Override
    protected String validateParams() throws Ks3ClientException {
        Asserts.notNull(this.getBucketname(), "bucket == null");
        Asserts.notNull(this.versioningConfiguration, "versioningConfiguration == null");
        Asserts.notNull(this.versioningConfiguration.getStatus(), "versioningConfiguration.getStatus() == null");
        Asserts.check(this.versioningConfiguration.getStatus().equals(BucketVersioningConfiguration.OFF) ||
                        this.versioningConfiguration.getStatus().equals(BucketVersioningConfiguration.ENABLED) ||
                        this.versioningConfiguration.getStatus().equals(BucketVersioningConfiguration.SUSPENDED),
                "this.versioningConfiguration.getStatus() is illegal");
        return null;
    }
}

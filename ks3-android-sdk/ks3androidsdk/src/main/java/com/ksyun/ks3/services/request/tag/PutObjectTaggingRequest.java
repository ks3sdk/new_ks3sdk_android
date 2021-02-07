package com.ksyun.ks3.services.request.tag;

import android.util.Base64;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.transfer.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.services.request.MD5CalculateAble;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.XmlWriter;

import java.io.ByteArrayInputStream;
import java.util.List;

public class PutObjectTaggingRequest extends Ks3HttpObjectRequest implements
        MD5CalculateAble {
    private static final long serialVersionUID = 8398633676278496457L;

    public PutObjectTaggingRequest(String bucketName, String objectName, ObjectTagging objectTagging) {
        this.setBucketname(bucketName);
        this.setObjectkey(objectName);
        this.setTagging(objectTagging);
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("tagging", "");
        XmlWriter writer = new XmlWriter();
        writer.start("Tagging");
        writer.start("TagSet");
        List<ObjectTag> tags = this.getTagging().getTagSet();
        for (ObjectTag tag : tags) {
            writer.start("Tag");
            writer.start("Key").value(tag.getKey()).end();
            if (tag.getValue() != null) {
                writer.start("Value").value(tag.getValue()).end();
            }
            writer.end();
        }
        writer.end();
        String xml = writer.toString();
        this.addHeader(HttpHeaders.ContentMD5, Md5Utils.md5AsBase64(xml.getBytes()));
        this.addHeader(HttpHeaders.ContentLength, String.valueOf(xml.length()));
        this.setRequestBody(new ByteArrayInputStream(xml.getBytes()));
        this.setTagHeader();
    }

    public void validateParams() throws IllegalArgumentException {

        super.validateParams();
    }

    public String getMd5() {
        return Base64
                .encodeToString(
                        ((MD5DigestCalculatingInputStream) super.getRequestBody())
                                .getMd5Digest(), Base64.DEFAULT).trim();
    }

}

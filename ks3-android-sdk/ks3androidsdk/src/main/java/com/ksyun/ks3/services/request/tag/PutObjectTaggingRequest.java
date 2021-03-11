package com.ksyun.ks3.services.request.tag;

import android.util.Base64;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.transfer.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.services.request.MD5CalculateAble;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.StringUtils;
import com.ksyun.ks3.util.XmlWriter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator.between;
import static com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator.notCorrect;

public class PutObjectTaggingRequest extends Ks3HttpRequest implements
        MD5CalculateAble {
    private static final long serialVersionUID = 8398633676278496457L;

    public ObjectTagging getTagging() {
        return tagging;
    }

    public void setTagging(ObjectTagging tagging) {
        this.tagging = tagging;
    }

    private ObjectTagging tagging;
    public Pattern TAG_PATTERN = Pattern.compile("^[\\w\\-+=.:/][\\w\\-+=.:/\\s]*(?<!\\s)$");

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
        writer.end();
        String xml = writer.toString();
        this.addHeader(HttpHeaders.ContentMD5, Md5Utils.md5AsBase64(xml.getBytes()));
        this.addHeader(HttpHeaders.ContentLength, String.valueOf(xml.length()));
        this.setRequestBody(new ByteArrayInputStream(xml.getBytes()));
    }

    public void validateParams() throws IllegalArgumentException {

        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
        if (StringUtils.isBlank(this.getObjectkey())) {
            throw new Ks3ClientException("object can not be null");
        }
        if (this.getTagging() != null) {
            if (this.getTagging().getTagSet() == null || this.getTagging().getTagSet().size() == 0) {
                throw between("tagset", "0", "1", "10");
            }
            if (this.getTagging().getTagSet().size() > 10) {
                throw between("tagset", "" + this.getTagging().getTagSet().size(), "1", "10");
            }
            List<String> tagKeys = new ArrayList<String>();
            for (ObjectTag tag : this.getTagging().getTagSet()) {
                if (!TAG_PATTERN.matcher(tag.getKey()).matches()) {
                    throw notCorrect("key", tag.getKey(), "invalid key format");
                }

                if (tag.getValue() != null && !TAG_PATTERN.matcher(tag.getValue()).matches()) {
                    throw notCorrect("value", tag.getValue(), "invalid value format");
                }

                if (tag.getKey().getBytes().length > 128) {
                    throw notCorrect("key", tag.getKey(), "invalid length");
                }

                if (tag.getValue() != null && tag.getValue().getBytes().length > 256) {
                    throw notCorrect("value", tag.getValue(), "invalid length");
                }

                if (!tagKeys.contains(tag.getKey())) {
                    tagKeys.add(tag.getKey());
                } else {
                    throw notCorrect("key", tag.getKey(), "duplicated tagging key");
                }
            }
        }
    }

    public String getMd5() {
        return Base64
                .encodeToString(
                        ((MD5DigestCalculatingInputStream) super.getRequestBody())
                                .getMd5Digest(), Base64.DEFAULT).trim();
    }

}

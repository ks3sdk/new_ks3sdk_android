package com.ksyun.ks3.services.request.common;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.services.request.tag.ObjectTag;
import com.ksyun.ks3.services.request.tag.ObjectTagging;
import com.ksyun.ks3.util.StringUtils;
import com.ksyun.ks3.util.XmlWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator.between;
import static com.ksyun.ks3.util.ClientIllegalArgumentExceptionGenerator.notCorrect;

public class Ks3HttpObjectRequest extends Ks3HttpRequest {

    public ObjectTagging getTagging() {
        return tagging;
    }

    public void setTagging(ObjectTagging tagging) {
        this.tagging = tagging;
    }

    private ObjectTagging tagging;

    public Pattern TAG_PATTERN = Pattern.compile("^[\\w\\-+=.:/][\\w\\-+=.:/\\s]*(?<!\\s)$");

    private String versionId;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {
        // 添加 versionId
        if (!StringUtils.isBlank(versionId)) {
            this.addParams("versionId", versionId);
        }
    }

    protected void setTagHeader() {

        if (this.getTagging() != null && this.getTagging().getTagSet() != null && this.getTagging().getTagSet().size() > 0) {
            XmlWriter writer = new XmlWriter();
            writer.start("Tagging");
            writer.start("TagSet");
            List<ObjectTag> tags = this.getTagging().getTagSet();
            StringBuffer stringBuffer = new StringBuffer();
            for (ObjectTag tag : tags) {
                writer.start("Tag");
                writer.start("Key").value(tag.getKey()).end();
                if (tag.getValue() != null) {
                    writer.start("Value").value(tag.getValue()).end();
                    stringBuffer.append(tag.getKey() + "=" + tag.getValue() + "&");
                }
            }
            if (stringBuffer.length() > 0) {
                String xKssObjectTagStr = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
                this.addHeader(HttpHeaders.XKssObjectTag, xKssObjectTagStr);
            }
        }
    }

    @Override
    protected String validateParams() throws Ks3ClientException {

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
        return null;
    }
}

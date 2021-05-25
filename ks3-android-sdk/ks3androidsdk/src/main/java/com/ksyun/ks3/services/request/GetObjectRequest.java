package com.ksyun.ks3.services.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.StringUtils;

public class GetObjectRequest extends Ks3HttpObjectRequest {
    private static final long serialVersionUID = 5102418101375085675L;
    private String range = null;
    private List<String> matchingETagConstraints = new ArrayList<String>();
    private List<String> nonmatchingEtagConstraints = new ArrayList<String>();
    private Date unmodifiedSinceConstraint;
    private Date modifiedSinceConstraint;

    public GetObjectRequest(String bucket, String key) {
        setBucketname(bucket);
        setObjectkey(key);
    }

    /**
     * @param bucket
     * @param key
     * @param versionId
     */
    public GetObjectRequest(String bucket, String key, String versionId) {
        setBucketname(bucket);
        setObjectkey(key);
        setVersionId(versionId);
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {
        this.setHttpMethod(HttpMethod.GET);
        super.setupRequest();
        if (!StringUtils.isBlank(range))
            this.addHeader(HttpHeaders.Range, range);
        if (matchingETagConstraints.size() > 0)
            this.addHeader(HttpHeaders.IfMatch,
                    StringUtils.join(matchingETagConstraints, ","));
        if (nonmatchingEtagConstraints.size() > 0)
            this.addHeader(HttpHeaders.IfNoneMatch,
                    StringUtils.join(nonmatchingEtagConstraints, ","));
        if (this.unmodifiedSinceConstraint != null)
            this.addHeader(HttpHeaders.IfUnmodifiedSince,
                    this.unmodifiedSinceConstraint.toGMTString());
        if (this.modifiedSinceConstraint != null)
            this.addHeader(HttpHeaders.IfModifiedSince,
                    this.modifiedSinceConstraint.toGMTString());
    }

    @Override
    protected String validateParams() throws Ks3ClientException {
        if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
            throw new Ks3ClientException("bucket name is not correct");
        if (StringUtils.isBlank(this.getObjectkey()))
            throw new Ks3ClientException("object key can not be null");
        if (!StringUtils.isBlank(range)) {
            if (!range.startsWith("bytes="))
                throw new Ks3ClientException(
                        "Range should be start with 'bytes='");
        }
        return null;
    }

    public String getRange() {
        return range;
    }

    public void setRange(long start, long end) {
        this.range = "bytes=" + start + "-" + end;
    }

    public List<String> getMatchingETagConstraints() {
        return matchingETagConstraints;
    }

    public void setMatchingETagConstraints(List<String> matchingETagConstraints) {
        this.matchingETagConstraints = matchingETagConstraints;
    }

    public List<String> getNonmatchingEtagConstraints() {
        return nonmatchingEtagConstraints;
    }

    public void setNonmatchingEtagConstraints(
            List<String> nonmatchingEtagConstraints) {
        this.nonmatchingEtagConstraints = nonmatchingEtagConstraints;
    }

    public Date getUnmodifiedSinceConstraint() {
        return unmodifiedSinceConstraint;
    }

    public void setUnmodifiedSinceConstraint(Date unmodifiedSinceConstraint) {
        this.unmodifiedSinceConstraint = unmodifiedSinceConstraint;
    }

    public Date getModifiedSinceConstraint() {
        return modifiedSinceConstraint;
    }


}

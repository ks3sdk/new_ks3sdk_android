package com.ksyun.ks3.services.request;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.model.result.ReplicationRule;
import com.ksyun.ks3.util.Md5Utils;
import com.ksyun.ks3.util.StringUtils;
import com.ksyun.ks3.util.XmlWriter;

import java.io.ByteArrayInputStream;

public class PutBucketReplicationConfigRequest extends Ks3HttpRequest {

    private static final long serialVersionUID = 28505512339783772L;
    private ReplicationRule replicationRule;


    public PutBucketReplicationConfigRequest(String bucketName) {
        super.setBucketname(bucketName);
    }

    public PutBucketReplicationConfigRequest(String bucketName, ReplicationRule replicationRule) {
        this(bucketName);
        this.replicationRule = replicationRule;
    }

    public ReplicationRule getReplicationRule() {
        return replicationRule;
    }

    public void setReplicationRule(ReplicationRule replicationRule) {
        this.replicationRule = replicationRule;
    }

    @Override
    protected void setupRequest() throws Ks3ClientException {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("crr", "");
        XmlWriter writer = new XmlWriter();
        writer.startWithNs("RegionReplicate");
        for (String prefix : replicationRule.getPrefixList()) {
            writer.start("prefix").value(prefix).end();
        }
        if (replicationRule.isDeleteMarkerStatus()) {
            writer.start("DeleteMarkerStatus").value(ReplicationRule.ENABLED).end();
        } else {
            writer.start("DeleteMarkerStatus").value(ReplicationRule.DISABLED).end();
        }
        writer.start("targetBucket").value(replicationRule.getTargetBucket()).end();
        writer.end();
        String xml = writer.toString();
        this.addHeader(HttpHeaders.ContentMD5, Md5Utils.md5AsBase64(xml.getBytes()));
        this.addHeader(HttpHeaders.ContentLength, String.valueOf(xml.length()));
        this.setRequestBody(new ByteArrayInputStream(xml.getBytes()));

    }

    public void validateParams() {
        if (StringUtils.isBlank(this.getBucketname()))
            throw new Ks3ClientException("bucket name is not correct");
        if (this.replicationRule == null)
            throw new Ks3ClientException("replicationRule is not correct");
        if (this.replicationRule.getTargetBucket() == null)
            throw new Ks3ClientException("targetBucket");
        if (this.replicationRule.getPrefixList().size() > 5)
            throw new Ks3ClientException("prefixList too many");
    }


}
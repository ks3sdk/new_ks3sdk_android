
package com.ksyun.ks3.services.request.object;

public class PutObjectFetchResult {

    private int status;
    private String key;
    private String bucket;
    private long objectSize;
    private String sourceUrl;
    private String requestId;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setObjectSize(long objectSize) {
        this.objectSize = objectSize;
    }

    public long getObjectSize() {
        return objectSize;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "PutObjectFetchResult{" +
                "status=" + status +
                ", key='" + key + '\'' +
                ", bucket='" + bucket + '\'' +
                ", objectSize=" + objectSize +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
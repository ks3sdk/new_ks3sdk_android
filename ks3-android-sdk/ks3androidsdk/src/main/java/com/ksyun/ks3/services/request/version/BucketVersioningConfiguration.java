package com.ksyun.ks3.services.request.version;

public class BucketVersioningConfiguration {
    public static final String OFF = "Off";
    public static final String SUSPENDED = "Suspended";
    public static final String ENABLED = "Enabled";
    private String status;

    public BucketVersioningConfiguration() {
        this.setStatus("Off");
    }

    public BucketVersioningConfiguration(String status) {
        this.setStatus(status);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BucketVersioningConfiguration withStatus(String status) {
        this.setStatus(status);
        return this;
    }

    public String toString() {
        return "BucketVersioningConfiguration(status=" + this.getStatus() + ")";
    }
}

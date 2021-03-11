package com.ksyun.ks3.model;

public class PostObjectFormFields {
    private String policy;
    private String kssAccessKeyId;
    private String signature;
    public String getPolicy() {
        return policy;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
    }
    public String getKssAccessKeyId() {
        return kssAccessKeyId;
    }
    public void setKssAccessKeyId(String kssAccessKeyId) {
        this.kssAccessKeyId = kssAccessKeyId;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
}

package com.ksyun.ks3.model.result.policy;

public enum BucketPolicyActionCode {

    /**
     * object
     */
    AbortMultipartUpload("ks3:AbortMultipartUpload"),
    DeleteObject("ks3:DeleteObject"),
    GetObject("ks3:GetObject"),
    GetObjectAcl("ks3:GetObjectAcl"),
    ListBucketMultipartUploads("ks3:ListBucketMultipartUploads"),
    ListMultipartUploadParts("ks3:ListMultipartUploadParts"),
    PutObject("ks3:PutObject"),
    PutObjectAcl("ks3:PutObjectAcl"),
    PostObjectRestore("ks3:PostObjectRestore"),
    /**
     * bucket
     */

    GetBucketAcl("ks3:GetBucketAcl"),
    GetBucketCORS("ks3:GetBucketCORS"),
    ListBucket("ks3:ListBucket"),
    PutBucketAcl("ks3:PutBucketAcl"),
    PutBucketCORS("ks3:PutBucketCORS"),
    DeleteBucket("ks3:DeleteBucket");

    private String value;

    BucketPolicyActionCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}

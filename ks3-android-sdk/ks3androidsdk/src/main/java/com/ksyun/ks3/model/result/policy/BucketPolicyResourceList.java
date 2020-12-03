package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;

public class BucketPolicyResourceList extends ArrayList<String> {

    private final static String prefix = "krn:ksc:ks3:::";

    public void addBucket(String bucketName) {
        super.add(prefix+bucketName);
        super.add(prefix+bucketName+"/*");
    }

    public boolean addRecource(String resource) {
        resource = prefix + resource;
        return super.add(resource);
    }
}

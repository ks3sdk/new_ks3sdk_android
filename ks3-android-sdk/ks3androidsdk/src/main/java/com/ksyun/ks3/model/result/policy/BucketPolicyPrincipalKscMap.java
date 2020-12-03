package com.ksyun.ks3.model.result.policy;

import java.util.HashMap;

public class BucketPolicyPrincipalKscMap extends HashMap {

    /**
     * 资源使用者
     */
    private BucketPolicyPrincipalList KSC = new BucketPolicyPrincipalList();

    private final static String prefix = "KSC";

    public BucketPolicyPrincipalKscMap() {
        this.put(prefix, this.getRequestHeader());
    }

    public BucketPolicyPrincipalList getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(BucketPolicyPrincipalList requestHeader) {
        this.requestHeader = requestHeader;
    }

    private BucketPolicyPrincipalList requestHeader = new BucketPolicyPrincipalList();

}

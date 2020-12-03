package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BucketPolicyCondition extends HashMap {

    private List<String> IpAddress;
    private List<String> NotIpAddress;


    private BucketConditionHeaderRule StringNotEquals;
    private BucketConditionHeaderRule StringEquals;
    private BucketConditionHeaderRule StringEqualsIgnoreCase;
    private BucketConditionHeaderRule StringNotEqualsIgnoreCase;
    private BucketConditionHeaderRule StringLike;
    private BucketConditionHeaderRule StringNotLike;


    public List<String> getIpAddress() {
        if (IpAddress == null){
            IpAddress = new ArrayList<>();
        }
        return IpAddress;
    }

    public List<String> getNotIpAddress() {
        if (NotIpAddress == null){
            NotIpAddress = new ArrayList<>();
        }
        return NotIpAddress;
    }

    public void setNotIpAddress(List<String> NotIpAddress) {
        this.NotIpAddress = NotIpAddress;
    }

    public BucketConditionHeaderRule getStringNotEquals() {
        if (StringNotEquals == null){
            StringNotEquals = new BucketConditionHeaderRule();
            this.put("StringNotEquals",StringNotEquals);
        }
        return StringNotEquals;
    }

    public void setStringNotEquals(BucketConditionHeaderRule stringNotEquals) {
        StringNotEquals = stringNotEquals;
    }

    public BucketConditionHeaderRule getStringEquals() {
        if (StringEquals == null){
            StringEquals = new BucketConditionHeaderRule();
            this.put("StringEquals",StringEquals);
        }
        return StringEquals;
    }

    public void setStringEquals(BucketConditionHeaderRule stringEquals) {
        StringEquals = stringEquals;
    }

    public BucketConditionHeaderRule getStringEqualsIgnoreCase() {
        if (StringEqualsIgnoreCase == null){
            StringEqualsIgnoreCase = new BucketConditionHeaderRule();
            this.put("StringEqualsIgnoreCase",StringEqualsIgnoreCase);
        }
        return StringEqualsIgnoreCase;
    }

    public void setStringEqualsIgnoreCase(BucketConditionHeaderRule stringEqualsIgnoreCase) {
        StringEqualsIgnoreCase = stringEqualsIgnoreCase;
    }

    public BucketConditionHeaderRule getStringNotEqualsIgnoreCase() {
        if (StringNotEqualsIgnoreCase == null){
            StringNotEqualsIgnoreCase = new BucketConditionHeaderRule();
            this.put("StringNotEqualsIgnoreCase",StringNotEqualsIgnoreCase);
        }
        return StringNotEqualsIgnoreCase;
    }

    public void setStringNotEqualsIgnoreCase(BucketConditionHeaderRule stringNotEqualsIgnoreCase) {
        StringNotEqualsIgnoreCase = stringNotEqualsIgnoreCase;
    }

    public BucketConditionHeaderRule getStringLike() {
        if (StringLike == null){
            StringLike = new BucketConditionHeaderRule();
            this.put("StringLike",StringLike);
        }
        return StringLike;
    }

    public void setStringLike(BucketConditionHeaderRule stringLike) {
        StringLike = stringLike;
    }

    public BucketConditionHeaderRule getStringNotLike() {
        if (StringNotLike == null){
            StringNotLike = new BucketConditionHeaderRule();
            this.put("StringNotLike",StringNotLike);
        }
        return StringNotLike;
    }

    public void setStringNotLike(BucketConditionHeaderRule stringNotLike) {
        StringNotLike = stringNotLike;
    }
    /**
     * 添加sourceIp
     *
     * @param sourceIp
     * @return
     */
    public void addSourceIp(String sourceIp, boolean isIntercept) {
        if (isIntercept) {
            this.getNotIpAddress().add(sourceIp);
        } else {
            this.getIpAddress().add(sourceIp);
        }
    }

    /**
     * 添加sourceHeader
     *
     * @param content
     * @param rule
     * @return
     */
    public void addSourceHeader(String content, BucketPolicyConditionRule rule) {

        switch (rule) {
            case StringEquals:
                this.getStringEquals().getRequestHeader().add(content);
                break;
            case StringNotEquals:
                this.getStringNotEquals().getRequestHeader().add(content);
                break;
            case StringEqualsIgnoreCase:
                this.getStringEqualsIgnoreCase().getRequestHeader().add(content);
                break;
            case StringNotEqualsIgnoreCase:
                this.getStringNotEqualsIgnoreCase().getRequestHeader().add(content);
                break;
            case StringLike:
                this.getStringLike().getRequestHeader().add(content);
                break;
            case StringNotLike:
                this.getStringNotLike().getRequestHeader().add(content);
                break;
        }
    }
    /**
     * 添加sourceSubnetID
     *
     * @param subnetID
     * @param rule
     * @return
     */
    public void addSourceBySubnetIDAndAccountId(String subnetID,String accountId,BucketPolicyConditionRule rule) {

        switch (rule) {
            case StringEquals:
                this.getStringEquals().getSubnetID().add(new BucketPolicySubnetIDMap(accountId,subnetID));
                break;
            case StringNotEquals:
                this.getStringNotEquals().getSubnetID().add(new BucketPolicySubnetIDMap(accountId,subnetID));
                break;
            case StringEqualsIgnoreCase:
                this.getStringEqualsIgnoreCase().getSubnetID().add(new BucketPolicySubnetIDMap(accountId,subnetID));
                break;
            case StringNotEqualsIgnoreCase:
                this.getStringNotEqualsIgnoreCase().getSubnetID().add(new BucketPolicySubnetIDMap(accountId,subnetID));
                break;
            case StringLike:
                this.getStringLike().getSubnetID().add(new BucketPolicySubnetIDMap(accountId,subnetID));
                break;
            case StringNotLike:
                this.getStringNotLike().getSubnetID().add(new BucketPolicySubnetIDMap(accountId,subnetID));
                break;
        }
    }


}

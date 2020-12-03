package com.ksyun.ks3.model.result.policy;

public enum BucketPolicyConditionRule {

//    /**
//     * 规则类型
//     */
//    RequestHeader("ksc:RequestHeader"),
//    SubnetID("ks3:SubnetID"),
//    SourceIp("ks3:SourceIp"),


    /**
     * 规则类型
     */
    StringEquals("StringEquals"),
    StringNotEquals("StringNotEquals"),
    StringEqualsIgnoreCase("StringEqualsIgnoreCase"),
    StringNotEqualsIgnoreCase("StringNotEqualsIgnoreCase"),
    StringLike("StringLike"),
    StringNotLike("StringNotLike");

    private String value;

    BucketPolicyConditionRule(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}

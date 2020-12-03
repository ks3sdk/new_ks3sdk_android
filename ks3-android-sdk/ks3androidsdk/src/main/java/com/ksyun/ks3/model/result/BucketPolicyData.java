package com.ksyun.ks3.model.result;

import com.ksyun.ks3.model.result.policy.BucketPolicyRule;

import java.util.ArrayList;
import java.util.List;

public class BucketPolicyData {

    /**
     * 版本
     */
   // private String Version = "2005-11-01";

    public List<BucketPolicyRule> getStatement() {
        return Statement;
    }

    public void setStatement(List<BucketPolicyRule> statement) {
        Statement = statement;
    }
    /**
     * 桶策略
     */
    private List<BucketPolicyRule> Statement = new ArrayList<>();


}


package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;
import java.util.List;

public class BucketPolicyRule {


    /**
     * 是否允許
     */
    private String Effect;


    /**
     * 接口名称
     */
    private List<String> Action;

    /**
     * 资源地址
     */
    private BucketPolicyResourceList Resource;

    /**
     * 资源使用者
     */
    private BucketPolicyPrincipalKscMap Principal;


    /**
     * 规则
     */
    private BucketPolicyCondition Condition;


    public String getEffect() {
        return Effect;
    }

    public BucketPolicyRule setEffect(String effect) {
        Effect = effect;
        return this;
    }
    /**
     * 添加action
     *
     * @return
     */
    public BucketPolicyRule addAction(BucketPolicyActionCode code) {
        Action.add(code.toString());
        return this;
    }

    /**
     * 添加action
     *
     * @return
     */
    public BucketPolicyRule addAllAction() {
        this.getAction().add("ks3:*");
        return this;
    }

    /**
     * 添加recource
     *
     * @return
     */
    public BucketPolicyRule addResource(String resource) {
        this.getResource().addRecource(resource);
        return this;
    }

    /**
     * 添加recource
     *
     * @return
     */
    public BucketPolicyRule addBucketResource(String bucketResource) {
        this.getResource().addBucket(bucketResource);
        return this;
    }

    /**
     * 添加Principal
     *
     * @param accountId
     * @return
     */
    public BucketPolicyRule addPrincipalByAccountId(String accountId) {
        this.getPrincipal().getRequestHeader().addAccountId(accountId);
        return this;
    }

    /**
     * 添加Principal
     *
     * @param accountId
     * @param userName
     * @return
     */
    public BucketPolicyRule addPrincipalByAccountIdAndUserName(String accountId, String userName) {
        this.getPrincipal().getRequestHeader().addAccountIdAndUserName(accountId, userName);
        return this;
    }

    /**
     * 添加Principal
     *
     * @param accountId
     * @param roleName
     * @return
     */
    public BucketPolicyRule addPrincipalByAccountIdAndRoleName(String accountId, String roleName) {
        this.getPrincipal().getRequestHeader().addAccountIdAndRoleName(accountId, roleName);
        return this;
    }

    /**
     * 添加Condition-ip
     *
     * @param content
     * @param isIntercept
     * @return
     */
    public BucketPolicyRule addConditionSouceIp(String content, boolean isIntercept) {

        this.getCondition().addSourceIp(content, isIntercept);
        return this;
    }

    /**
     * 添加Condition-header
     *
     * @param content
     * @param rule
     * @return
     */
    public BucketPolicyRule addSourceHeader(String content, BucketPolicyConditionRule rule) {
        this.getCondition().addSourceHeader(content, rule);
        return this;
    }
    /**
     * 添加Condition-SubnetID
     *
     * @param subnetID
     * @param accountId
     * @param rule
     * @return
     */
    public BucketPolicyRule addSourceSubnet(String subnetID,String accountId,BucketPolicyConditionRule rule) {
        this.getCondition().addSourceBySubnetIDAndAccountId(subnetID,accountId,rule);
        return this;
    }

    /**
     * 添加规则
     *
     * @return
     */
    public static BucketPolicyRule statement() {
        BucketPolicyRule policyStatement = new BucketPolicyRule();
        return policyStatement;
    }


    public BucketPolicyCondition getCondition() {
        if (Condition == null) {
            Condition = new BucketPolicyCondition();
        }
        return Condition;
    }

    public List<String> getAction() {
        if (Action == null) {
            Action = new ArrayList<>();
        }
        return Action;
    }

    public BucketPolicyResourceList getResource() {
        if (Resource == null) {
            Resource = new BucketPolicyResourceList();
        }
        return Resource;
    }

    public BucketPolicyPrincipalKscMap getPrincipal() {
        if (Principal == null) {
            Principal = new BucketPolicyPrincipalKscMap();
        }
        return Principal;
    }
}

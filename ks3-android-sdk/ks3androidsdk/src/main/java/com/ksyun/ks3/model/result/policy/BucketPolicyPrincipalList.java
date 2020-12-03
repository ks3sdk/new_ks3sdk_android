package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;

public class BucketPolicyPrincipalList  extends ArrayList<String> {

    private  final static String prefix = "krn:ksc:iam::";

    @Override
    public boolean add(String o) {
        o = prefix + o;
        return super.add(o);
    }

    /**
     * 添加主账户
     * @param account
     * @return
     */
    public boolean addAccountId(String account) {
        account = prefix + account+":root";
        return super.add(account);
    }

    /**
     * 通过用户名添加子账户
     * @param account
     * @param userName
     * @return
     */
    public boolean addAccountIdAndUserName(String account, String userName) {

        String principal = prefix + account + ":user/" + userName;
        return super.add(principal);
    }

    /**
     * 通过角色添加子账户
     * @param account
     * @param roleName
     * @return
     */
    public boolean addAccountIdAndRoleName(String account, String roleName) {

        String principal = prefix + account + ":user/" + roleName;
        return super.add(principal);
    }
}

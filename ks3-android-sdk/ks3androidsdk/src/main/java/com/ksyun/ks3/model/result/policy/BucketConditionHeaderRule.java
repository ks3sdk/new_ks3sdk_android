/**
 * Copyright 2020 bejson.com
 */
package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * date: 2020-12-02 12:1:12
 *
 * @author cqc

 */
public class BucketConditionHeaderRule extends HashMap {


    private List<String> requestHeader;
    private List<BucketPolicySubnetIDMap> SubnetID;
    private List<String> ipList;

    public List<String> getRequestHeader() {
        if (requestHeader == null) {
            requestHeader = new ArrayList<>();
            this.put("ksc:RequestHeader", requestHeader);
        }
        return requestHeader;
    }

    public List<BucketPolicySubnetIDMap> getSubnetID() {
        if (SubnetID == null) {
            SubnetID = new ArrayList<>();
            this.put("ksc:SubnetID", SubnetID);
        }
        return SubnetID;
    }

    public List<String> getIpList() {
        if (ipList == null) {
            ipList = new ArrayList<>();
            this.put("ksc:SourceIp", ipList);
        }
        return ipList;
    }

}
package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;
import java.util.HashMap;

public class BucketPolicySubnetIDMap extends HashMap {


    /**
     * 添加subNetID
     * @param accountID
     * @param subNetID
     */
    public BucketPolicySubnetIDMap(String accountID,String subNetID) {
        getRequestSubNetIDs().add(subNetID);
        this.put("AccountID", accountID);
    }

    public ArrayList getRequestSubNetIDs() {
        if (requestSubNetIDs == null){
            requestSubNetIDs = new ArrayList<>();
            this.put("SubNetIDs", requestSubNetIDs);
        }
        return requestSubNetIDs;
    }
    private ArrayList requestSubNetIDs;

}

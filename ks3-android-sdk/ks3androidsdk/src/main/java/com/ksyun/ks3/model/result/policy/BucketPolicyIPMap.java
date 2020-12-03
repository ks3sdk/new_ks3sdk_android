package com.ksyun.ks3.model.result.policy;

import java.util.ArrayList;
import java.util.HashMap;

public class BucketPolicyIPMap extends HashMap {


    public BucketPolicyIPMap(String ip) {
        getRequestSubNetIDs().add(ip);
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

package com.ksyun.ks3.model.result;

import com.ksyun.ks3.services.handler.Ks3HttpResponceHandler;

import java.util.ArrayList;
import java.util.List;

public class ReplicationRule extends Ks3HttpResponceHandler {

    private List<String> prefixList = new ArrayList<String>();
    private String targetBucket;
    private boolean deleteMarkerStatus = false;

    /**
     * Constant for an enabled rule.
     */
    public static final String ENABLED = "Enabled";

    /**
     * Constant for a disabled rule.
     */
    public static final String DISABLED = "Disabled";

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
    }

    public String getTargetBucket() {
        return targetBucket;
    }

    public boolean isDeleteMarkerStatus() {
        return deleteMarkerStatus;
    }

    public void setDeleteMarkerStatus(boolean deleteMarkerStatus) {
        this.deleteMarkerStatus = deleteMarkerStatus;
    }

    public void setTargetBucket(String targetBucket) {
        this.targetBucket = targetBucket;
    }


}


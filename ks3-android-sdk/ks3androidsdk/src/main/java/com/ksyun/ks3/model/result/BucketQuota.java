package com.ksyun.ks3.model.result;

import com.ksyun.ks3.services.handler.Ks3HttpResponceHandler;

public class BucketQuota extends Ks3HttpResponceHandler {

    /**
     * 桶配额度
     */
    private long StorageQuota;

    public long getStorageQuota() {
        return StorageQuota;
    }

    public void setStorageQuota(long storageQuota) {
        StorageQuota = storageQuota;
    }


    public BucketQuota(long storageQuota) {
        StorageQuota = storageQuota;
    }
    public BucketQuota() {
    }
}

package com.ksyun.ks3.model;

import java.util.ArrayList;
import java.util.List;

public class ObjecVersiontListing {
    private List<Ks3ObjectSummary> objectSummaries = new ArrayList<Ks3ObjectSummary>();
    private List<String> commonPrefixes = new ArrayList<String>();
    private String bucketName;
    private boolean isTruncated;
    private String prefix;
    private String marker;
    private int maxKeys;
    private String delimiter;

    private String nextKeyMarker;
    private String nextVersionIdMarker;
    /**
     * 即游标，将列出排在游标之后的object
     */
    private String keymarker;
    private String versionIdMarker;
    @Override
    public String toString() {
        return "ObjectListing[bucket=" + this.bucketName + ";nextKeyMarker="
                + this.nextKeyMarker + ";isTruncated=" + this.isTruncated
                + ";prefix=" + this.prefix + ";marker=" + this.marker
                + ";maxKeys=" + this.maxKeys + ";delimiter=" + this.delimiter
                + ";commonPrefixs=" + this.commonPrefixes + ";objectSummaries="
                + this.objectSummaries
                + ";keymarker=" + this.keymarker
                + ";versionIdMarker=" + this.versionIdMarker
                + ";nextVersionIdMarker=" + this.nextVersionIdMarker
                + "]";
    }

    public List<Ks3ObjectSummary> getObjectSummaries() {
        return objectSummaries;
    }

    public void setObjectSummaries(List<Ks3ObjectSummary> objs) {
        this.objectSummaries = objs;
    }

    public List<String> getCommonPrefixes() {
        return commonPrefixes;
    }

    public void setCommonPrefixes(List<String> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean isTruncated) {
        this.isTruncated = isTruncated;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public void setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
    }

    public String getNextVersionIdMarker() {
        return nextVersionIdMarker;
    }

    public void setNextVersionIdMarker(String nextVersionIdMarker) {
        this.nextVersionIdMarker = nextVersionIdMarker;
    }

    public String getKeymarker() {
        return keymarker;
    }

    public void setKeymarker(String keymarker) {
        this.keymarker = keymarker;
    }

    public String getVersionIdMarker() {
        return versionIdMarker;
    }

    public void setVersionIdMarker(String versionIdMarker) {
        this.versionIdMarker = versionIdMarker;
    }
}

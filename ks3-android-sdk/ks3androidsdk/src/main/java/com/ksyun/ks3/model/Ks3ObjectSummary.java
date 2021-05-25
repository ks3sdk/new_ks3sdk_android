package com.ksyun.ks3.model;

import java.util.Date;

public class Ks3ObjectSummary {
	protected String bucketName;
	protected String key;
	protected String eTag;
	protected long size;
	protected Date lastModified;
	protected String storageClass;
	protected Owner owner;


	public String toString() {
		return "Ks3ObjectSummary[bucket=" + this.bucketName + ";key="
				+ this.key + ";eTag=" + this.eTag + ";size=" + this.size
				+ ";lastModified=" + this.lastModified + ";storgeClass="
				+ this.storageClass + ";owner=" + this.owner
				+ ";versionId=" + this.versionId
				+ ";isLatest=" + this.isLatest
				+ ";isDeleteMarker=" + this.isDeleteMarker
				+ "]";
	}
	/**
	 * 版本号
	 */
	protected String versionId;
	/**
	 * 是否是最新的
	 */
	protected boolean isLatest = true;
	/**
	 * 是否是 deleteMarker
	 */
	protected boolean isDeleteMarker;

	public boolean isDeleteMarker() {
		return isDeleteMarker;
	}

	public void setDeleteMarker(boolean deleteMarker) {
		isDeleteMarker = deleteMarker;
	}

	public boolean isLatest() {
		return isLatest;
	}

	public void setLatest(boolean latest) {
		isLatest = latest;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getStorageClass() {
		return storageClass;
	}

	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}
}

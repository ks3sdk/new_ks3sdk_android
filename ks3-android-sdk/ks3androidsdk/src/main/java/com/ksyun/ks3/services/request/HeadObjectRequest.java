package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.StringUtils;

public class HeadObjectRequest extends Ks3HttpObjectRequest {

	private static final long serialVersionUID = 3060892869127898914L;

	public HeadObjectRequest(String bucketname, String objectkey) {
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
	}
	public HeadObjectRequest(String bucketname, String objectkey,String versionId) {
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.setVersionId(versionId);
	}
	@Override
	protected void setupRequest() throws Ks3ClientException {
		this.setHttpMethod(HttpMethod.HEAD);
		super.setupRequest();
	}

	@Override
	protected String validateParams() throws Ks3ClientException {
		if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
			throw new Ks3ClientException("bucket name is not correct");
		if (StringUtils.isBlank(this.getObjectkey()))
			throw new Ks3ClientException("object key can not be null");
		return null;
	}

}

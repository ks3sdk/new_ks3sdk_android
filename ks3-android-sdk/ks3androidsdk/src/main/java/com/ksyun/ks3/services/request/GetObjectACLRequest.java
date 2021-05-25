package com.ksyun.ks3.services.request;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.StringUtils;

public class GetObjectACLRequest extends Ks3HttpObjectRequest {
	private static final long serialVersionUID = 2850575739783772L;

	@Override
	protected void setupRequest() throws Ks3ClientException {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("acl", "");
		super.setupRequest();
	}

	@Override
	protected String validateParams() throws Ks3ClientException {
		if (StringUtils.validateBucketName(this.getBucketname()) == null)
			throw new Ks3ClientException("bucket name is not correct");

		if (StringUtils.isBlank(this.getObjectkey())) {
			throw new Ks3ClientException("object key can not be null");
		}
        return null;
    }

	public GetObjectACLRequest(String bucketName, String objectKey) {
		setBucketname(bucketName);
		setObjectkey(objectKey);
	}
	public GetObjectACLRequest(String bucketName, String objectKey,String versionId) {
		setBucketname(bucketName);
		setObjectkey(objectKey);
		setVersionId(versionId);
	}
}

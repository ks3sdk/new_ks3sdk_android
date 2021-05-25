package com.ksyun.ks3.services.request;

import com.ksyun.ks3.auth.ValidateUtil;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.common.Ks3HttpObjectRequest;
import com.ksyun.ks3.util.StringUtils;

public class DeleteObjectRequest extends Ks3HttpObjectRequest {
	private static final long serialVersionUID = 5383705835021414599L;

	@Override
	protected void setupRequest() throws Ks3ClientException {
		this.setHttpMethod(HttpMethod.DELETE);
	}

	@Override
	protected String validateParams() throws Ks3ClientException {
		if (ValidateUtil.validateBucketName(this.getBucketname()) == null)
			throw new Ks3ClientException("bucket name is not correct");
		if (StringUtils.isBlank(this.getObjectkey()))
			throw new Ks3ClientException("object key can not be null");
		return null;
	}

	public DeleteObjectRequest(String bucketname, String key) {
		setBucketname(bucketname);
		setObjectkey(key);
	}
	public DeleteObjectRequest(String bucketname, String key, String versionId) {
		setBucketname(bucketname);
		setObjectkey(key);
		setVersionId(versionId);
	}
}

package com.ksyun.ks3.services.request.adp;


import com.ksyun.ks3.util.StringUtils;

/**
 * 云数据处理操作
 */
public class Adp {
	/**
	 * 处理命令，详见KS3 API文档，数据处理
	 */
	private String command;
	/**
	 * 数据处理成功后存储的bucket,如果不提供的话将会存在原数据的bucket下。
	 */
	private String bucket;
	/**
	 * 数据处理成功后存储的key,如果不提供的话将会使用随机的key。
	 */
	private String key;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String toString()
	{
		return StringUtils.object2string(this);
	}
}

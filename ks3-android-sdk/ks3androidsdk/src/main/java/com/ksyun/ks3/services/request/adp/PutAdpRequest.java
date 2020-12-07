package com.ksyun.ks3.services.request.adp;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpHeaders;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.util.HttpUtils;
import com.ksyun.ks3.util.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 添加数据处理任务
 */
public class PutAdpRequest extends Ks3HttpRequest {

	/**
	 * 要进行的处理任务
	 */
	private List<Adp> adps = new ArrayList<Adp>();
	/**
	 * 数据处理任务完成后通知的url
	 */
	private String notifyURL;
	/**
	 * 
	 * @param bucketName 要处理的数据所在bucket
	 * @param key 要处理的数据的key
	 */
	public PutAdpRequest(String bucketName,String key){
		setBucketname(bucketName);
		setObjectkey(key);
	}
	/**
	 * 
	 * @param bucketName 要处理的数据所在bucket
	 * @param key 要处理的数据的key
	 * @param adps 数据处理指令
	 */
	public PutAdpRequest(String bucketName,String key,List<Adp> adps){
		this(bucketName,key);
		this.setAdps(adps);
	}

	@Override
	protected void setupRequest() throws Ks3ClientException {
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("adp", "");
		this.addHeader(HttpHeaders.AsynchronousProcessingList, URLEncoder.encode(HttpUtils.convertAdps2String(adps)));
		if(!StringUtils.isBlank(notifyURL))
			this.addHeader(HttpHeaders.NotifyURL, HttpUtils.urlEncode(notifyURL,false));
	}

	@Override
	public void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new Ks3ClientException("bucketname");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new Ks3ClientException("objectkey");
		if(adps==null){
			throw new Ks3ClientException("adps");
		}else{
			for(Adp adp : adps){
				if(StringUtils.isBlank(adp.getCommand())){
					throw new Ks3ClientException("adps.command");
				}
			}
		}
		if(StringUtils.isBlank(notifyURL))
			throw new Ks3ClientException("notifyURL");
	}
	public List<Adp> getAdps() {
		return adps;
	}

	public void setAdps(List<Adp> adps) {
		this.adps = adps;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}


}

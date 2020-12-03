package com.ksyun.ks3.services.request;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.util.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午5:08:36
 * 
 * @description 查询数据处理任务的状态
 **/
public class GetAdpRequest extends Ks3HttpRequest {

	/**
	 * 由putpfop，postobject，putobject，complete_mutipart_upload返回的taskid
	 */
	private String taskid;
	public GetAdpRequest(String taskid){
		this.taskid = taskid;
	}

	@Override
	protected void setupRequest() throws Ks3ClientException {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("queryadp", "");
	}

	@Override
	public void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.taskid))
			throw new Ks3ClientException("taskid");
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


}

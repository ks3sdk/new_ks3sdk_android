package com.ksyun.ks3.services.request.adp;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.model.HttpMethod;
import com.ksyun.ks3.services.request.Ks3HttpRequest;
import com.ksyun.ks3.util.StringUtils;

/**
 * 查询数据处理任务的状态
 */
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
		this.setObjectkey(taskid);
	}

	@Override
	public String validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.taskid))
			throw new Ks3ClientException("taskid");
        return null;
    }
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


}

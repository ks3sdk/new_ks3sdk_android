package com.ksyun.ks3.services.request.adp;

import com.ksyun.ks3.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午5:44:53
 * 
 * @description 查询数据处理任务返回结果{@link AdpTask}中单条命令的具体处理结果
 **/
public class AdpInfo {
	/**
	 * 数据处理命令
	 */
	private String command;
	/**
	 * 是否处理成功
	 */
	private boolean success;
	/**
	 * 处理信息信息
	 */
	private String desc;
	/**
	 * 数据处理完成后新的数据的key
	 */
	private List<String> keys= new ArrayList<String>();
	
	public String toString()
	{
		return StringUtils.object2string(this);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	
}

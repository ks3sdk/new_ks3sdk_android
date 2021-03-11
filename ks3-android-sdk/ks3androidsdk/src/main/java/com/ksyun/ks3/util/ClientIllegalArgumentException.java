package com.ksyun.ks3.util;

import com.ksyun.ks3.exception.Ks3ClientException;

public class ClientIllegalArgumentException extends Ks3ClientException {
    static enum Reason{
        notNull,//参数不能为空
        notCorrect,//参数格式错误
        notBetween,//参数不在正确取值范围内
        others;
    }
    private Reason reason = Reason.others;
    private String paramName;//出错的参数名
    public ClientIllegalArgumentException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    public Reason getReason() {
        return reason;
    }
    public void setReason(Reason reason) {
        this.reason = reason;
    }
    public String getParamName() {
        return paramName;
    }
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    public String toString(){
        return "ClientIllegalArgumentException:"+super.getMessage()+";reason:"+this.reason+";paramName:"+this.paramName;
    }
}


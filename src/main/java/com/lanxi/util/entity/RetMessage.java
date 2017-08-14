package com.lanxi.util.entity;


import com.lanxi.util.consts.RetCodeEnum;

public class RetMessage extends Message {
	private RetCodeEnum result;
	private Object obj;
	public RetMessage() {}
	public RetMessage(RetCodeEnum result,String message,Object obj){
		setMessage(message);
		this.result=result;
		this.obj=obj;
	}	
	@Override
	public void setCode(String code){
		super.setCode(code);
		result=RetCodeEnum.valueOfCode(code);
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public void setResult(RetCodeEnum result) {
		this.result = result;
		setCode(RetCodeEnum.codeOfValue(result));
	}
	public RetCodeEnum getResult() {
		return result;
	}
	@Override
	public String toString() {
		return "RetMessage [result=" + result + ", obj=" + obj + "]"+super.toString();
	}
	
}

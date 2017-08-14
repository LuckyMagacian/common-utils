package com.lanxi.util.entity;

import com.lanxi.util.consts.RetCodeEnum;

public class GenericRetMessage<T> extends Message {
	private RetCodeEnum result;
	private T obj;
	public GenericRetMessage(){}
	public GenericRetMessage(RetCodeEnum result,String message,T t){
		setMessage(message);
		this.obj=t;
		this.result=result;
	}
	@Override
	public void setCode(String code){
		super.setCode(code);
		result=RetCodeEnum.valueOfCode(code);
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
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

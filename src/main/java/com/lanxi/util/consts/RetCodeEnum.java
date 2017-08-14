package com.lanxi.util.consts;
/**
 * 交互响应码枚举
 * @author yangyuanjian
 * SUCCESS 		操作成功 	0000
 * FAIL			操作失败	0001
 * WARNING		警告		0009
 * EXCEPTION	异常		0999
 * ERROR		错误		9999
 */
public enum RetCodeEnum {
	SUCCESS,FAIL,WARNING,EXCEPTION,ERROR;
	public static RetCodeEnum valueOfCode(String code){
		switch (code) {
		case "0000":return SUCCESS;
		case "0001":return FAIL;
		case "0009":return WARNING;
		case "0999":return EXCEPTION;
		case "9999":return ERROR;
		default:return FAIL;
		}
	}
	public static String codeOfValue(RetCodeEnum result){
		switch (result) {
		case SUCCESS:return "0000";
		case FAIL:return "0001";
		case WARNING:return "0009";
		case EXCEPTION:return "0999";
		case ERROR:return "9999";
		}
		return null;
	}
	@Override
	public String toString() {
		switch (this) {
		case SUCCESS:return "0000";
		case FAIL:return "0001";
		case WARNING:return "0009";
		case EXCEPTION:return "0999";
		case ERROR:return "9999";
		}
		return null;
	}
}

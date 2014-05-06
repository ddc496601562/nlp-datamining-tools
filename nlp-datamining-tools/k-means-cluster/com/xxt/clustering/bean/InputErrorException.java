package com.xxt.clustering.bean;
/**
 * 用于数据输入检测的异常
 * @author dingdongchao
 * @since 2010年5月26日
 * @version 1.0
 */
public class InputErrorException extends RuntimeException {
	public InputErrorException(String message){
		super(message);
	}
}

package com;

public class BINARY extends ISOFiled{
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public BINARY(String filedValue){
		super(filedValue,filedValue.length());
	}
	
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public BINARY(String filedValue,int len){
		super(filedValue,len);
	}
}

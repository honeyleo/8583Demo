package com;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class ASCII extends ISOFiled{
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public ASCII(String filedValue){
		super(filedValue,filedValue.length());
	}
	
	public ASCII(String filedValue,int len){
		super(filedValue,len);
	}

	@Override
	public String pack() throws ISOException {
		return ISOUtil.hexString(getFiledValue().getBytes());
	}

	@Override
	public String unPack(String buf) throws ISOException {
		String pacString = ISOUtil.takeFirstN(buf, getLen()*2);
		buf = ISOUtil.takeLastN(buf, buf.length()-getLen()*2);
		setFiledValue(new String(ISOUtil.hex2byte(pacString)) );
		return buf;
	}	
}

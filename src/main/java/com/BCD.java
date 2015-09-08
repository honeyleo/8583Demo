package com;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class BCD extends ISOFiled{
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public BCD(String filedValue){
		super(filedValue,filedValue.length());
	}
	
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public BCD(String filedValue,int len){
		super(filedValue,len);
	}

	@Override
	public String pack() throws ISOException {
		if(getFiledValue().length()%2 == 0){
			return getFiledValue();
		}else{
			return ISOUtil.padright(getFiledValue(), getFiledValue().length()+1, '0');
		}
	}

	@Override
	public String unPack(String buf) throws ISOException {
		if(getLen()%2!=0){
			setLen(getLen()+1);;
		}
		String pacString = ISOUtil.takeFirstN(buf, getLen());
		buf = ISOUtil.takeLastN(buf, buf.length()-getLen());
		setFiledValue(pacString);
		return buf;
	}
	
}

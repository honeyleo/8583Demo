package com;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class LLBCD extends ISOFiled{

	public LLBCD(){
		
	}
	
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public LLBCD(String filedValue,int len){
		super(filedValue,len);
	}
	
	/**
	 *构造一个制定长度和value的字段
	 * @param filedValue
	 * @param len
	 */
	public LLBCD(String filedValue){
		super(filedValue,filedValue.length());
	}
	
	
	@Override
	public String pack() throws ISOException {
		StringBuffer buf = new StringBuffer();
		if(getFiledValue().length()%2==0){
			buf.append(ISOUtil.padleft(String.valueOf(getFiledValue().length()), 2, '0')).append(getFiledValue());
		}else{
			buf.append(ISOUtil.padleft(String.valueOf(getFiledValue().length()), 2, '0')).append(getFiledValue()+"0");
		}
		return buf.toString();
	}

	@Override
	public String unPack(String buf) throws ISOException {
		// 读取两个字符长度
		String len = ISOUtil.takeFirstN(buf, 2);
		buf = ISOUtil.takeLastN(buf, buf.length()-2);
		int leni = ISOUtil.parseInt(len);
		if(leni%2!=0){
			leni +=1;
		}
		String pacString = ISOUtil.takeFirstN(buf, leni);
		buf = ISOUtil.takeLastN(buf, buf.length()-leni);
		setFiledValue(pacString);
		setLen(leni);
		return buf;
	}

}

package com;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class LLLBCD extends ISOFiled{
	
	
	public LLLBCD(String filedValue){
		super(filedValue, filedValue.length());
	}
	
	public LLLBCD(){
	}
	
	@Override
	public String pack() throws ISOException {
		StringBuffer buf = new StringBuffer();
		if(getFiledValue().length()%2==0){
			buf.append(ISOUtil.padleft(String.valueOf(getFiledValue().length()), 4, '0')).append(getFiledValue());
		}else{
			buf.append(ISOUtil.padleft(String.valueOf(getFiledValue().length()), 4, '0')).append(getFiledValue()+"0");
		}
		return buf.toString();
	}

	@Override
	public String unPack(String buf) throws ISOException {
		// 读取两个字符长度
		String len = ISOUtil.takeFirstN(buf, 4);
		buf = ISOUtil.takeLastN(buf, buf.length()-4);
		int leni = ISOUtil.parseInt(len);
		if(leni%2 != 0){
			leni +=1;
		}
		String pacString = ISOUtil.takeFirstN(buf, leni);
		buf = ISOUtil.takeLastN(buf, buf.length()-leni);
		setFiledValue(pacString);
		setLen(leni);
		return buf;
	}

}

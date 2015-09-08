package com;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class ICDATA extends ISOFiled{

	public ICDATA(String filedValue){
		super(filedValue, filedValue.length()/2);
	}
	public ICDATA(){
	}
	@Override
	public String pack() throws ISOException {
		StringBuffer buf = new StringBuffer();
		buf.append(ISOUtil.padleft(String.valueOf(getFiledValue().length()/2), 4,'0')).append(getFiledValue());
		return buf.toString();
	}

	@Override
	public String unPack(String buf) throws ISOException {
		// 读取两个字符长度
		String len = ISOUtil.takeFirstN(buf, 4);
		buf = ISOUtil.takeLastN(buf, buf.length()-4);
		int leni = ISOUtil.parseInt(len);
		String pacString = ISOUtil.takeFirstN(buf, leni*2);
		buf = ISOUtil.takeLastN(buf, buf.length()-leni*2);
		setFiledValue(pacString);
		setLen(leni);
		return buf;
	}

}

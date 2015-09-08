package com;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class LLASCII extends ISOFiled{

	public LLASCII(String filedValue){
		super(filedValue, filedValue.length());
	}
	public LLASCII(){
	}
	
	@Override
	public String pack() throws ISOException {
		StringBuffer buf = new StringBuffer();
		buf.append(
				ISOUtil.padleft(String.valueOf(getFiledValue().length()), 2,
						'0')).append(
				ISOUtil.hexString(getFiledValue().getBytes()));
		return buf.toString();
	}

	@Override
	public String unPack(String buf) throws ISOException {
		// 读取两个字符长度
		String len = ISOUtil.takeFirstN(buf, 2);
		buf = ISOUtil.takeLastN(buf, buf.length()-2);
		int leni = ISOUtil.parseInt(len);
		String pacString = ISOUtil.takeFirstN(buf, leni*2);
		buf = ISOUtil.takeLastN(buf, buf.length()-leni*2);
		setFiledValue(new String(ISOUtil.hex2byte(pacString)) );
		setLen(leni);
		return buf;
	}

}

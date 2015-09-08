package com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class ISOMsg {
	private  String header ;
	private Map<Integer, ISOFiled> bitMap = new HashMap<Integer, ISOFiled>();

	public Map<Integer, ISOFiled> getBitMap() {
		return bitMap;
	}

	public void setBitMap(Map<Integer, ISOFiled> bitMap) {
		this.bitMap = bitMap;
		System.out.println(bitMap);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	private Map<Integer, ISOFiled> fileds = new HashMap<Integer, ISOFiled>();

	public void set(Integer filedindex,ISOFiled filed){
		fileds.put(filedindex, filed);
	}

	public ISOFiled get(Integer filed){
		return fileds.get(filed);
	}	

	public Set<Integer> getFileds(){
		return fileds.keySet();
	}

	/**
	 * 打包8583报文
	 * @param msg
	 * @return
	 * @throws ISOException 
	 */
	public byte[] pack() throws ISOException{
		Set<Integer> bitMap = this.getFileds();
		StringBuffer buf = new StringBuffer();
		StringBuffer bitMapByte = new StringBuffer();
		buf.append(header);
		buf.append(this.get(0).pack());
		for(int i = 1 ; i <= 64;i++){
			if(bitMap.contains(new Integer(i))){
				bitMapByte.append("1");
			}else{
				bitMapByte.append("0");
			}
		}
		buf.append(ISO8583Utils.packageBitMap(bitMapByte.toString()));
		for(int i = 1 ; i <= 64;i++){
			if(null != this.get(i)){
				buf.append(this.get(i).pack());
			}
		}
		String len = Integer.toHexString(ISOUtil.hex2byte(buf.toString()).length);
		len = ISOUtil.padleft(len, 4, '0');
		//System.out.println("消息长度："+len);
		return ISOUtil.concat(ISOUtil.hex2byte(len), ISOUtil.hex2byte(buf.toString()));
	}

	/**
	 * 解包8583报文
	 * @param buff
	 * @return
	 * @throws ISOException 
	 */
	public ISOMsg upPack(String buff) throws ISOException{
		ISOMsg isoMsg = new ISOMsg();
		isoMsg.setHeader(header);
		isoMsg.setBitMap(new HashMap<Integer, ISOFiled>());
//		System.out.println(bitMap);
		if(bitMap == null){
			return null;
		}
		if(null == buff){
			return null;
		}
		//读取head信息
		isoMsg.setHeader(ISOUtil.takeFirstN(buff, header.length()));
		buff = ISOUtil.takeLastN(buff, buff.length()- header.length());
		//读取消息类型
		isoMsg.set(0, new BCD(ISOUtil.takeFirstN(buff, 4)));
		buff = ISOUtil.takeLastN(buff, buff.length()- 4);
		//读取位图
		String bitMaps = ISOUtil.takeFirstN(buff, 16);
		buff = ISOUtil.takeLastN(buff, buff.length()- 16);
		//转换位图
		List<Integer> bitMapi = ISO8583Utils.parseBitMap(bitMaps);
		for(int i = 0 ;i <64;i++){
			if(bitMapi.contains(i) && null != bitMap.get(i)){
				buff = bitMap.get(i).unPack(buff);
				isoMsg.set(i, bitMap.get(i));
				//System.out.println("第"+i+":"+isoMsg.get(i).getFiledValue());
			}
		}
		return isoMsg;
	}

}

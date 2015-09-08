package com;

import org.jpos.iso.ISOException;


public interface Filed {
	//右补位
	public static String  PADDING_TYPE_RINGHT ="RINGHT";
	
	//左补位
	public static String  PADDING_TYPE_LEFT ="LEFT";
	
	/**
	 * 打包字段
	 * @return
	 */
	public String pack()  throws ISOException;
	
	/**
	 * 解包方法
	 * @param buf
	 * @return
	 */
	public String unPack(String buf) throws ISOException ;
}

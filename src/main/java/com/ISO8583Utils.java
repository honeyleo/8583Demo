package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;




public class ISO8583Utils {

	/**
	 * 将01组成的位图串转换成16进制的位图串
	 * @param bitMap 01组成的位图串
	 * @return 16进制的位图串
	 * @throws ISO8583Exception
	 */
	public static StringBuffer packageBitMap(String bitMap) {
		if (bitMap == null || bitMap.length() != 64) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, k = 0; i < bitMap.length() / 4; i++, k += 4) {
			buffer.append(binaryToHex(bitMap.substring(k, k + 4)));
		}
		return buffer;
	}	

	/**
	 * 将4位的01串转换成16进制
	 * @param str 4位01串
	 * @return 01串的16进制表示(0-f)
	 */
	public static String binaryToHex(String str) {
		if (str == null || str.length() != 4) {
			return null;
		}
		if (ISO8583Const.HEX_0.equals(str)) {
			return ISO8583Const.BINARY_0;
		}else if (ISO8583Const.HEX_1.equals(str)) {
			return ISO8583Const.BINARY_1;
		}else if (ISO8583Const.HEX_2.equals(str)) {
			return ISO8583Const.BINARY_2;
		}else if (ISO8583Const.HEX_3.equals(str)) {
			return ISO8583Const.BINARY_3;
		}else if (ISO8583Const.HEX_4.equals(str)) {
			return ISO8583Const.BINARY_4;
		}else if (ISO8583Const.HEX_5.equals(str)) {
			return ISO8583Const.BINARY_5;
		}else if (ISO8583Const.HEX_6.equals(str)) {
			return ISO8583Const.BINARY_6;
		}else if (ISO8583Const.HEX_7.equals(str)) {
			return ISO8583Const.BINARY_7;
		}else if (ISO8583Const.HEX_8.equals(str)) {
			return ISO8583Const.BINARY_8;
		}else if (ISO8583Const.HEX_9.equals(str)) {
			return ISO8583Const.BINARY_9;
		}else if (ISO8583Const.HEX_A.equals(str)) {
			return ISO8583Const.BINARY_A;
		}else if (ISO8583Const.HEX_B.equals(str)) {
			return ISO8583Const.BINARY_B;
		}else if (ISO8583Const.HEX_C.equals(str)) {
			return ISO8583Const.BINARY_C;
		}else if (ISO8583Const.HEX_D.equals(str)) {
			return ISO8583Const.BINARY_D;
		}else if (ISO8583Const.HEX_E.equals(str)) {
			return ISO8583Const.BINARY_E;
		}else if (ISO8583Const.HEX_F.equals(str)) {
			return ISO8583Const.BINARY_F;
		}
		return null;
	}

	/**
	 * 根据16进制的bitMap串来判断哪个域中有值，并将域的位置放入一个int数组
	 * 
	 * @param bitMap
	 * @return int数组，其中的值表示那个域有值
	 */
	public static List<Integer> parseBitMap(String bitMap) {
		List<Integer> list = new ArrayList<Integer>();
		StringBuffer buffer = new StringBuffer();
		char[] ch = bitMap.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			buffer.append(hexToBinary(ch[i]));
		}

		for (int k = 0; k < buffer.length(); k++) {
			if (buffer.charAt(k) == '1') {
				list.add(k+1);
			}
		}
		return list;
	}

	/**
	 * 将一个16进制字符转换成2进制
	 * 
	 * @param c 16进制字符
	 * @return 2进制表示
	 */
	public static String hexToBinary(char ch) {
		String c = String.valueOf(ch);
		if (ISO8583Const.BINARY_0.equals(c)) {
			return ISO8583Const.HEX_0;
		} else if (ISO8583Const.BINARY_1.equals(c)) {
			return ISO8583Const.HEX_1;
		} else if (ISO8583Const.BINARY_2.equals(c)) {
			return ISO8583Const.HEX_2;
		} else if (ISO8583Const.BINARY_3.equals(c)) {
			return ISO8583Const.HEX_3;
		} else if (ISO8583Const.BINARY_4.equals(c)) {
			return ISO8583Const.HEX_4;
		} else if (ISO8583Const.BINARY_5.equals(c)) {
			return ISO8583Const.HEX_5;
		} else if (ISO8583Const.BINARY_6.equals(c)) {
			return ISO8583Const.HEX_6;
		} else if (ISO8583Const.BINARY_7.equals(c)) {
			return ISO8583Const.HEX_7;
		} else if (ISO8583Const.BINARY_8.equals(c)) {
			return ISO8583Const.HEX_8;
		} else if (ISO8583Const.BINARY_9.equals(c)) {
			return ISO8583Const.HEX_9;
		} else if (ISO8583Const.BINARY_A.equals(c)) {
			return ISO8583Const.HEX_A;
		} else if (ISO8583Const.BINARY_B.equals(c)) {
			return ISO8583Const.HEX_B;
		} else if (ISO8583Const.BINARY_C.equals(c)) {
			return ISO8583Const.HEX_C;
		} else if (ISO8583Const.BINARY_D.equals(c)) {
			return ISO8583Const.HEX_D;
		} else if (ISO8583Const.BINARY_E.equals(c)) {
			return ISO8583Const.HEX_E;
		} else if (ISO8583Const.BINARY_F.equals(c)) {
			return ISO8583Const.HEX_F;
		}
		return null;
	}
	
	public static ISOMsg send(ISOMsg msg) throws ISOException, IOException {
		Socket soc = new Socket("120.192.83.154", 10006);
//		soc.connect(new InetSocketAddress("120.192.83.154", 10006), 3600);
		OutputStream out = new DataOutputStream(soc.getOutputStream());
		out.write(msg.pack());
		out.flush();
		DataInputStream in = new DataInputStream(soc.getInputStream());	//响应，返回
		byte [] mesLength = new byte[2];
		in.read(mesLength, 0, 2);
		int len = Integer.parseInt(ISOUtil.hexString(mesLength), 16);
		byte [] msgByte = new byte[len];
		
		in.read(msgByte, 0, len);
		//System.out.println("返回消息长度："+ Integer.parseInt(ISOUtil.hexString(mesLength), 16));
		//System.out.println("返回消息内容："+ ISOUtil.hexString(msgByte));
		ISOMsg r = msg.upPack(ISOUtil.hexString(msgByte));
		for(int i = 0 ;i< 64; i++){
			if(null != r.get(i)){
				//System.out.println("第"+i+"信息："+r.get(i).getFiledValue());
				//Log.e("send(ISOMsg msg)","第"+i+"域："+ r.get(i).getFiledValue());
			}
		}
		soc.close();
		return r;
	}
}

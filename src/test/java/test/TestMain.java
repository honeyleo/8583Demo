package test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import com.ASCII;
import com.BCD;
import com.ISO8583Utils;
import com.ISOFiled;
import com.ISOMsg;
import com.LLBCD;
import com.LLLASCII;
import com.LLLBCD;
import com.LLLBINAry;

public class TestMain {
	public static void main(String [] args) throws ISOException, UnknownHostException, IOException{
		ISOMsg msg = new ISOMsg();
		msg.setHeader("6000040000603100311004");
		Map<Integer, ISOFiled> bitMap = new HashMap<Integer, ISOFiled>();
		//设置返回的bitMap类型
		bitMap.put(11, new BCD("",6));
		bitMap.put(12, new BCD("",6));
		bitMap.put(13, new BCD("",4));
		bitMap.put(32, new LLBCD());
		bitMap.put(37, new ASCII("",12));
		bitMap.put(39, new ASCII("",2));
		bitMap.put(41, new ASCII("",8));
		bitMap.put(42, new ASCII("",15));
		bitMap.put(59, new LLLASCII(""));
		bitMap.put(60, new LLLBCD());
		bitMap.put(62, new LLLBINAry());
		bitMap.put(63, new LLLASCII());
		msg.setBitMap(bitMap);
		msg.set(0, new BCD("0800"));
		msg.set(11, new BCD("111111"));
		msg.set(41, new ASCII("59225257"));
		msg.set(42, new ASCII("100370154113655"));
		msg.set(59, new LLLASCII("null                20150827174550      "));
		msg.set(60, new LLLBCD("00111111003"));
		msg.set(63, new LLLASCII("01 "));
		for(int i=0;i<64;i++){
			if(msg.get(i) != null ){
				System.out.println(i+"域=" + msg.get(i).getFiledValue());
			}
		}
		System.out.println(ISOUtil.hexString(msg.pack()));
		msg = ISO8583Utils.send(msg);
	
		msg.get(62).getFiledValue();
		for(int i=0;i<64;i++){
			if(msg.get(i) != null ){
				System.out.println(i+"域=" + msg.get(i).getFiledValue());
			}
		}
		
	}

}

package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jpos.iso.ISOException;

import com.ASCII;
import com.BCD;
import com.ISOMsg;
import com.LLLASCII;
import com.LLLBCD;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 9000);
			OutputStream out = socket.getOutputStream();
			ISOMsg msg = new ISOMsg();
			msg.setHeader("6000030000603100311004");
			msg.set(0, new BCD("0800"));
			msg.set(11, new BCD("111111"));
			msg.set(41, new ASCII("88888888"));
			msg.set(42, new ASCII("100440350940001"));
			msg.set(60, new LLLBCD("00111111003"));
			msg.set(63, new LLLASCII("001"));
			byte[] bytes = msg.pack();
			out.write(bytes);
			socket.getInputStream();
			try {
				Thread.sleep(10000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ISOException e) {
			e.printStackTrace();
		}
		
	}

}

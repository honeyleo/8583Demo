package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jpos.iso.ISOUtil;

public class Server {

	private static final ExecutorService POOL = Executors.newCachedThreadPool();
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(9000);
			while(true) {
				Socket socket = server.accept();
				System.out.println("客户端-" + socket.getRemoteSocketAddress() + "连接上服务器...");
				Task task = new Task(socket);
				POOL.submit(task);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static class Task implements Runnable {
		
		private Socket socket;
		
		public Task(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			InputStream in = null;
			try {
				in = socket.getInputStream();
				
				//2个字节代表后面数据的包大小
				byte[] lenBytes = new byte[2];
				do {
					int b = in.read(lenBytes);
					if(b == 2) {
						break;
					}
				} while(true);
				
				int size = ((lenBytes[0] & 0xFF) << 8) + (lenBytes[1] & 0xFF);
				
				byte[] bytes = new byte[10];
				int offset = 0;
				ByteArrayOutputStream bous = new ByteArrayOutputStream(size);
				do {
					int len = in.read(bytes);
					bous.write(bytes, 0, len);
					offset +=len;
				} while(offset < size);
				
				byte[] data = bous.toByteArray();
				System.out.println(ISOUtil.hexString(data));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

package server;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer2 {
    private final int PORT = 60000;
    private ExecutorService pool;
    private ServerSocketChannel ssc;
    private Selector selector;
    private int n;

    public static void main(String[] args) throws IOException {
        MultiThreadServer2 server = new MultiThreadServer2();
        server.doService();
    }

    public MultiThreadServer2() throws IOException {
        pool = Executors.newFixedThreadPool(5);

        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ServerSocket ss = ssc.socket();
        ss.bind(new InetSocketAddress(PORT));
        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started...");
    }

    public void doService() {
        while (true) {
            try {
                n = selector.select();
            } catch (IOException e) {
                throw new RuntimeException("Selector.select()异常!");
            }
            if (n == 0)
                continue;
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = null;
                    try {
                        sc = ((ServerSocketChannel) key.channel()).accept();
                        sc.configureBlocking(false);
                        System.out.println("客户端:"
                                + sc.socket().getInetAddress().getHostAddress()+"端口"+sc.socket().getPort()
                                + " 已连接");
                        SelectionKey k = sc.register(selector,
                                SelectionKey.OP_READ);
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        k.attach(buf);
                    } catch (Exception e) {
                        try {
                            sc.close();
                        } catch (Exception ex) {
                        }
                    }
                } else if (key.isReadable()) {
                    key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                    pool.execute(new Reader(key));
                }
            }
        }
    }

    public byte[] _read(SocketChannel channel, int length) throws IOException {
		int nrecvd = 0;
		byte[] data = new byte[length];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		try {
			while (nrecvd < length) {
				long n = channel.read(buffer);
				if (n < 0)
					throw new EOFException();
				nrecvd += (int) n;
			}
		} finally {

		}
		return data;
	}
    public static class Reader implements Runnable {
        private SelectionKey key;

        public Reader(SelectionKey key) {
            this.key = key;
        }

        @Override
        public void run() {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buf = (ByteBuffer) key.attachment();
            buf.clear();
            int len = 0;
            try {
                while ((len = sc.read(buf)) > 0) {// 非阻塞，立刻读取缓冲区可用字节
                    buf.flip();
                    byte[] bytes = new byte[buf.remaining()];
                    buf.get(bytes); 
                    buf.clear();
                    ByteBuffer res = ByteBuffer.wrap(bytes);
                    sc.write(res);
                }
                if (len == -1) {
                    System.out.println("客户端断开。。。");
                    sc.close();
                }
                // 没有可用字节,继续监听OP_READ
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                key.selector().wakeup();
            } catch (Exception e) {
                try {
                    sc.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}

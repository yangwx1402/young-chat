package com.young.java.chat.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by dell on 2016/10/28.
 */
public class YoungChatClient {
    private String serverIp;

    private int serverPort;

    private SocketChannel socketChannel;

    private Selector selector;

    private int retry = 3;

    private int retryInterval = 1000;

    private volatile boolean stop = false;

    public YoungChatClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void init() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
    }

    public void connectServer() throws IOException, InterruptedException {
        if (socketChannel.connect(new InetSocketAddress(serverIp, serverPort))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
        process();
    }

    public void stop() throws IOException {
        socketChannel.close();
        selector.close();
        stop = true;
    }

    private void process() throws IOException, InterruptedException {
        int select = 0;
        SelectionKey key = null;
        while (!stop) {
            select = selector.select(1000);
            if (select == 0) {
                continue;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                keys.remove();
                handInput(key);
            }
            if(socketChannel.finishConnect()){
                doWrite(socketChannel);
                Thread.sleep(1000);
            }
        }
    }

    private void handInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    //注册读取事件
                    sc.register(selector, SelectionKey.OP_READ);
                    //网服务器发送信息
                    doWrite(sc);
                } else {
                    //连接失败了
                    System.exit(-1);
                }
            }
            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
                int readBytes = sc.read(buffer);
                if (readBytes > 0) {
                    buffer.flip();
                    byte[] temp = new byte[buffer.remaining()];
                    buffer.get(temp);
                    System.out.println("client receive server data -[" + new String(temp, "utf-8") + "]");
                    //stop();
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = ("Client time is " + new Date().toString()).getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("client write data to server success");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        YoungChatClient client = new YoungChatClient("localhost", 9999);
        client.init();
        for (int i = 0; i < 100; i++)
            client.connectServer();
    }
}

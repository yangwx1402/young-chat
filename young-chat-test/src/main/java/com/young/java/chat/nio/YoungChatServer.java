package com.young.java.chat.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by dell on 2016/10/28.
 */
public class YoungChatServer {
    private Selector serverSelector;

    private ServerSocketChannel serverSocketChannel;

    private String ip;

    private int port;

    private volatile boolean stop = false;

    public YoungChatServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void stop() throws IOException {
        this.stop = false;
        serverSocketChannel.close();
        serverSelector.close();
    }

    public void start() throws IOException {
        serverSelector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(ip, port), 1024);
        serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
        listener();
    }

    private void listener() throws IOException {
        int select = 0;
        while (!stop) {
            select = serverSelector.select(1000);
            if (select == 0)
                continue;
            Iterator<SelectionKey> keys = serverSelector.selectedKeys().iterator();
            SelectionKey key = null;
            while (keys.hasNext()) {
                key = keys.next();
                keys.remove();
                try {
                    inputHandler(key);
                }catch (Exception e){
                    key.cancel();
                    key.channel().close();
                }
            }
        }
    }

    private void inputHandler(SelectionKey key) throws IOException {
        //验证SelectionKey的有效性
        if (key.isValid()) {
            //判断SelectionKey是否准备好等待客户端的连接
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                System.out.println("ssc==serverSocketChannel" + (ssc == serverSocketChannel));
                SocketChannel ss = ssc.accept();
                if (ss != null) {
                    ss.configureBlocking(false);
                    ss.register(serverSelector, SelectionKey.OP_READ);
                }
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
                if (socketChannel.isOpen()) {
                    int readBytes = socketChannel.read(buffer);
                    if (readBytes > 0) {
                        buffer.flip();
                        byte[] temp = new byte[buffer.remaining()];
                        buffer.get(temp);
                        System.out.println("server receive a message -["+(new String(temp,"utf-8"))+"]");
                        doWrite(socketChannel,"now server time is -["+(System.currentTimeMillis())+"]");
                    } else if (readBytes < 0) {
                        key.cancel();
                        socketChannel.close();
                    }
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String s) throws IOException {
        byte[] bytes = s.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);
    }

    public static void main(String[] args) throws IOException {
        YoungChatServer server = new YoungChatServer("localhost",9999);
        server.start();
    }

}

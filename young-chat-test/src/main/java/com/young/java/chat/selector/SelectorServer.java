package com.young.java.chat.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by dell on 2016/10/28.
 */
public class SelectorServer {
    public void startServer(String ip, int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ServerSocket ss = ssc.socket();
        ssc.configureBlocking(false);
        //这里绑定采用ServerSocket和ServerSocketChannel是一样的,但是accept不一样,ss是阻塞的 而ssc可以是非阻塞的
        ss.bind(new InetSocketAddress(ip, port));
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(ssc.socket());
        while (true) {
            //看看是否有事件准备好
            int key = selector.select(1000);
            //什么事件也没有
            if (key == 0) {
                continue;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey event = keys.next();
                if (event.isAcceptable()) {
                    System.out.println("server receive a connection");
                    ServerSocketChannel channel = (ServerSocketChannel) event.channel();
                    System.out.println(ssc == channel);
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    sayHello("hello", socketChannel);
                }
                if (event.isReadable()) {
                    System.out.println("server read data");
                    readDataFromSocket(event);
                }
                keys.remove();
            }
        }
    }

    private void readDataFromSocket(SelectionKey event) throws IOException {
        SocketChannel channel = (SocketChannel) event.channel();
        ByteBuffer buffer = ByteBuffer.allocate(10 * 1024);
        while (channel.read(buffer) != -1) {
            buffer.flip();
            byte[] bytes = new byte[2];
            while(buffer.hasRemaining()){
                //channel.write(buffer);
                System.out.println("server receive a message");
                buffer.get(bytes);
                System.out.println(new String(bytes));
            }
            buffer.clear();
        }
        channel.close();
    }

    private void sayHello(String data, SocketChannel socketChannel) throws IOException {
        System.out.println("server send a hello");
        ByteBuffer buffer = ByteBuffer.allocate(data.getBytes().length);
        buffer.put(data.getBytes());
        buffer.flip();
        while (buffer.hasRemaining())
            socketChannel.write(buffer);
    }

    public static void main(String[] args) throws IOException {
        SelectorServer server = new SelectorServer();
        server.startServer("localhost",9999);
    }
}

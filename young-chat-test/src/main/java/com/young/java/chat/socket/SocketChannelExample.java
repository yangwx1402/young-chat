package com.young.java.chat.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * Created by dell on 2016/10/26.
 */
public class SocketChannelExample {
    public void setBlocking(boolean blocking) throws IOException {
        SocketChannel sc = SocketChannel.open();
        //直接用socket连接就是阻塞方式的
        sc.configureBlocking(blocking);
        System.out.println(sc.isBlocking());
        //打开了一个socket与channel对应,但是这个socket是未连接的
        Socket socket = sc.socket();
        System.out.println(socket);
        System.out.println(socket.isConnected());
        System.out.println(sc == socket.getChannel());
    }

    public void blockConnect() throws IOException {
        SocketChannel sc = SocketChannel.open();
        Socket socket = sc.socket();
        socket.connect(new InetSocketAddress("localhost",9999));
        System.out.println(socket.isConnected());
    }

    public void noBlockConnect() throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        boolean bool = sc.connect(new InetSocketAddress("localhost",9999));
        System.out.println(bool);
        while(true) {
            if(sc.finishConnect()){
                System.out.println("socket is connected");
                break;
            }else{
                System.out.println("socket is connecting");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        SocketChannelExample example = new SocketChannelExample();
        //example.setBlocking(false);
        example.blockConnect();
        example.noBlockConnect();
    }
}

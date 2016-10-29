package com.young.java.chat.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

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
        socket.connect(new InetSocketAddress("localhost", 9999));
        System.out.println(socket.isConnected());
    }

    public void noBlockConnect(List<String> data) throws IOException, InterruptedException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        boolean bool = sc.connect(new InetSocketAddress("localhost", 9999));
        System.out.println(bool);
        while (true) {
            if (sc.finishConnect()) {
                System.out.println("socket is connected");
                sendData(data, sc);
                break;
            } else {
                System.out.println("socket is connecting");
            }
        }
        Thread.sleep(10000);
    }

    private void sendData(List<String> data, SocketChannel sc) throws IOException {
        for (String line : data) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(line.getBytes().length);
            byteBuffer.put(line.getBytes());
            sc.write(byteBuffer);
            byteBuffer.clear();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannelExample example = new SocketChannelExample();
        //example.setBlocking(false);
        //example.blockConnect();
        List<String> data = new ArrayList<String>(100);
        for(int i=0;i<100;i++){
            data.add("data_"+i);
        }
        example.noBlockConnect(data);
    }
}

package com.young.java.chat.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by dell on 2016/10/26.
 */
public class ServerSocketChannelExample {
    public void example() throws IOException, InterruptedException {
        //执行了open方法以后就创建了一个未绑定的java.net.ServerSocket,该对象可以通过SSC的socket方法获取到
        ServerSocketChannel ssc = ServerSocketChannel.open();

        System.out.println(ssc.socket());
        //由于ssc并没有bind方法,所以需要改socket进行bind
        ServerSocket ss = ssc.socket();
        ss.bind(new InetSocketAddress(9999));
        //此时serversocket对象已经进行了绑定
        System.out.println(ss);
        //如果使用ss.accept()那么该socket的表现跟java.net.ServerSocket的表现一样,是阻塞模式的socket
        //ss.accept();
        //而如果使用ssc.accept()那么该socket可以工作在非阻塞模式,是阻塞还是非阻塞决定于configBlocking,默认为阻塞
        ssc.configureBlocking(false);
        while (true) {
            SocketChannel channel = ssc.accept();
            if (channel == null) {
                Thread.sleep(2000);
            } else {
                System.out.println(channel);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannelExample example = new ServerSocketChannelExample();
        example.example();
    }
}

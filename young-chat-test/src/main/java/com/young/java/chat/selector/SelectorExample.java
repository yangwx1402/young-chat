package com.young.java.chat.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by dell on 2016/10/27.
 */
public class SelectorExample {
    public void selectorApi() throws IOException {
        //open通过调用SPI的底层SelectorProvider提供了Selector
        Selector selector = Selector.open();
        //只有SelectableChannel才能注册在Selector中,FileChannel不可以
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //只有nonblocking的channel才能注册到Selector,否则会报java.nio.channels.IllegalBlockingModeException
        //ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.configureBlocking(false);
        //注册channel的时候只能注册channel有的操作,否则会报java.lang.IllegalArgumentException
        //ssc.register(selector,SelectionKey.OP_CONNECT);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //注册多种操作
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        System.out.println(selector.keys());
        //打印selector已经注册的channel
        for (SelectionKey key : selector.keys()) {
            System.out.println(key.channel());
        }
        System.out.println(selector.isOpen());
        selector.close();
        System.out.println(selector.isOpen());
    }

    public void selects(int selectionKey) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress("localhost",9999));
        ssc.configureBlocking(false);
        SelectionKey key = ssc.register(selector, selectionKey);

    }

    public static void main(String[] args) throws IOException {
        SelectorExample example = new SelectorExample();
        //example.selectorApi();
        example.selects(SelectionKey.OP_ACCEPT);
    }
}

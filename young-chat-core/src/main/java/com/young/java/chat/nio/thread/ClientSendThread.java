package com.young.java.chat.nio.thread;

import com.young.java.chat.nio.ChatCommon;
import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.message.TextMessage;
import com.young.java.chat.util.DateUtils;
import com.young.java.chat.util.JsonUtils;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by dell on 2016/10/31.
 */
public class ClientSendThread extends ChatCommon implements Runnable {

    private Selector selector;

    private SocketChannel socketChannel;

    public ClientSendThread(Selector selector,SocketChannel socketChannel){
        this.selector = selector;
        this.socketChannel = socketChannel;
    }
    private int timeout = 5 * 1000;

    private volatile boolean stop = false;

    private volatile boolean connected = false;

    public void stop(){
        stop = true;
    }

    private void listen() throws IOException {
        int selectNum = 0;
        SelectionKey key = null;
        while (!stop) {
            selectNum = selector.select(timeout);
            //System.out.println("client is waiting for read server time is -" + DateUtils.date2String(dateFormat, new Date()));
            if (selectNum == 0) {
                continue;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                keys.remove();
                handInput(key);
            }
        }
    }

    private void handInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    connected = true;
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    System.out.println("server conntected successful");
                }else {
                    System.out.println("connect server failed exit ....");
                    System.exit(-1);
                }
            }
            if (key.isReadable()) {
                String data = readData(socketChannel);
               // System.out.println("read data from socket -["+data+"]");
                if (data != null) {
                    ChatMessage message = JsonUtils.fromJson(data, ChatMessage.class);
                    printMessage(message);
                }
            }
        }
    }

    private void printMessage(ChatMessage message) {
        TextMessage text = message.getText();
        System.out.println(DateUtils.date2String(dateFormat, new Date(message.getSendTime())) + " " + text.getFromUser().getUsername() + ":" + text.getText());
    }

    public void run() {
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

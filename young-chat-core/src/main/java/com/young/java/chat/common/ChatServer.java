package com.young.java.chat.common;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.message.TextMessage;
import com.young.java.chat.message.UserMessage;
import com.young.java.chat.util.DateUtils;
import com.young.java.chat.util.JsonUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by dell on 2016/10/31.
 */
public class ChatServer extends ChatCommon {
    //保存所有的客户端长连接
    private Map<UserMessage, SocketChannel> user_channel_mapping = new HashMap<UserMessage, SocketChannel>();

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private String ip;

    private int port;

    private int timeout = 5000;

    private volatile boolean stop = false;


    public ChatServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(ip, port), 100);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        listen();
    }

    public synchronized void stop() throws IOException {
        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            serverSocketChannel.close();
        }
        if (selector != null && selector.isOpen()) {
            selector.close();
        }
        stop = false;
    }

    private void listen() throws IOException {
        int selectNum = 0;
        SelectionKey key = null;
        System.out.println("server started bind info is -" + serverSocketChannel);
        while (!stop) {
            selectNum = selector.select(timeout);
            if (user_channel_mapping.isEmpty()) {
                System.out.println("server is waiting for connection time is -" + DateUtils.date2String(dateFormat, new Date()));
            }
            if (selectNum == 0) {
                continue;
            }
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                keys.remove();
                try {
                    handInput(key);
                } catch (Exception e) {
                    e.printStackTrace();
                    key.cancel();
                    key.channel().close();
                }
            }
        }
    }

    private void handInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //判断服务端是否准备好进行连接
            if (key.isAcceptable()) {
                SocketChannel sc = serverSocketChannel.accept();
                if (sc != null) {
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }
            }
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                String jsonData = readData(sc);
                //System.out.println("server receive data -["+jsonData+"]");
                if (jsonData != null) {
                    ChatMessage message = JsonUtils.fromJson(jsonData, ChatMessage.class);
                    processMessage(message, sc);
                }
            }
        }
    }

    private void processMessage(ChatMessage message, SocketChannel sc) throws IOException {
        UserMessage user = message.getUser();
        if (!user_channel_mapping.containsKey(user)) {
            user_channel_mapping.put(message.getUser(), sc);
        }
        sendMessage(message);
    }

    private void sendMessage(ChatMessage message) throws IOException {
        List<UserMessage> users = message.getToUsers();
        if (users == null || users.isEmpty()) {
            users = getAllOnlineUser();
        }
        sendMessageToUserLists(message, users);
    }

    private void sendMessageToUserLists(ChatMessage message, List<UserMessage> users) throws IOException {
        for (UserMessage user : users) {
            if (!message.getUser().getUsername().equals(user.getUsername()) && user_channel_mapping.containsKey(user)) {
                doWriteMessage(message, user_channel_mapping.get(user));
            }
        }
    }

    private void doWriteMessage(ChatMessage message, SocketChannel socketChannel) throws IOException {
        byte[] temp = JsonUtils.toJson(message).getBytes(encode);
        ByteBuffer buffer = ByteBuffer.allocate(temp.length);
        buffer.put(temp);
        buffer.flip();
        socketChannel.write(buffer);
    }

    private List<UserMessage> getAllOnlineUser() {
        return new ArrayList<UserMessage>(user_channel_mapping.keySet());
    }


    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer("localhost", 9999);
        server.start();
    }
}

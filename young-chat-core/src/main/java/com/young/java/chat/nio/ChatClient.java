package com.young.java.chat.nio;

import com.young.java.chat.nio.thread.ClientSendThread;
import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.message.LoginMessage;
import com.young.java.chat.message.TextMessage;
import com.young.java.chat.message.UserMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by dell on 2016/10/31.
 */
public class ChatClient extends ChatCommon {
    private Selector selector;

    private SocketChannel socketChannel;

    private String serverIp;

    private int serverPort;

    private ClientSendThread runnable;

    public ChatClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void stop() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
        if (selector != null && selector.isOpen()) {
            selector.close();
        }
        if(runnable!=null){
            runnable.stop();
        }

    }

    public void connectServer() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        if (socketChannel.connect(new InetSocketAddress(serverIp, serverPort))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
        runnable = new ClientSendThread(selector,socketChannel);
        new Thread(runnable).start();
    }



    public ChatMessage createMessage(String username, String gender, int age, String line) {
        ChatMessage message = new ChatMessage();
        UserMessage user = new UserMessage();
        user.setUsername(username);
        user.setGender(gender);
        user.setAge(age);
        message.setUser(user);
        LoginMessage login = new LoginMessage();
        login.setUsername(username);
        login.setPassword("123456");
        message.setLogin(login);
        TextMessage text = new TextMessage();
        text.setFromUser(message.getUser());
        text.setText(line);
        message.setSendTime(System.currentTimeMillis());
        message.setText(text);
        return message;
    }

    public void sendMessage(String username, String gender, int age, String line) throws IOException {
        ChatMessage message = createMessage(username, gender, age, line);
        sendMessage(message,socketChannel);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ChatClient client = new ChatClient("localhost", 9999);
        client.connectServer();
        Thread.sleep(5000);
        Random random = new Random();
        String username = "杨勇_"+random.nextInt(10000);
        String gender = "男";
        int age = 30;
        Scanner scanner = new Scanner(System.in);
        String line = null;
        while(true){
            System.out.println(username+":");
            line = scanner.nextLine();
            client.sendMessage(username,gender,age,line);
        }
    }
}

package com.young.java.chat.netty.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.message.LoginMessage;
import com.young.java.chat.message.TextMessage;
import com.young.java.chat.message.UserMessage;
import com.young.java.chat.util.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.Scanner;

/**
 * Created by young.yang on 2016/11/6.
 * 處理客戶端發送邏輯
 */
public class MessageSenderHandler extends SimpleChannelInboundHandler<ChatMessage> {

    private String username;

    private String gender;

    private int age;

    public MessageSenderHandler(String username, String gender, int age){
        this.username = username;
        this.gender = gender;
        this.age = age;
    }

    public ChatMessage createMessage(String line) {
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

    private class MessageSenderThread implements Runnable{

         private ChannelHandlerContext ctx;
        public MessageSenderThread(ChannelHandlerContext ctx){
            this.ctx = ctx;
        }

        public void run() {
           String line = null;
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.print(username + ":");
                line = scanner.nextLine();
                ChatMessage message = createMessage(line);
                message.setToken(token);
                ctx.writeAndFlush(message);
                System.out.println("");
            }
        }
    }

    private static String token = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // System.out.println("MessageSenderHandler channelActive run");
        ChatMessage message = createMessage(null);
        ctx.writeAndFlush(message);
        new Thread(new MessageSenderThread(ctx)).start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        if(token!=null){
           System.out.println(DateUtils.date2String("yyyy-MM-dd HH:mm:ss",new Date(msg.getSendTime()))+""+msg.getUser().getUsername()+":"+msg.getText().getText());
        }else{
            token = msg.getToken();
        }
    }
}

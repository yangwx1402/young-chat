package com.young.java.chat.netty;

import com.young.java.chat.netty.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * Created by young.yang on 2016/11/6.
 */
public class ChatNettyClient {

    private String username;

    private String gender;

    private int age;

    public ChatNettyClient(String username,String gender,int age){
        this.username = username;
        this.gender = gender;
        this.age = age;
    }

    public void connect(String ip,int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup handlerGroup = new NioEventLoopGroup(10);
        bootstrap.group(handlerGroup).option(ChannelOption.SO_BACKLOG,10)
                .channel(NioSocketChannel.class).handler(new ChatNettyClientHandlerInit());
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip,port)).sync();
        future.channel().closeFuture().sync();
    }

    private class ChatNettyClientHandlerInit extends ChannelInitializer{
        @Override
        protected void initChannel(Channel ch) throws Exception {
            //1先解密
            ch.pipeline().addLast(new ClientBase64DetryHandler());
            //2在反序列化
            ch.pipeline().addLast(new JsonDecoderHandler());
            //5.加密并回写到服務端
            ch.pipeline().addLast(new Base64EntryHandler());
            //4.序列化
            ch.pipeline().addLast(new JsonEncoderHandler());
            //3發送消息
            ch.pipeline().addLast(new MessageSenderHandler(username,gender,age));
        }
    }
    public static void main(String[] args) throws InterruptedException {
        String username = "user_"+new Random().nextInt(1000);
        String gender = "男";
        int age = new Random().nextInt(100);
        ChatNettyClient client = new ChatNettyClient(username,gender,age);
        String ip = "localhost";
        int port = 9999;
        client.connect(ip,port);
    }
}

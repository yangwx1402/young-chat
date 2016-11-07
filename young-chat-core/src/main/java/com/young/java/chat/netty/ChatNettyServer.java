package com.young.java.chat.netty;

import com.young.java.chat.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by young.yang on 2016/11/6.
 */
public class ChatNettyServer {
    public void start(String ip,int port) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup connectionProcessGroup = new NioEventLoopGroup(10);
        EventLoopGroup handlerProcessGroup = new NioEventLoopGroup(100);
        serverBootstrap.group(connectionProcessGroup,handlerProcessGroup).channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG,100).childHandler(new ChatNettyServerHandlerInit());
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(ip,port)).sync();
        future.channel().closeFuture().sync();
    }

    private class ChatNettyServerHandlerInit extends ChannelInitializer{

        @Override
        protected void initChannel(Channel ch) throws Exception {
            //1先解密
            ch.pipeline().addLast(new Base64DetryHandler());
            //2在反序列化
            ch.pipeline().addLast(new JsonDecoderHandler());
            //5.加密并回写到客户端
            ch.pipeline().addLast(new Base64EntryHandler());
            //4.序列化
            ch.pipeline().addLast(new JsonEncoderHandler());
            //3实现转发
            ch.pipeline().addLast(new MessageForwardHandler());
        }
    }
    public static void main(String[] args) throws InterruptedException {
        String ip = "localhost";
        int port = 9999;
        ChatNettyServer server = new ChatNettyServer();
        server.start(ip,port);
    }
}

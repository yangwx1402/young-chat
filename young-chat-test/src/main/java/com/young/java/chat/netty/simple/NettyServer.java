package com.young.java.chat.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by dell on 2016/11/1.
 */
public class NettyServer {
    public void start() {
        int threads = 10;
        int backlog = 1024;
        int port = 9999;
        EventLoopGroup selectorListenerThreadsPool = new NioEventLoopGroup(threads);
        EventLoopGroup handlerProcessThreadsPool = new NioEventLoopGroup(threads);
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(selectorListenerThreadsPool, handlerProcessThreadsPool).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, backlog);
            server.childHandler(new ChildHandlerChannel());
            ChannelFuture channelFuture = server.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            selectorListenerThreadsPool.shutdownGracefully();
            handlerProcessThreadsPool.shutdownGracefully();
        }
    }
    private class ChildHandlerChannel extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    private class TimeServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf)msg;
            byte[] temp = new byte[buf.readableBytes()];
            buf.readBytes(temp);
            System.out.println("server receive a message -["+(new String(temp,"utf-8"))+"]");
        }
    }

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start();
    }

}

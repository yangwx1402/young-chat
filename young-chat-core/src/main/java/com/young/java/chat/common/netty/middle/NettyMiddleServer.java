package com.young.java.chat.common.netty.middle;

import com.young.java.chat.common.netty.middle.handler.NettyMiddleDecodeHandler;
import com.young.java.chat.common.netty.middle.handler.NettyMiddleEncodeHandler;
import com.young.java.chat.common.netty.middle.handler.NettyMiddlePrintHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by young.yang on 2016/11/3.
 */
public class NettyMiddleServer {

    private String ip;

    private int port;

    public NettyMiddleServer(String ip,int port){
        this.ip = ip;
        this.port = port;
    }

    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup parentPool = new NioEventLoopGroup(100);
        EventLoopGroup childPool = new NioEventLoopGroup(100);
        serverBootstrap.group(parentPool,childPool);
        serverBootstrap.channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,100);
        serverBootstrap.childHandler(new NettyMiddleChildHandler());
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(ip, port)).sync();
        future.channel().closeFuture().sync();
    }

    /**
     * 在使用Handler的过程中，需要注意：
     1、ChannelInboundHandler之间的传递，通过调用 ctx.fireChannelRead(msg) 实现；调用ctx.write(msg) 将传递到ChannelOutboundHandler。
     2、ctx.write()方法执行后，需要调用flush()方法才能令它立即执行。
     3、ChannelOutboundHandler 在注册的时候需要放在最后一个ChannelInboundHandler之前，否则将无法传递到ChannelOutboundHandler。
     4、Handler的消费处理放在最后一个处理。
     */
    private class NettyMiddleChildHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyMiddleDecodeHandler());
            ch.pipeline().addLast(new NettyMiddleEncodeHandler());
            ch.pipeline().addLast(new NettyMiddlePrintHandler());

        }

    }
    public static void main(String[] args) throws InterruptedException {
        NettyMiddleServer server = new NettyMiddleServer("localhost",9999);
        server.start();
    }
}

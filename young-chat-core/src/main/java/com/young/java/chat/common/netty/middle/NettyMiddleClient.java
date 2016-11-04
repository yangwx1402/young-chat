package com.young.java.chat.common.netty.middle;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by young.yang on 2016/11/3.
 */
public class NettyMiddleClient {

    public void connect(String ip,int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup threadPool = new NioEventLoopGroup(100);
        bootstrap.group(threadPool).channel(NioSocketChannel.class).option(ChannelOption.SO_BACKLOG,100);
        bootstrap.handler(null);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip,port)).sync();
        future.channel().closeFuture().sync();
    }
}

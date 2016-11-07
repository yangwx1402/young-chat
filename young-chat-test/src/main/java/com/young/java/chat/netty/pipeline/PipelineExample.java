package com.young.java.chat.netty.pipeline;


import com.young.java.chat.netty.pipeline.handler.NettyInHandler1;
import com.young.java.chat.netty.pipeline.handler.NettyInHandler2;
import com.young.java.chat.netty.pipeline.handler.NettyOutHandler2;
import com.young.java.chat.netty.pipeline.handler.NetyOutHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by young.yang on 2016/11/4.
 */
public class PipelineExample {
   public void example() throws InterruptedException {
       ServerBootstrap serverBootstrap = new ServerBootstrap();
       EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
       serverBootstrap.group(eventLoopGroup,eventLoopGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,2);
       serverBootstrap.childHandler(new ChildHandlerInit());
       ChannelFuture future = serverBootstrap.bind(new InetSocketAddress("localhost",9999)).sync();
       future.channel().closeFuture().sync();
   }

    private class ChildHandlerInit extends ChannelInitializer{

        @Override
        protected void initChannel(Channel ch) throws Exception {
            /**
             * 这里要注意了,Netty的ChannelInBound和ChannelOutBound的区别
             * 1.In是处理从客户端发送来的请求,而Out是从服务器向客户端写数据
             * 2.In的执行顺序是按add顺序执行,而Out是按照add的顺序逆序执行，
             * 但是这个逆序指的是以最后一个InBound为起始点往回寻找OutBound逆序执行
             * 也就是说所有的OutBuoud应该在最后一个InBoud之前添加。
             */
            ch.pipeline().addLast(new NettyInHandler1());
            ch.pipeline().addLast(new NetyOutHandler1());
            ch.pipeline().addLast(new NettyOutHandler2());
            ch.pipeline().addLast(new NettyInHandler2());
        }
    }
    public static void main(String[] args) throws InterruptedException {
        PipelineExample example = new PipelineExample();
        example.example();
    }
}

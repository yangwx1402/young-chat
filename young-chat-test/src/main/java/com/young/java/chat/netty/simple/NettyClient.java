package com.young.java.chat.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created by young.yang on 2016/11/2.
 */
public class NettyClient {

    public void connect(String ip,int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        int threads = 100;
        EventLoopGroup threadsPool = new NioEventLoopGroup(threads);
        bootstrap.group(threadsPool).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(ip,port)).handler(new ChildClientHandler());
        ChannelFuture future = bootstrap.connect().sync();
        future.channel().closeFuture().sync();

    }

    private class ChildClientHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ClientHandler());
        }
    }

    private class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.write(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
            ctx.flush();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            byte[] temp = new byte[msg.readableBytes()];
            msg.readBytes(temp);
            System.out.println("client receive a message -["+(new String(temp,"utf-8"))+"]");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient();
        client.connect("localhost",9999);
    }
}

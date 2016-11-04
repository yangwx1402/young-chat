package com.young.java.chat.common.netty.middle.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by young.yang on 2016/11/3.
 * 采用json对数据进行反序列化
 */
public class NettyMiddleDecodeHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("1:NettyMiddleDecodeHandler active ---");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("2:NettyMiddleDecodeHandler");
       int readBytes = msg.readableBytes();
        byte[] bytes = new byte[readBytes];
        msg.readBytes(bytes);
        String json = new String(bytes,"utf-8");
        ChatMessage message = JsonUtils.fromJson(json,ChatMessage.class);
        ctx.fireChannelActive();
        ctx.fireChannelRead(message);
        ctx.fireChannelReadComplete();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("3:NettyMiddleDecodeHandler read over");
    }
}

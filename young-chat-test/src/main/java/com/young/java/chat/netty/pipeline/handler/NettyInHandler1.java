package com.young.java.chat.netty.pipeline.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by young.yang on 2016/11/4.
 */
public class NettyInHandler1 extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int readBytes = msg.readableBytes();
        byte[] bytes = new byte[readBytes];
        msg.readBytes(bytes);
        String json = new String(bytes,"utf-8");
        System.out.println("NettyInHandler1 read data - ["+json+"]");
        ChatMessage message = JsonUtils.fromJson(json,ChatMessage.class);
        ctx.fireChannelRead(message);
    }
}

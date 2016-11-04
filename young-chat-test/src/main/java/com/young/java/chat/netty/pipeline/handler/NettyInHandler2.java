package com.young.java.chat.netty.pipeline.handler;

import com.young.java.chat.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by young.yang on 2016/11/4.
 */
public class NettyInHandler2 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChatMessage message = (ChatMessage)msg;
        System.out.println("NettyInHandler2 read a object "+message.getText().getText());
        ctx.writeAndFlush(message);
    }
}

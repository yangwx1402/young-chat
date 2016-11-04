package com.young.java.chat.common.netty.middle.handler;

import com.young.java.chat.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by young.yang on 2016/11/3.
 */
public class NettyMiddlePrintHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("1:NettyMiddlePrintHandler active---");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        System.out.println("2:NettyMiddlePrintHandler");
       System.out.println("NettyMiddlePrintHandler print message "+msg.getText().getText());
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("3:NettyMiddlePrintHandler");
    }
}

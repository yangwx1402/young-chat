package com.young.java.chat.netty.pipeline.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

/**
 * Created by young.yang on 2016/11/4.
 */
public class NetyOutHandler1 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String json = (String)msg;
        ByteBuf buf = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        ctx.writeAndFlush(buf);
    }
}

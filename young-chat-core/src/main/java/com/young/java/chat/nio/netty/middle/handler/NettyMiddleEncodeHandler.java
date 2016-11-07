package com.young.java.chat.nio.netty.middle.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

/**
 * Created by young.yang on 2016/11/3.
 */
public class NettyMiddleEncodeHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ChatMessage message = (ChatMessage)msg;
        String json = JsonUtils.toJson(message);
        System.out.println("1:NettyMiddleEncodeHandler write json "+json);
        ByteBuf buf = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        ctx.write(buf);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}

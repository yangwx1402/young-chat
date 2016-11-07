package com.young.java.chat.netty.handler;

import com.young.java.chat.util.CodecUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.Charset;

/**
 * Created by young.yang on 2016/11/6.
 * 主要处理Base64加密传输
 */
public class Base64EntryHandler extends ChannelOutboundHandlerAdapter{

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String json = (String)msg;
        String encode = CodecUtils.base64Encode(json);
        ByteBuf buf = Unpooled.copiedBuffer(encode, Charset.forName("utf-8"));
        ctx.writeAndFlush(buf);
    }
}

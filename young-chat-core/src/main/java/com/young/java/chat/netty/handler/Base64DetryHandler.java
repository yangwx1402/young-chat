package com.young.java.chat.netty.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.message.LoginMessage;
import com.young.java.chat.message.TextMessage;
import com.young.java.chat.message.UserMessage;
import com.young.java.chat.util.CodecUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by young.yang on 2016/11/6.
 * 主要来实现从byte2string和Base64解密功能
 */
public class Base64DetryHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
       // System.out.println("Base64DetryHandler channelRead0 run");
        int readBytes = msg.readableBytes();
        byte[] bytes = new byte[readBytes];
        msg.readBytes(bytes);
        ctx.fireChannelRead(CodecUtils.base64Decode(new String(bytes,"utf-8")));
    }
}

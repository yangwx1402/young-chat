package com.young.java.chat.netty.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by young.yang on 2016/11/6.
 * 主要处理Ｊｓｏｎ序列化
 */
public class JsonEncoderHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //System.out.println("JsonEncoderHandler write run");
        ChatMessage message = (ChatMessage)msg;
        String json = JsonUtils.toJson(message);
        ctx.writeAndFlush(json);
    }
}

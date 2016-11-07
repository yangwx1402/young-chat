package com.young.java.chat.netty.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by young.yang on 2016/11/6.
 * 主要处理用json反序列化对象功能
 */
public class JsonDecoderHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("JsonDecoderHandler channelActive run");
        ctx.fireChannelActive();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //System.out.println("JsonDecoderHandler channelRead0 run msg - "+msg);
        ChatMessage message = JsonUtils.fromJson(msg,ChatMessage.class);
        ctx.fireChannelRead(message);
    }
}

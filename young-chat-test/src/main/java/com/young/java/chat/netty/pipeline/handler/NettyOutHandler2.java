package com.young.java.chat.netty.pipeline.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by young.yang on 2016/11/4.
 */
public class NettyOutHandler2 extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ChatMessage message = (ChatMessage)msg;
        System.out.println("NettyOutHandler2 read a object -"+message.getText().getText());
        String json = JsonUtils.toJson(message);
        ctx.write(json);
        ctx.flush();
    }
}

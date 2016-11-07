package com.young.java.chat.netty.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by young.yang on 2016/11/7.
 */
public class ClientBase64DetryHandler extends Base64DetryHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // System.out.println("ClientBase64DetryHandler channelActive run");
        ctx.fireChannelActive();
    }
}

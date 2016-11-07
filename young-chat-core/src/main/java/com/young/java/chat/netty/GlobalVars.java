package com.young.java.chat.netty;

import com.young.java.chat.message.UserMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by young.yang on 2016/11/7.
 */
public class GlobalVars {

    public static final Map<UserMessage,ChannelHandlerContext> onlineUers = new HashMap<UserMessage,ChannelHandlerContext>();

}

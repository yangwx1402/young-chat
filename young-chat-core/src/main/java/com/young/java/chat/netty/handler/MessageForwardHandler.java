package com.young.java.chat.netty.handler;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.message.UserMessage;
import com.young.java.chat.netty.GlobalVars;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by young.yang on 2016/11/6.
 * 主要处理IM业务逻辑
 */
public class MessageForwardHandler extends SimpleChannelInboundHandler<ChatMessage>{

    private ExecutorService executors;

    public MessageForwardHandler(ExecutorService executors){
        this.executors = executors;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx,final ChatMessage msg) throws Exception {
        final String token = msg.getToken();
        //為了提高性能采用多線程進行轉發
        executors.submit(new Runnable() {
             public void run() {
                 if(token==null){
                     GlobalVars.onlineUers.put(msg.getUser(),ctx);
                     msg.setToken(msg.getUser().getUsername());
                     ctx.writeAndFlush(msg);
                 }else{
                     forward(msg);
                 }
             }
         });
    }

    private void forward(ChatMessage msg) {
        List<UserMessage> users = msg.getToUsers();
        if(users==null||users.isEmpty()){
            users = getAllOnlineUser();
        }
        System.out.println(users);
        for(UserMessage user:users){
            if(!user.equals(msg.getUser())&&GlobalVars.onlineUers.containsKey(user)){
                GlobalVars.onlineUers.get(user).writeAndFlush(msg);
                System.out.println("forward user -"+user.getUsername());
            }
        }
    }

    private List<UserMessage> getAllOnlineUser() {
        return new ArrayList<UserMessage>(GlobalVars.onlineUers.keySet());
    }
}

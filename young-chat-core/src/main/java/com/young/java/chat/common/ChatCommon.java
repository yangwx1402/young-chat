package com.young.java.chat.common;

import com.young.java.chat.message.ChatMessage;
import com.young.java.chat.util.JsonUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by dell on 2016/10/31.
 */
public class ChatCommon {

    private int bufferSize = 1024 * 20;

    protected String dateFormat = "yyyy-MM-dd HH:mm:ss";

    protected final String encode = "utf-8";

    protected String readData(SocketChannel sc) throws IOException {
        if (sc.isOpen()) {
            ByteBuffer result = ByteBuffer.allocate(bufferSize);
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            int tempReadBytes = 0;
            int allReadlimit = bufferSize;
            while ((tempReadBytes = sc.read(buffer)) !=0) {
                buffer.flip();
                if (tempReadBytes < allReadlimit) {
                    result.put(buffer);
                } else {
                    allReadlimit = allReadlimit * 2;
                    ByteBuffer newBuffer = ByteBuffer.allocate(allReadlimit);
                    result.flip();
                    newBuffer.put(result);
                    result = newBuffer;
                }
            }
            result.flip();
            byte[] data = new byte[result.remaining()];
            result.get(data);
            return new String(data, encode);
        }
        return null;
    }

    protected void sendMessage(ChatMessage message,SocketChannel socketChannel) throws IOException {
        if (!socketChannel.finishConnect()) {
            System.out.println("server is not connected ");
            return;
        }
        String json = JsonUtils.toJson(message);
        //System.out.println("client send json -["+json+"]");
        byte[] temp = json.getBytes(encode);
        ByteBuffer buffer = ByteBuffer.allocate(temp.length);
        buffer.put(temp);
        buffer.flip();
        //System.out.println("client decode string - ["+new String(temp,"utf-8")+"]");
        socketChannel.write(buffer);
    }
}

package com.young.java.chat.lock;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by dell on 2016/10/25.
 */
public class WriteFileExample {
    public void writeFile(String data,String filePath) throws IOException {
        FileChannel fileChannel = new FileOutputStream(filePath).getChannel();
        byte[] temp = data.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(temp.length);
        buffer.put(temp);
        buffer.flip();
        while(buffer.hasRemaining()){
            fileChannel.write(buffer);
        }
        fileChannel.close();
    }

    public static void main(String[] args) throws IOException {
        WriteFileExample example = new WriteFileExample();
        String path = "E:\\data\\file.txt";
        example.writeFile("我们都是小黄豆",path);
    }
}

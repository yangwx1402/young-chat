package com.young.java.chat.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * Created by dell on 2016/10/24.
 */
public class FileChannelExample {
    public void copyFile(File from, File to) throws IOException {
        FileChannel fromChannel = new FileInputStream(from).getChannel();
        FileChannel toChannel = new FileOutputStream(to).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
        while (fromChannel.read(buffer) != -1) {
            //flip使buffer从写入模式变成读取模式
            buffer.flip();
            //将buffer数据写入到Channel中
            toChannel.write(buffer);
            //由于所有的io操作都是异步的,有可能write的buffer并没有全部写入,所以进行compact压缩buffer继续变为写入模式
            buffer.compact();
        }
        //可能还有剩余的byte未写入
        buffer.flip();
        while (buffer.hasRemaining()) {
            toChannel.write(buffer);
        }
        fromChannel.close();
        toChannel.close();
    }

    public void copyChannel(File from, File to) throws IOException {
        FileChannel fromChannel = new FileInputStream(from).getChannel();
        FileChannel toChannel = new FileOutputStream(to).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
        while(fromChannel.read(buffer)!=-1){
            //将buffer由写入模式反转为读取模式
            buffer.flip();
            //确保buffer中所有的数据都已经写入toChannel中
            while(buffer.hasRemaining()){
                toChannel.write(buffer);
            }
            //清空Buffer继续下次写入
            buffer.clear();
        }
    }

    public static void main(String[] args) throws IOException {
        FileChannelExample example = new FileChannelExample();
        File from = new File("E:\\data\\data.zip");
        File to = new File("E:\\data\\data_bak.zip");
      //  example.copyFile(from, to);
        example.copyChannel(from,to);
    }
}

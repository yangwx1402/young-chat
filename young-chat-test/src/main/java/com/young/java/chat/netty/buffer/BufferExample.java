package com.young.java.chat.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * Created by young.yang on 2016/11/5.
 * netty的buf都有兩個索引,跟nio里的buf不一樣
 * 其中讀索引和寫索引是分開的
 */
public class BufferExample {
    public static void byteBufExample(){
        /**
         * 创建一个java Heap Buffer,创建16字节的buf,该buf在满了以后会自动扩容
         * 因为创建HeapBuf的时候需要2哥参数,分别为初始大小和最大的Capacity,默认的max
         * 为Integer.MAXVALUE,所以buf会自动扩容
         */
        ByteBuf buf = Unpooled.buffer(16);
        //写入4个Int 一共16字节
        for(int i=0;i<4;i++){
            buf.writeInt(i);
        }
        System.out.println("write index "+buf.writerIndex());
        System.out.println("read index "+buf.readerIndex());
        System.out.println("buf capacity -"+buf.capacity());
        System.out.println(buf.getClass().getName());
        System.out.println(buf.maxCapacity());
        System.out.println(Integer.MAX_VALUE);


        ByteBuf string = Unpooled.copiedBuffer("杨勇", Charset.forName("utf-8"));
        System.out.println("write index "+string.writerIndex());
        System.out.println("read index "+string.readerIndex());
        System.out.println("buf capacity -"+string.capacity());
        System.out.println(string.maxCapacity());

        ByteBuf guding = Unpooled.buffer(16,16);
        //这里如果多写入字节就会报错
        for(int i=0;i<4;i++){
            guding.writeInt(i);
        }
        System.out.println("write index "+guding.writerIndex());
        System.out.println("read index "+guding.readerIndex());
        System.out.println("buf capacity -"+guding.capacity());
        System.out.println(guding.maxCapacity());
    }

    public static void read(){
        ByteBuf guding = Unpooled.buffer(16,16);
        for(int i=0;i<4;i++){
            guding.writeInt(i);
        }
        int readBytes = guding.readableBytes();
        for(int i=guding.readerIndex();i<readBytes;i++){
            System.out.println(guding.readByte());
        }
    }
    public static void main(String[] args){
        BufferExample.byteBufExample();
        BufferExample.read();
    }
}

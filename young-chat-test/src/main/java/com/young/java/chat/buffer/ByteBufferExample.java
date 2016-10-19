package com.young.java.chat.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created by young.yang on 2016/10/19.
 */
public class ByteBufferExample {
    /**
     * 将数据写入buffer
     * @param line
     * @return
     */
    public CharBuffer writeBuffer(String line){
        CharBuffer buffer = CharBuffer.allocate(line.toCharArray().length*2);
        char[] chars = line.toCharArray();
        for(int i=0;i<chars.length;i++){
            buffer.put(chars[i]);
        }
        return buffer;
    }

    public ByteBuffer writeByteBuffer(String line){
       ByteBuffer buffer = ByteBuffer.allocate(line.getBytes().length);
        char[] chars = line.toCharArray();
        for(int i=0;i<chars.length;i++){
            byte[] bytes = (chars[i]+"").getBytes();
            System.out.println(bytes.length);
            buffer.put(bytes);
        }
        return buffer;
    }

    /**
     * 将数据从buffer中读取出来展示
     * @param buffer
     */
    public void readBuffer(CharBuffer buffer){
        /**
         * flip()方法是buffer从写到读取的切换方法,其实底层只是将limit置为当前position的位置，而将position置为0
         */
        buffer.flip();
        while(buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }

    public void readByteBuffer(ByteBuffer buffer,int byteNum){
        byte[] temp = new byte[byteNum];
        buffer.flip();
        while(buffer.hasRemaining()){
            buffer.get(temp);
            System.out.println(new String(temp));
        }
    }

public static void main(String[] args){
    ByteBufferExample example = new ByteBufferExample();
    String line = "我们都是小黄豆";
    CharBuffer buffer = example.writeBuffer(line);
    buffer.put('哈');
    buffer.put('哈');
    example.readBuffer(buffer);

    ByteBuffer byteBuffer = example.writeByteBuffer(line);
    example.readByteBuffer(byteBuffer,2);

}
}

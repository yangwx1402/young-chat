package com.young.java.chat.lock;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by dell on 2016/10/25.
 */
public class FileLockExample {

    public void lockExample(FileChannel channel) throws IOException, InterruptedException {
        FileLock fileLock = channel.tryLock();
        if (fileLock != null) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            fileLock.channel().read(buffer);
            buffer.flip();
            Thread.sleep(10000);
            System.out.println(new String(buffer.array()));
            fileLock.release();
            channel.close();
        } else {
            System.out.println("file is locked");
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FileLockExample example = new FileLockExample();
        FileChannel channel = new RandomAccessFile("E:\\data\\file.txt", "rw").getChannel();
        example.lockExample(channel);
    }
}

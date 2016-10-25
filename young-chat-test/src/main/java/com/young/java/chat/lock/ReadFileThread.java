package com.young.java.chat.lock;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2016/10/25.
 * note:FileChannel是线程安全的,所有的操作都是原子的顺序的,
 * 多线程读取FileChannel
 */
public class ReadFileThread implements Runnable {

    private static final Object lock = new Object();

    public ReadFileThread(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    private FileChannel fileChannel;

    public void run() {
            int bytes = 2;
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            byte[] temp = new byte[bytes];
            try {
                int readSize = fileChannel.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    buffer.get(temp);
                    System.out.println(Thread.currentThread().getName() + " println" + " " + new String(temp) + " readsize = " + readSize + " channel position = " + fileChannel.position());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FileChannel channel = new FileInputStream("E:\\data\\file.txt").getChannel();
        System.out.println(channel.size());
        int threadCount = 10;
        long start = System.currentTimeMillis();
        ExecutorService executors = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executors.submit(new Thread(new ReadFileThread(channel)));
        }
        executors.shutdown();
        while (true) {
            if (executors.isTerminated()) {
                System.out.println("read cost time -[" + (System.currentTimeMillis() - start) + "]");
                break;
            }else{
                executors.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}

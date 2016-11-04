package com.young.java.chat.future.java;

import java.util.concurrent.*;

/**
 * Created by young.yang on 2016/11/3.
 */
public class CallableExample implements Callable<String> {
    public String call() throws Exception {
        return "yangyong";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new CallableExample());
        if(future.isDone()){
        /**
         * get是阻塞获取结果,当然get的时候可以设置超时时间
         */
            System.out.println(future.get());
        }
    }
}

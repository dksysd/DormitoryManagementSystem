package client;


import client.dmsRun.DefaultPage;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String host = "172.30.126.66";
        int port = 4_000;

        DefaultPage.run(host,port);
        Thread.currentThread().join();
    }
}


package client;


import client.dmsRun.DefaultPage;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String host = "localhost";
        int port = 4_000;

        DefaultPage.run(host,port);

        Thread.currentThread().join();
    }


    }


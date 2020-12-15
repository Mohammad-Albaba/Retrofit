package com.example.retrofit;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutor {

    private static AppExecutor instance;
    private final Executor networkExecutor;
    private final Executor diskIoExecutor;
    private final Executor mainExecutor;

    public static AppExecutor getInstance(){
        if (instance == null){
            instance = new AppExecutor();
        }
        return instance;
    }
     private AppExecutor(){
        networkExecutor = Executors.newFixedThreadPool(3);
        diskIoExecutor = Executors.newSingleThreadExecutor();
        mainExecutor = new MainThreadExecutor();
     }

    public Executor getNetworkExecutor() {
        return networkExecutor;
    }

    public Executor getDiskIoExecutor() {
        return diskIoExecutor;
    }

    public Executor getMainExecutor() {
        return mainExecutor;
    }

    private static class MainThreadExecutor implements Executor {

        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
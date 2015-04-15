package com.company;


import com.company.models.Task;

import java.net.HttpURLConnection;
import java.net.URL;

public class Ping implements Runnable {

    public static final int SECONDS = 1000;
    public static final int TIMEOUT = 10 * SECONDS;
    private volatile WorkerXml workerXml;
    private Task task;

    public Ping(WorkerXml workerXml, Task task) {
        this.workerXml = workerXml;
        this.task = task;
    }


    @Override
    public void run() {
        if (task == null || task.getUrl() == null || task.getUrl().isEmpty()) {
            return;
        }
        task.setAttempts(task.getAttempts() + 1L);
        URL url;
        HttpURLConnection connection = null;
        long start = System.currentTimeMillis();
        try {
            url = new URL(task.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            int responseCode = connection.getResponseCode();
            long time = System.currentTimeMillis() - start; //it's time for request
            if (responseCode == HttpURLConnection.HTTP_OK && connection.getResponseMessage() != null) {
                task.setAttemptsOk(task.getAttemptsOk() + 1L);
            }
        } catch (Exception ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        task.setTime(System.currentTimeMillis());
        workerXml.updateSyncTask(task);


        task.setTime(System.currentTimeMillis());
        workerXml.updateSyncTask(task);
    }
}

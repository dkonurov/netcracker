package com.company;

import com.company.models.Task;

import java.util.List;

/**
 * Pattern ThreadPool
 * it's a bridge for task and Ping
 */
public class Bridge implements Runnable {

    public static final int TIME_OUT = 10;

    private volatile WorkerXml workerXml;

    public Bridge(WorkerXml workerXml) {
        this.workerXml = workerXml;
    }

    @Override
    public void run() {
        int count = workerXml.getCountThread();
        Thread[] threads = new Thread[count];
        List<Task> taskList = workerXml.getTasks();
        if (taskList == null) {
            return;
        }
        int i = 0;
        while (i < taskList.size()) {
            boolean check = false;
            for (int j = 0; j < threads.length; j++) {
                if ((threads[j] != null && threads[j].isAlive()) || (threads[j] == null)) {
                    Ping ping = new Ping(workerXml, taskList.get(i));
                    threads[j] = new Thread(ping);
                    threads[j].setDaemon(true);
                    threads[j].start();
                    check = true;
                    break;
                }
            }
            if (!check) {
                try {
                    Thread.sleep(TIME_OUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                i++;
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
        try {
            Thread.sleep(workerXml.getTimeUpdate() * 1000);
            run();
        } catch (InterruptedException e) {
            return;
        }
    }
}

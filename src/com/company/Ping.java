package com.company;


import com.company.models.Task;

import java.io.IOException;

public class Ping implements Runnable {

    private volatile WorkerXml workerXml;
    private Task task;

    public Ping(WorkerXml workerXml, Task task) {
        this.workerXml = workerXml;
        this.task = task;
    }

    private Process getProcess() throws IOException {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            return Runtime.getRuntime().exec(String.format("ping -n 1 %s", task.getUrl()));
        } else if (os.toLowerCase().contains("linux")) {
            return Runtime.getRuntime().exec(String.format("ping -c 1 %s", task.getUrl()));
        } else {
            return null;
        }
    }


    @Override
    public void run() {
        if (task == null || task.getUrl() == null || task.getUrl().isEmpty()) {
            return;
        }
        task.setAttempts(task.getAttempts() + 1L);
        try {
            Process process = getProcess();
            if (process == null) {
                return;
            }
            try {
                process.waitFor();
                task.setAttemptsOk(task.getAttemptsOk() + 1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        task.setTime(System.currentTimeMillis());
        workerXml.updateSyncTask(task);
    }
}

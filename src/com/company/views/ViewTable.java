package com.company.views;

import com.company.models.Task;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class ViewTable extends View {

    private List<Task> taskList;
    private StringBuilder builder;
    private List<String> stringList;
    private static final String LEFT_ALIGN_FORMAT = "| %-8d | %-42s | %-15s | %-12d | %-14d | %-16d |%n";

    public ViewTable() {
        taskList = new ArrayList<Task>();
        stringList = new ArrayList<String>();
        builder = new StringBuilder();
    }

    public void addTask(Task task) {
        if (task != null) {
            taskList.add(task);
        }
    }

    public void addTask(List<Task> tasks) {
        taskList.addAll(tasks);
    }

    public void addString(String string) {
        stringList.add(string);
    }

    private void createHeaderTable() {
        Formatter formatter = new Formatter();
        formatter.
                format("+----------+--------------------------------------------+-----------------+--------------+----------------+------------------+%n");
        formatter.
                format("| id       |                       url                  |      Name       |   Attempts   |    Attemps ok  |      Time(ms)    |%n");
        formatter.
                format("+----------+--------------------------------------------+-----------------+--------------+----------------+------------------+%n");
        builder.append(formatter.toString());
    }

    @Override
    public void print() {
        createHeaderTable();
        Formatter formatter = new Formatter();
        if (taskList.size() >= 1) {
            for (Task task : taskList) {
                if (task != null) {
                    formatter.format(LEFT_ALIGN_FORMAT, task.getId(),
                            task.getUrl(), task.getName(), task.getAttempts(), task.getAttemptsOk(),
                            (task.getTime() != 0) ? System.currentTimeMillis() - task.getTime() : task.getTime());
                }
            }
        } else {
            formatter.format("sorry not found\n");
        }
        builder.append(formatter.toString());
        createFooterTable();
        System.out.println(builder.toString());

        if (isLastPrintln()) {
            for (int i = 0; i < stringList.size() - 1; i++) {
                System.out.println(stringList.get(i));
            }
            System.out.print(stringList.get(stringList.size() - 1));
        } else {
            for (String string : stringList) {
                System.out.println(string);
            }
        }
    }

    private void createFooterTable() {
        Formatter formatter = new Formatter();
        builder.append(formatter.
                format("+----------+--------------------------------------------+-----------------+--------------+----------------+------------------+%n").toString());
    }
}

package com.company.models;


import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Messages {

    public static final String DEAFULT = "if you don't know commands please write help";
    public static final String PREPARE_EXIT = "Are you sure want exit? \n Yes(y) \\ No(n)";
    public static final String GOODBYE = "goodbye! thank you for use this application";
    public static final String HELLO = "THIS IS TEST TASK! HELLO!";
    public static final String HELP = "commands :\nadd\n\tAdd new task:\n\t-URL (string format in http://google.com:80/)\n\t-name\nshow\n\tshow all task\nget\n\tshow task by id:\n\t-id\nsave\n\tsave all tasks\ndelete\n\tDelete node by id:\n\t\t-id";
    public static final String ADD_HELP = "\tURL : http://google.com:80/\n\tname : google";
    public static final String ADD_SUBMIT = "task added ";
    public static final String ADD_ERROR = "sorry can't add new task";
    public static final String GET_NOT_DIGITS = "id is not digits";
    private static final String LEFT_ALIGN_FORMAT = "| %-8d | %-42s | %-15s | %-12d | %-14d | %-16d |%n";
    public static final String SAVE = "all tasks is save";
    public static final String ERROR = "sorry errors";
    public static final String DELETE = "task is delete";
    public static final String ERROR_VALIDATE = "errors validate";
    public static final String ERROR_VALIDATE_LENGTH = "-id";
    public static final String ID_EMPTY = "id is empty";
    public static final String ADD_ERROR_URL = "incorrect url";

    private final StringBuilder builder;
    private final List<Task> taskList;

    public Messages() {
        builder = new StringBuilder();
        taskList = new ArrayList<Task>();
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
    public String toString() {
        return builder.toString();
    }

    /**
     * generate println table
     * @return
     */
    public String returnTable() {
        createHeaderTable();
        Formatter formatter = new Formatter();
        for (Task task : taskList) {
            if (task != null) {
                formatter.format(LEFT_ALIGN_FORMAT, task.getId(),
                        task.getUrl(), task.getName(), task.getAttempts(), task.getAttemptsOk(),
                        (task.getTime() != 0) ? System.currentTimeMillis() - task.getTime() : task.getTime());
            }
        }
        builder.append(formatter.toString());
        createFooterTable();
        return builder.toString();
    }

    private void createFooterTable() {
        Formatter formatter = new Formatter();
        builder.append(formatter.
                format("+----------+--------------------------------------------+-----------------+--------------+----------------+------------------+%n").toString());
    }

    public void putTask(Task task) {
        taskList.add(task);
    }

    public void putListTask(List<Task> taskList) {
        this.taskList.addAll(taskList);
    }
}

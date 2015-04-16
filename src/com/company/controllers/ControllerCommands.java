package com.company.controllers;

import com.company.Bridge;
import com.company.WorkerXml;
import com.company.models.Messages;
import com.company.models.Task;
import com.company.models.TypeCommands;
import com.company.views.View;
import com.company.views.ViewMessage;

import java.util.ArrayList;
import java.util.List;

public class ControllerCommands {

    private boolean isPrepareExit = false;
    private boolean isFinish = false;

    private volatile WorkerXml workerXml;
    private Thread threadBridge;
    private TypeCommands typeCommands;

    public ControllerCommands() {
        workerXml = new WorkerXml();
        typeCommands = new TypeCommands();
        updatePing();
    }

    public int getIdCommandsByName(String name) {
        return typeCommands.getIdByName(name);
    }

    /**
     * start thread for ping tasks
     */
    private void updatePing() {
        Bridge bridge = new Bridge(workerXml);
        threadBridge = new Thread(bridge);
        threadBridge.start();
    }

    /**
     * delete task of id
     *
     * @param splitCommands array String
     * @return information about delete
     */
    public void deleteTask(String[] splitCommands, View view) {
        Integer id = Integer.parseInt(splitCommands[1]);
        boolean result = workerXml.delete(id);
        List<String> list = new ArrayList<String>();
        if (result) {
            list.add(Messages.DELETE);
        } else {
            list.add(Messages.ERROR);
        }
        list.add(Messages.IN_COMMANDS);
        printView(view, list);
    }

    /**
     * use for collect information into view and display
     * @param view View
     * @param stringList List<String>
     */
    private void printView(View view, List<String> stringList) {
        printView(view, stringList, null);
    }

    /**
     * use for collect information into view and display
     * @param view View
     * @param stringList List<String>
     * @param taskList List<Task>
     */
    private void printView(View view, List<String> stringList, List<Task> taskList) {
        if (view == null) {
            return;
        }
        if (stringList != null && stringList.size() >= 1) {
            for (String string : stringList) {
                view.add(string);
            }
        }
        if (taskList != null && taskList.size() >= 1) {
            for (Task task : taskList) {
                view.add(task);
            }
        }
        view.print();
    }

    /**
     * generate table for current task of id
     *
     * @param splitCommands array String
     * @return table for current task
     */
    public void getById(String[] splitCommands, View view) {
        Integer id = Integer.parseInt(splitCommands[1]);
        Task task = workerXml.getById(id);
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.IN_COMMANDS);
        List<Task> taskList = new ArrayList<Task>();
        taskList.add(task);
        printView(view, stringList, taskList);
    }

    /**
     * generate table show
     *
     * @return table show
     */
    public void show(View view) {
        List<Task> taskList = workerXml.getTasks();
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList, taskList);
    }

    /**
     * add table into xml
     *
     * @param splitCommands array String
     * @return String added
     */
    public void addTask(String[] splitCommands, View view) {
        String url = splitCommands[1];
        String name = splitCommands[2];
        Task task = new Task();
        task.setUrl(url);
        task.setName(name);
        boolean added = workerXml.addTask(task);
        List<String> stringList = new ArrayList<String>();
        if (added) {
            stringList.add(Messages.ADD_SUBMIT);
        } else {
            stringList.add(Messages.ADD_ERROR);
        }
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    public boolean getIsFinish() {
        return isFinish;
    }


    /**
     * show view when start
     */
    public void start() {
        ViewMessage viewMessage = new ViewMessage();
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.HELLO);
        stringList.add(Messages.DEFAULT);
        stringList.add(Messages.IN_COMMANDS);
        printView(viewMessage, stringList);
    }

    /**
     * show view if empty string
     */
    public void emptyString() {
        ViewMessage viewMessage = new ViewMessage();
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.EMPTY_INPUT);
        stringList.add(Messages.IN_COMMANDS);
        printView(viewMessage, stringList);
    }

    public boolean isPrepareExit() {
        return isPrepareExit;
    }

    /**
     * finish application
     * @param view View
     */
    public void exitYes(View view) {
        isFinish = true;
        threadBridge.interrupt();
        try {
            threadBridge.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.GOODBYE);
        if (view != null) {
            view.setLastPrintln(false);
        }
        printView(view, stringList);
    }

    /**
     * if accidentally set exit
     * @param view View
     */
    public void exitNo(View view) {
        isPrepareExit = false;
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * start exit applcation
     * @param view View
     */
    public void exit(View view) {
        isPrepareExit = true;
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.PREPARE_EXIT);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * show help information
     * @param view View
     */
    public void help(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.HELP);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * show view information add help
     * @param view View
     */
    public void addHelp(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.ADD_HELP);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * show error add url
     * @param view View
     */
    public void addErrorUrl(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.ADD_ERROR_URL);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * incorrect id
     * @param view View
     */
    public void errorId(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.ID_NOT_DIGITS);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * show information for command get
     * @param view View
     */
    public void getHelp(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.GET_HELP);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * save tasks
     * @param view View
     */
    public void save(View view) {
        boolean result = workerXml.save();
        List<String> stringList = new ArrayList<String>();
        if (result) {
            stringList.add(Messages.SAVE);
        } else {
            stringList.add(Messages.ERROR);
        }
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }

    /**
     * show information about delete task
     * @param view View
     */
    public void deleteHelp(View view) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(Messages.DELETE_HELP);
        stringList.add(Messages.IN_COMMANDS);
        printView(view, stringList);
    }
}

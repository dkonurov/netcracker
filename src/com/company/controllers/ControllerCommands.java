package com.company.controllers;

import com.company.Bridge;
import com.company.WorkerXml;
import com.company.models.Messages;
import com.company.models.Task;
import com.company.models.TypeCommands;
import com.company.views.View;
import com.company.views.ViewMessage;
import com.company.views.ViewTable;

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
    public void deleteTask(String[] splitCommands) {
        Integer id = Integer.parseInt(splitCommands[1]);
        boolean result = workerXml.delete(id);
        ViewMessage viewMessage = new ViewMessage();
        if (result) {
            viewMessage.addMessage(Messages.DELETE);
        } else {
            viewMessage.addMessage(Messages.ERROR);
        }
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    /**
     * generate table for current task of id
     *
     * @param splitCommands array String
     * @return table for current task
     */
    public void getById(String[] splitCommands) {
        Integer id = Integer.parseInt(splitCommands[1]);
        Task task = workerXml.getById(id);
        ViewTable viewTable = new ViewTable();
        viewTable.addTask(task);
        viewTable.addString(Messages.IN_COMMANDS);
        viewTable.print();
    }

    /**
     * generate table show
     *
     * @return table show
     */
    public void show() {
        List<Task> taskList = workerXml.getTasks();
        ViewTable viewTable = new ViewTable();
        viewTable.addTask(taskList);
        viewTable.addString(Messages.IN_COMMANDS);
        viewTable.print();
    }

    /**
     * add table into xml
     *
     * @param splitCommands array String
     * @return String added
     */
    public void addTask(String[] splitCommands) {
        String url = splitCommands[1];
        String name = splitCommands[2];
        Task task = new Task();
        task.setUrl(url);
        task.setName(name);
        boolean added = workerXml.addTask(task);
        ViewMessage viewMessage = new ViewMessage();
        if (added) {
            viewMessage.addMessage(Messages.ADD_SUBMIT);
        } else {
            viewMessage.addMessage(Messages.ADD_ERROR);
        }
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public boolean getIsFinish() {
        return isFinish;
    }

    public void start() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.HELLO);
        viewMessage.addMessage(Messages.DEFAULT);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void emptyString() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.EMPTY_INPUT);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public boolean isPrepareExit() {
        return isPrepareExit;
    }

    public void exitYes() {
        isFinish = true;
        threadBridge.interrupt();
        try {
            threadBridge.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.GOODBYE);
        viewMessage.print();
    }

    public void exitNo() {
        isPrepareExit = false;
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void exit() {
        isPrepareExit = true;
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.PREPARE_EXIT);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void help() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.HELP);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void addHelp() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.ADD_HELP);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void addErrorUrl() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.ADD_ERROR_URL);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void errorId() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.ID_NOT_DIGITS);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void getHelp() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.GET_HELP);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void save() {
        boolean result = workerXml.save();
        ViewMessage viewMessage = new ViewMessage();
        if (result) {
            viewMessage.addMessage(Messages.SAVE);
        } else {
            viewMessage.addMessage(Messages.ERROR);
        }
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }

    public void deleteHelp() {
        ViewMessage viewMessage = new ViewMessage();
        viewMessage.addMessage(Messages.DELETE_HELP);
        viewMessage.addMessage(Messages.IN_COMMANDS);
        viewMessage.print();
    }
}

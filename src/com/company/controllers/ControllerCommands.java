package com.company.controllers;

import com.company.Bridge;
import com.company.WorkerXml;
import com.company.models.Messages;
import com.company.models.Task;
import com.company.models.TypeCommands;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;

public class ControllerCommands {

    private boolean isPrepareExit = false;
    private boolean isFinish = false;

    private volatile WorkerXml workerXml;
    private Thread threadBridge;

    public ControllerCommands() {
        workerXml = new WorkerXml();
        updatePing();
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
     * parse commands from string and return id for result
     *
     * @param textCommand String
     * @return String result
     */
    public String switcher(String textCommand) {
        if (textCommand == null || textCommand.isEmpty()) {
            return firstShow();
        }
        String[] splitCommands = textCommand.split(" ");
        if (isPrepareExit) {
            if (splitCommands[0].equals(TypeCommands.YES)) {
                isFinish = true;
                threadBridge.interrupt();
                try {
                    threadBridge.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Messages.GOODBYE;
            } else if (splitCommands[0].equals(TypeCommands.NO)) {
                isPrepareExit = false;
                return null;
            }
        }
        if (splitCommands[0].equals(TypeCommands.EXIT)) {
            isPrepareExit = true;
            return Messages.PREPARE_EXIT;
        } else if (splitCommands[0].equals(TypeCommands.HELP)) {
            return Messages.HELP;
        } else if (splitCommands[0].equals(TypeCommands.ADD)) {
            return addTask(splitCommands);
        } else if (splitCommands[0].equals(TypeCommands.SHOW)) {
            return show();
        } else if (splitCommands[0].equals(TypeCommands.GET)) {
            return getById(splitCommands);
        } else if (splitCommands[0].equals(TypeCommands.SAVE)) {
            boolean result = workerXml.save();
            if (result) {
                return Messages.SAVE;
            } else {
                return Messages.ERROR;
            }
        } else if (splitCommands[0].equals(TypeCommands.DELETE)) {
            return deleteTask(splitCommands);
        } else {
            return Messages.DEAFULT;
        }
    }

    /**
     * delete task of id
     * @param splitCommands array String
     * @return information about delete
     */
    private String deleteTask(String[] splitCommands) {
        if (splitCommands.length != 2) {
            return Messages.ERROR_VALIDATE_LENGTH;
        } else {
            if (splitCommands[1] == null || splitCommands[1].isEmpty()) {
                return Messages.ID_EMPTY;
            }
            try {
                Integer id = Integer.parseInt(splitCommands[1]);
                boolean result = workerXml.delete(id);
                if (result) {
                    return Messages.DELETE;
                } else {
                    return Messages.ERROR;
                }
            }catch (NumberFormatException e) {
                return Messages.ERROR_VALIDATE;
            }
        }
    }

    /**
     * generate table for current task of id
     * @param splitCommands array String
     * @return table for current task
     */
    private String getById(String[] splitCommands) {
        if (splitCommands.length != 2) {
            return Messages.ERROR_VALIDATE_LENGTH;
        } else {
            if (splitCommands[1] == null || splitCommands[1].isEmpty()) {
                return Messages.ID_EMPTY;
            }
            try {
                Integer id = Integer.parseInt(splitCommands[1]);
                Task task = workerXml.getById(id);
                Messages messages = new Messages();
                messages.putTask(task);
                return messages.returnTable();
            } catch (NumberFormatException e) {
                return Messages.GET_NOT_DIGITS;
            }
        }
    }

    /**
     * generate table show
     * @return table show
     */
    private String show() {
        Messages messages = new Messages();
        List<Task> taskList = workerXml.getTasks();
        messages.putListTask(taskList);
        return messages.returnTable();
    }

    /**
     * add table into xml
     * @param splitCommands array String
     * @return String added
     */
    private String addTask(String[] splitCommands) {
        String url;
        String name;
        if (splitCommands.length != 3 || splitCommands[1] == null || splitCommands[2] == null
                || splitCommands[1].isEmpty() || splitCommands[2].isEmpty()) {
            return Messages.ADD_HELP;
        } else {
            url = splitCommands[1];
            name = splitCommands[2];
            if (validateAdd(url, name)) return Messages.ADD_ERROR_URL;
            Task task = new Task();
            task.setUrl(url);
            task.setName(name);
            boolean added = workerXml.addTask(task);
            if (added) {
                return Messages.ADD_SUBMIT;
            } else {
                return Messages.ADD_ERROR;
            }
        }
    }

    /**
     * validate current url and name add task
     * @param url String
     * @param name String
     * @return boolean isValidate
     */
    private boolean validateAdd(String url, String name) {
        UrlValidator urlValidator = new UrlValidator();
        if (url == null || url.isEmpty()) {
            return true;
        }
        if (!urlValidator.isValid(url) || (!url.endsWith(":80/") && !url.endsWith(":80"))) {
            return true;
        }
        if (name == null || name.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean getIsFinish() {
        return isFinish;
    }

    public String hello() {
        return Messages.HELLO;
    }

    public String firstShow() {
        return Messages.DEAFULT;
    }
}

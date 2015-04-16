package com.company.views;

import com.company.controllers.ControllerCommands;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.models.TypeCommands.*;

public class InputView {

    private ControllerCommands controllerCommands;

    public InputView() {
        controllerCommands = new ControllerCommands();
    }

    public void work() {
        controllerCommands.start();
        do {
            Scanner scan = new Scanner(System.in);
            String textCommand = scan.nextLine();
            switcher(textCommand);
        } while (!controllerCommands.getIsFinish());
    }


    /**
     * parse commands from string and return id for result
     *
     * @param textCommand String
     */
    public void switcher(String textCommand) {
        if (textCommand == null || textCommand.isEmpty()) {
            controllerCommands.emptyString();
            return;
        }
        String[] splitCommands = textCommand.split(" ");
        int commandsId = controllerCommands.getIdCommandsByName(splitCommands[0]);
        switch (commandsId) {
            case YES_ID:
                if (controllerCommands.isPrepareExit()) {
                    controllerCommands.exitYes(new ViewMessage());
                }
                break;
            case NO_ID:
                controllerCommands.exitNo(new ViewMessage());
                break;
            case EXIT_ID:
                controllerCommands.exit(new ViewMessage());
                break;
            case HELP_ID:
                controllerCommands.help(new ViewMessage());
                break;
            case ADD_ID:
                if (!validateAdd(splitCommands)) {
                    return;
                }
                controllerCommands.addTask(splitCommands, new ViewMessage());
                break;
            case SHOW_ID:
                controllerCommands.show(new ViewTable());
                break;
            case GET_ID:
                if (!validateGet(splitCommands)) {
                    return;
                }
                controllerCommands.getById(splitCommands, new ViewTable());
                break;
            case SAVE_ID:
                controllerCommands.save(new ViewMessage());
                break;
            case DELETE_ID:
                if (!validateDelete(splitCommands)) {
                    return;
                }
                controllerCommands.deleteTask(splitCommands, new ViewMessage());
                break;
            default:
                controllerCommands.help(new ViewMessage());
        }
    }

    /**
     * validate before delete task
     * @param splitCommands String[]
     * @return boolean validate
     */
    private boolean validateDelete(String[] splitCommands) {
        boolean validate = validateNotEmpty(splitCommands, 2);
        if (!validate) {
            controllerCommands.deleteHelp(new ViewMessage());
            return false;
        }
        try {
            Integer id = Integer.parseInt(splitCommands[1]);
        } catch (NumberFormatException e) {
            validate = false;
            controllerCommands.errorId( new ViewMessage());
        }
        return validate;
    }

    /**
     * validate before get information
     * @param splitCommands String[]
     * @return boolean validate
     */
    private boolean validateGet(String[] splitCommands) {
        boolean validate = validateNotEmpty(splitCommands, 2);
        if (!validate) {
            controllerCommands.getHelp( new ViewMessage());
            return false;
        }
        try {
            Integer id = Integer.parseInt(splitCommands[1]);
        } catch (NumberFormatException e) {
            validate = false;
            controllerCommands.errorId(new ViewMessage());
        }
        return validate;
    }

    /**
     * validate before add task
     * @param splitCommands String[]
     * @return boolean validate
     */
    private boolean validateAdd(String[] splitCommands) {
        if (!validateNotEmpty(splitCommands, 3)) {
            controllerCommands.addHelp(new ViewMessage());
            return false;
        }
        if (!validateUrl(splitCommands)) {
            controllerCommands.addErrorUrl(new ViewMessage());
            return false;
        }
        return true;
    }

    /**
     * validate url
     * @param splitCommands String[]
     * @return boolean validate
     */
    private boolean validateUrl(String[] splitCommands) {
        String url = splitCommands[1];
        Pattern urlPattern = Pattern.compile("((https?|ftp|telnet)?://)?([a-zA-Z0-9-]{1,128}\\.)+([a-zA-Z]{2,4})+(:[0-9]{0,5})?(/[a-zA-Z0-9.,_@%&?+=\\~/#-]*)?");
        Matcher urlMatcher = urlPattern.matcher(url);
        boolean validate = urlMatcher.find();
        if (!url.endsWith(":80/") && !url.endsWith(":80")) {
            validate = false;
        }
        return validate;
    }

    /**
     * validate for empty element
     * @param commands String[]
     * @param count int
     * @return boolean validate
     */
    private boolean validateNotEmpty(String[] commands, int count) {
        boolean validate = true;
        if (commands.length != count) {
            validate = false;
        }
        for (String command : commands) {
            if (command == null || command.isEmpty()) {
                validate = false;
            }
        }
        return validate;
    }
}

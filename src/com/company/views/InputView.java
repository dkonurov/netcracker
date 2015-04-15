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
                    controllerCommands.exitYes();
                }
                break;
            case NO_ID:
                controllerCommands.exitNo();
                break;
            case EXIT_ID:
                controllerCommands.exit();
                break;
            case HELP_ID:
                controllerCommands.help();
                break;
            case ADD_ID:
                if (!validateAdd(splitCommands)) {
                    return;
                }
                controllerCommands.addTask(splitCommands);
                break;
            case SHOW_ID:
                controllerCommands.show();
                break;
            case GET_ID:
                if (!validateGet(splitCommands)) {
                    return;
                }
                controllerCommands.getById(splitCommands);
                break;
            case SAVE_ID:
                controllerCommands.save();
                break;
            case DELETE_ID:
                if (!validateDelete(splitCommands)) {
                    return;
                }
                controllerCommands.deleteTask(splitCommands);
                break;
            default:
                controllerCommands.help();
        }
    }

    private boolean validateDelete(String[] splitCommands) {
        boolean validate = validateNotEmpty(splitCommands, 2);
        if (!validate) {
            controllerCommands.deleteHelp();
            return false;
        }
        try {
            Integer id = Integer.parseInt(splitCommands[1]);
        } catch (NumberFormatException e) {
            validate = false;
            controllerCommands.errorId();
        }
        return validate;
    }

    private boolean validateGet(String[] splitCommands) {
        boolean validate = validateNotEmpty(splitCommands, 2);
        if (!validate) {
            controllerCommands.getHelp();
            return false;
        }
        try {
            Integer id = Integer.parseInt(splitCommands[1]);
        } catch (NumberFormatException e) {
            validate = false;
            controllerCommands.errorId();
        }
        return validate;
    }

    private boolean validateAdd(String[] splitCommands) {
        if (!validateNotEmpty(splitCommands, 3)) {
            controllerCommands.addHelp();
            return false;
        }
        if (!validateUrl(splitCommands)) {
            controllerCommands.addErrorUrl();
            return false;
        }
        return true;
    }

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

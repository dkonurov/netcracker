package com.company;

import com.company.controllers.ControllerCommands;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ControllerCommands controllerCommands = new ControllerCommands();
        System.out.println(controllerCommands.hello());
        System.out.println(controllerCommands.firstShow());
        do {
            Scanner scan = new Scanner(System.in);
            String textCommand = scan.nextLine();
            String message = controllerCommands.switcher(textCommand);
            if (message != null) {
                System.out.println(message);
            }
        } while (!controllerCommands.getIsFinish());
    }
}

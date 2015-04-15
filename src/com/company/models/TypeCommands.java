package com.company.models;

import java.util.HashMap;
import java.util.Map;

public class TypeCommands {

    private static final String EXIT = "exit";
    private static final String ADD = "add";
    private static final String DELETE = "delete";
    private static final String SHOW = "show";
    private static final String GET = "get";
    private static final String SAVE = "save";
    private static final String YES = "y";
    private static final String NO = "n";
    private static final String HELP = "help";

    public static final int EXIT_ID = 0;
    public static final int ADD_ID = 1;
    public static final int DELETE_ID = 2;
    public static final int SHOW_ID = 3;
    public static final int GET_ID = 4;
    public static final int SAVE_ID = 5;
    public static final int YES_ID = 6;
    public static final int NO_ID = 7;
    public static final int HELP_ID = 8;

    private Map<String, Integer> mapCommands;

    public TypeCommands() {
        mapCommands = new HashMap<String, Integer>();
        mapCommands.put(EXIT, EXIT_ID);
        mapCommands.put(ADD, ADD_ID);
        mapCommands.put(DELETE, DELETE_ID);
        mapCommands.put(SHOW, SHOW_ID);
        mapCommands.put(GET, GET_ID);
        mapCommands.put(SAVE, SAVE_ID);
        mapCommands.put(YES, YES_ID);
        mapCommands.put(NO, NO_ID);
        mapCommands.put(HELP, HELP_ID);
    }

    public int getIdByName(String name) {
        Integer id = mapCommands.get(name);
        if (id == null) {
            return -1;
        } else {
            return id;
        }
    }
}

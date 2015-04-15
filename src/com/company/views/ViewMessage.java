package com.company.views;

import java.util.ArrayList;

public class ViewMessage extends View {

    private ArrayList<String> list;

    public ViewMessage() {
        list = new ArrayList<String>();
    }

    public void addMessage(String message) {
        list.add(message);
    }

    @Override
    public void print() {
        if (isLastPrintln()) {
            for (int i = 0; i < list.size() - 1; i++) {
                System.out.println(list.get(i));
            }
            System.out.print(list.get(list.size() - 1));
        } else {
            for (String string : list) {
                System.out.println(string);
            }
        }
    }
}

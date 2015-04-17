package com.company.views;

import java.util.ArrayList;

public class ViewMessage extends View {

    private ArrayList<String> list;

    public ViewMessage() {
        list = new ArrayList<String>();
    }

    private void addMessage(String message) {
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

    @Override
    public void add(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof String) {
            addMessage((String) object);
        }
    }
}

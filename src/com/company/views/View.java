package com.company.views;

abstract public class View  {

    private boolean isLastPrintln = true;

    public boolean isLastPrintln() {
        return isLastPrintln;
    }

    public void setLastPrintln(boolean isLastPrintln) {
        this.isLastPrintln = isLastPrintln;
    }

    abstract public void print();
}

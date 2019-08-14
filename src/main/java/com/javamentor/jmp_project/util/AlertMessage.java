package com.javamentor.jmp_project.util;

public class AlertMessage {

    private String message;
    private boolean show = true;

    public AlertMessage(String message) {
        this.message = message;
    }

    public boolean isShow() {
        return show;
    }

    @Override
    public String toString() {
        if (show) {
            show = false;
            return message;
        }
        return null;
    }

}

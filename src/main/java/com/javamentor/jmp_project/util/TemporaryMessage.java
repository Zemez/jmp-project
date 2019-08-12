package com.javamentor.jmp_project.util;

public class TemporaryMessage {

    private String message;
    private boolean show = true;

    public TemporaryMessage(String message) {
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

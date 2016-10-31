package com.young.java.chat.message;

/**
 * Created by dell on 2016/10/31.
 */
public class TextMessage {
    public UserMessage getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserMessage fromUser) {
        this.fromUser = fromUser;
    }

    private UserMessage fromUser;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

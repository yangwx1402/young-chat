package com.young.java.chat.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/10/31.
 */
public class ChatMessage {

    private LoginMessage login;

    private UserMessage user;

    private TextMessage text;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private long sendTime;

    private List<UserMessage> toUsers = new ArrayList<UserMessage>();

    public LoginMessage getLogin() {
        return login;
    }

    public void setLogin(LoginMessage login) {
        this.login = login;
    }

    public UserMessage getUser() {
        return user;
    }

    public void setUser(UserMessage user) {
        this.user = user;
    }

    public TextMessage getText() {
        return text;
    }

    public void setText(TextMessage text) {
        this.text = text;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public List<UserMessage> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<UserMessage> toUsers) {
        this.toUsers = toUsers;
    }
}

package com.young.java.chat.message;

/**
 * Created by dell on 2016/10/31.
 */
public class UserMessage {

    private String username;

    private String gender;

    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserMessage) {
            UserMessage user = (UserMessage) obj;
            return this.username.equals(user.getUsername());
        } else {
            return false;
        }
    }
}

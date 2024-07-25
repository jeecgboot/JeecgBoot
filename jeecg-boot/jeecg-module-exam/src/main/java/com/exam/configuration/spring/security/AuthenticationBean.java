package com.exam.configuration.spring.security;

/**
 * @version 3.5.0
 * @description: The type Authentication bean.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class AuthenticationBean {
    private String userName;
    private String password;
    private boolean remember;

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Is remember boolean.
     *
     * @return the boolean
     */
    public boolean isRemember() {
        return remember;
    }

    /**
     * Sets remember.
     *
     * @param remember the remember
     */
    public void setRemember(boolean remember) {
        this.remember = remember;
    }

}

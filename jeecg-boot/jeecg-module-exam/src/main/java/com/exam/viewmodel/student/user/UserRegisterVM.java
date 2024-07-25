package com.exam.viewmodel.student.user;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRegisterVM {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotNull
    private Integer userLevel;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }
}

package cn.mrray.raybaas.demo.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private Long userBalance;

    public User(String userName, Long userBalance) {
        this.userName = userName;
        this.userBalance = userBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Long userBalance) {
        this.userBalance = userBalance;
    }
}

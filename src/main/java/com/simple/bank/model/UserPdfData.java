package com.simple.bank.model;

public class UserPdfData {
    private String userName;
    private Integer userAccountNumber;
    private String userAccountName;
    private Double depositAmount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAccountNumber() {
        return userAccountNumber;
    }

    public void setUserAccountNumber(Integer userAccountNumber) {
        this.userAccountNumber = userAccountNumber;
    }

    public String getUserAccountName() {
        return userAccountName;
    }

    public void setUserAccountName(String userAccountName) {
        this.userAccountName = userAccountName;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }
}

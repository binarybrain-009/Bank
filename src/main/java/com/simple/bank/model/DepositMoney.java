package com.simple.bank.model;

public class DepositMoney {

    private Integer senderID;
    private Integer senderAccountNumber;
    private Integer receiverID;
    private Integer receiverAccountNumber;
    private Double transferAmount;

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public Integer getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(Integer senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public Integer getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Integer receiverID) {
        this.receiverID = receiverID;
    }

    public Integer getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(Integer receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }
}

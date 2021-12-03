package com.simple.bank.requestDto;


import com.sun.istack.NotNull;

public class EmployeeDto {
    @NotNull
    private String name;

    @NotNull
    private String contactNumber;

    @NotNull
    private String birthday;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String role;

    public EmployeeDto() {
    }

    public EmployeeDto(String name, String contactNumber, String birthday, String email, String password, String role) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

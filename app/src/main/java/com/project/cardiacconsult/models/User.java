package com.project.cardiacconsult.models;


import com.project.cardiacconsult.utils.RoleEnum;

public class User {

    private String userName;

    private String address;

    private String phoneNO;

    private String email;

    private RoleEnum role;

    public User() {
    }

    public User(String userName, String address, String phoneNO, String email, RoleEnum role) {

        this.userName = userName;
        this.address = address;
        this.phoneNO = phoneNO;
        this.email = email;
        this.role = role;
    }



    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNO() {
        return phoneNO;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNO='" + phoneNO + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}

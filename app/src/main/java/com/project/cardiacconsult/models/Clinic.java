package com.project.cardiacconsult.models;

public class Clinic {

    private String name;

    private String address;

    private String contactNo;

    private String licenceNo;

    //private String physicianEmail;

    public Clinic() {
    }

    public Clinic(String name, String address, String contactNo, String licenceNo/*, String physicianEmail*/) {
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.licenceNo = licenceNo;
        //this.physicianEmail = physicianEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    /*public String getPhysicianEmail() {
        return physicianEmail;
    }

    public void setPhysicianEmail(String physicianEmail) {
        this.physicianEmail = physicianEmail;
    }*/

    @Override
    public String toString() {
        return "Clinic{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", licenceNo='" + licenceNo + '\'' +/*
                ", physicianEmail='" + physicianEmail + '\'' +*/
                '}';
    }
}

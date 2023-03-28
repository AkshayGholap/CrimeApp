package com.example.crimereport.model;

public class missingComplaintModel {

    String Name,  Age,  Gender,  Height,  Location,  Desc,  Phone,  ImageUri,  CrimeId,  CrimeRegisterDate ,Status;


    public missingComplaintModel() {
    }

    public missingComplaintModel(String name, String age, String gender, String height, String location, String desc, String phone, String imageUri, String crimeId, String crimeRegisterDate, String status) {
        Name = name;
        Age = age;
        Gender = gender;
        Height = height;
        Location = location;
        Desc = desc;
        Phone = phone;
        ImageUri = imageUri;
        CrimeId = crimeId;
        CrimeRegisterDate = crimeRegisterDate;
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getCrimeId() {
        return CrimeId;
    }

    public void setCrimeId(String crimeId) {
        CrimeId = crimeId;
    }

    public String getCrimeRegisterDate() {
        return CrimeRegisterDate;
    }

    public void setCrimeRegisterDate(String crimeRegisterDate) {
        CrimeRegisterDate = crimeRegisterDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

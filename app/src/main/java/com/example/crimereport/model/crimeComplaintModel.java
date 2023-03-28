package com.example.crimereport.model;

public class crimeComplaintModel {

    String ComplainerName,VictimName,  VictimAge,  VictimGender,  Location,  Desc,  Phone,  ImageUri,  CrimeId,  CrimeRegisterDate ,Status;

    public crimeComplaintModel() {
    }

    public crimeComplaintModel(String complainerName, String victimName, String victimAge, String victimGender, String location, String desc, String phone, String imageUri, String crimeId, String crimeRegisterDate, String status) {
        ComplainerName = complainerName;
        VictimName = victimName;
        VictimAge = victimAge;
        VictimGender = victimGender;
        Location = location;
        Desc = desc;
        Phone = phone;
        ImageUri = imageUri;
        CrimeId = crimeId;
        CrimeRegisterDate = crimeRegisterDate;
        Status = status;
    }

    public String getComplainerName() {
        return ComplainerName;
    }

    public void setComplainerName(String complainerName) {
        ComplainerName = complainerName;
    }

    public String getVictimName() {
        return VictimName;
    }

    public void setVictimName(String victimName) {
        VictimName = victimName;
    }

    public String getVictimAge() {
        return VictimAge;
    }

    public void setVictimAge(String victimAge) {
        VictimAge = victimAge;
    }

    public String getVictimGender() {
        return VictimGender;
    }

    public void setVictimGender(String victimGender) {
        VictimGender = victimGender;
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

package com.nttung.oufood.Class;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String idUser;
    private String name;
    private String phone;
    private String email;
    private String url;
    private List<AnAddress> Addresses;

    public User() {
    }

    public User(String idUser, String name, String phone, String email, String url, List<AnAddress> addresses) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.url = url;
        Addresses = addresses;
    }

    public void addAddress(AnAddress address) {
        if (this.Addresses == null) {
            this.Addresses = new ArrayList<>();
        }
        this.Addresses.add(address);
    }

    //getter + setter/////////////////////////////////////////////////////////

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<AnAddress> getAddresses() {
        return Addresses;
    }

    public void setAddresses(List<AnAddress> addresses) {
        this.Addresses = addresses;
    }
}

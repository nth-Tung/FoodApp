package com.tuantung.oufood.Class;

import com.tuantung.oufood.common.AddressType;

public class AnAddress {
    private String address;
    private boolean isDefault;
    private String name;
    private String phone;
    private AddressType typeAddress;
    private Ward ward;

    // load from firebase
    public AnAddress(String address, boolean isDefault, String name, String phone, AddressType typeAddress, Ward ward) {
        this.name = name;
        this.phone = phone;
        this.ward = ward;
        this.address = address;
        this.typeAddress = typeAddress;
        this.isDefault = isDefault;
    }

    public AnAddress() {
    }

    @Override
    public String toString() {
        return String.format("%s, %s", address, ward.getPath());
    }


    //getter+setter//////////////////////////////////////////////////////////////

//    public String getIdAddress() {
//        return idAddress;
//    }
//
//    public void setIdAddress(String idAddress) {
//        this.idAddress = idAddress;
//    }

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

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressType getTypeAddress() {
        return typeAddress;
    }

    public void setTypeAddress(AddressType typeAddress) {
        this.typeAddress = typeAddress;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

}

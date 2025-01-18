package com.nttung.oufood.Class;

import java.util.List;

public class Request {
    private String idRequest;
    private String idCurrentUser;
    private String total;
    private List<Order> Orders;
    private AnAddress anAddress;
    private String status; // -1: canceled  0: ordered  1:completed

    public Request() {
    }

    public Request(String idRequest, String idCurrentUser, String total, List<Order> orders, AnAddress anAddress, String status) {
        this.idRequest = idRequest;
        this.idCurrentUser = idCurrentUser;
        this.total = total;
        Orders = orders;
        this.anAddress = anAddress;
        this.status = status;
    }

    ////////////////////////////////
//    getter + setter
    public String getIdCurrentUser() {
        return idCurrentUser;
    }

    public AnAddress getAnAddress() {
        return anAddress;
    }

    public void setAnAddress(AnAddress anAddress) {
        this.anAddress = anAddress;
    }

    public void setIdCurrentUser(String idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrders() {
        return Orders;
    }

    public void setOrders(List<Order> orders) {
        Orders = orders;
    }

    //    getter + setter
///////////////////////////////////////////

}

package com.nttung.oufood.Class;

import java.util.List;

public class Request {
    private String idRequest;
    private String idCURRENTUSER;
    private String total;
    private List<Order> Orders;
    private AnAddress anAddress;
    private String status; // -1: canceled  0: ordered  1:completed

    public Request() {
    }

    public Request(String idRequest, String idCURRENTUSER, String total, List<Order> orders, AnAddress anAddress, String status) {
        this.idRequest = idRequest;
        this.idCURRENTUSER = idCURRENTUSER;
        this.total = total;
        Orders = orders;
        this.anAddress = anAddress;
        this.status = status;
    }

    ////////////////////////////////
//    getter + setter
    public String getIdCURRENTUSER() {
        return idCURRENTUSER;
    }

    public AnAddress getAnAddress() {
        return anAddress;
    }

    public void setAnAddress(AnAddress anAddress) {
        this.anAddress = anAddress;
    }

    public void setIdCURRENTUSER(String idCURRENTUSER) {
        this.idCURRENTUSER = idCURRENTUSER;
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

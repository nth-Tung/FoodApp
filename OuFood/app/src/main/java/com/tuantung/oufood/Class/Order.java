package com.tuantung.oufood.Class;

public class Order {
    private String ProductId;
    private String ProductName;
    private String Price;
    private String Quantity;
    private String Discount;
    private String isBuy;
    private boolean isRate = false;
    private int countStars = 5;

    public Order(int countStars, String discount, String isBuy, boolean isRate, String price, String productId, String productName, String quantity) {
        this.countStars = countStars;
        this.isRate = isRate;
        this.ProductId = productId;
        this.ProductName = productName;
        this.Price = price;
        this.isBuy = isBuy;
        this.Quantity = quantity;
        this.Discount = discount;
    }

    public Order() {
    }

    public Order(Food food, int quantity) {
        ProductId = food.getId();
        ProductName = food.getName();
        Price = food.getPrice();
        Quantity = String.valueOf(quantity);
        Discount = food.getDiscount();
    }

    public Order(String productId, String productName, String price, String quantity, String discount, String isBuy) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Quantity = quantity;
        Discount = discount;
        this.isBuy = isBuy;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ProductId='" + ProductId + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", Price='" + Price + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Discount='" + Discount + '\'' +
                ", isBuy='" + isBuy + '\'' +
                ", isRate=" + isRate +
                ", countStars=" + countStars +
                '}';
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String buy) {
        isBuy = buy;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public int compareTo(Order order) {
        return this.ProductId.compareTo(order.getProductId());
    }

    public boolean getIsRate() {
        return isRate;
    }

    public void setIsRate(boolean rate) {
        isRate = rate;
    }

    public int getCountStars() {
        return countStars;
    }

    public void setCountStars(int countStars) {
        this.countStars = countStars;
    }

}

package com.tuantung.oufood.Class;

import java.io.Serializable;

public class Food{
    private String id;
    private String categoryId;
    private String name;
    private String image;
    private String description;
    private String discount;
    private String price;
    private String url;

    private int countRating;
    private int countStars;


    public Food() {
    }

    public Food(String description, String discount, String image, String categoryId, String name, String price, String URL) {
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.url = URL;
    }

    public Food(String description, String discount, String image, String categoryId, String name, String price, String URL, int countRating, int countStars) {
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.url = URL;
        this.countStars = countStars;
        this.countRating = countRating;
    }

    public Food(String categoryId, int countRating, int countStars, String description, String discount, String id, String image, String name, String price, String URL) {
        this.id = id;
        this.description = description;
        this.discount = discount;
        this.image = image;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.url = URL;
        this.countStars = countStars;
        this.countRating = countRating;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String URL) {
        this.url = URL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCountStars() {
        return countStars;
    }

    public void setCountStars(int countStars) {
        this.countStars = countStars;
    }

    public int getCountRating() {
        return countRating;
    }

    public void setCountRating(int countRating) {
        this.countRating = countRating;
    }

    @Override
    public String toString() {
        return "Food{" + "id='" + id + '\'' + ", Name='" + name + '\'' + ", CategoryId='" + categoryId + '\'' + '}';
    }
}

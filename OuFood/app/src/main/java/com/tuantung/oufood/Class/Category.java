package com.tuantung.oufood.Class;

public class Category {
    private String Name;
    private String URL;
    private String id = "";


    public Category() {
    }

    public Category(String name, String uRL) {
        Name = name;
        URL = uRL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}

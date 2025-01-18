package com.nttung.oufood.Class;

public class District {
    private String code;
    private String name;
    private String parent_code;

    public District(String code, String name, String parent_code) {
        this.code = code;
        this.name = name;
        this.parent_code = parent_code;
    }

    public District() {
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
}

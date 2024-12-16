package com.tuantung.oufood.Class;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Ward implements Parcelable{
    private String code;
    private String name;
    private String parent_code;
    private String path;

    public Ward() {
    }

    //load from firebase
    public Ward(String code, String name, String parent_code, String path) {
        this.code = code;
        this.name = name;
        this.parent_code = parent_code;
        this.path = path;
    }

    protected Ward(Parcel in) {
        code = in.readString();
        name = in.readString();
        parent_code = in.readString();
        path = in.readString();
    }

    public static final Parcelable.Creator<Ward> CREATOR = new Parcelable.Creator<Ward>() {
        @Override
        public Ward createFromParcel(Parcel in) {
            return new Ward(in);
        }

        @Override
        public Ward[] newArray(int size) {
            return new Ward[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ward ward = (Ward) o;
        return Objects.equals(code, ward.code) && Objects.equals(name, ward.name) && Objects.equals(parent_code, ward.parent_code) && Objects.equals(path, ward.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, parent_code, path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(parent_code);
        dest.writeString(path);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

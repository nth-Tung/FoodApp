package com.tuantung.oufood.common;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.tuantung.oufood.Class.City;
import com.tuantung.oufood.Class.District;
import com.tuantung.oufood.Class.User;
import com.tuantung.oufood.Class.Ward;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Common {
    public static User CURRENTUSER;

    public static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final String REF_CATEGORIES = "Category";
    public static final String REF_FOODS = "Foods";
    public static final String REF_USERS = "User";
    public static final String REF_REQUESTS = "Requests";

    public static final String USERNAME_KEY = "USER_NAME";
    public static final String PASSWORD_KEY = "PASSWORD";

    public static final int TOP_BEST_SELLER = 6;

    public static String CURRENCY = "$";

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_CART = "1";

    public static final int DELAY_TIME = 10000;

    public static List<City> cities(Context context, int resourceId) {
        List<City> list = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            Iterator<String> keys = jsonObject.keys();

            String key, name;
            JSONObject obj;
            while (keys.hasNext()) {
                key = keys.next();
                obj = jsonObject.getJSONObject(key);
                name = obj.getString("name");
                list.add(new City(key, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<District> districts(Context context, int resourceId, String s) {
        List<District> list = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            Iterator<String> keys = jsonObject.keys();
            String key, name;
            JSONObject obj;
            while (keys.hasNext()) {

                key = keys.next();

                obj = jsonObject.getJSONObject(key);
                if (obj.getString("parent_code").equals(s)) {
                    name = obj.getString("name");
                    list.add(new District(key, name, s));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Ward> wards(Context context, int resourceId, String s) {
        List<Ward> list = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            Iterator<String> keys = jsonObject.keys();
            String key, name;
            JSONObject obj;
            while (keys.hasNext()) {
                key = keys.next();
                obj = jsonObject.getJSONObject(key);
                if (obj.getString("parent_code").equals(s)) {
                    name = obj.getString("name");
                    String path = obj.getString("path");
                    list.add(new Ward(key, name, s, path));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

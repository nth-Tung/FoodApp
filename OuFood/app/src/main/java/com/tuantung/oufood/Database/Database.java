package com.tuantung.oufood.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.tuantung.oufood.Activity.FoodDetailActivity;
import com.tuantung.oufood.Class.Order;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "OUFoodDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName", "ProductId", "Price", "Quantity", "Discount", "isBuy"};
        String sqlTable = "OrderDetail";

        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlSelect, null, null, null, null, null);

        List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int productIdIndex = cursor.getColumnIndex("ProductId");
                int productNameIndex = cursor.getColumnIndex("ProductName");
                int priceIndex = cursor.getColumnIndex("Price");
                int quantityIndex = cursor.getColumnIndex("Quantity");
                int discountIndex = cursor.getColumnIndex("Discount");
                int isBuyIndex = cursor.getColumnIndex("isBuy");

                // Kiểm tra nếu các cột đều tồn tại
                if (productIdIndex != -1 && priceIndex != -1 && productNameIndex != -1 && quantityIndex != -1 && discountIndex != -1 && isBuyIndex != -1) {
                    result.add(new Order(cursor.getString(productIdIndex), cursor.getString(productNameIndex), cursor.getString(priceIndex), cursor.getString(quantityIndex), cursor.getString(discountIndex), cursor.getString(isBuyIndex)));
                } else {

                }
            } while (cursor.moveToNext());
        }

        return result;
    }

    public void add_list_to_cart(List<Order> list) {
        for (Order order : list) {
            addToCart(order);
        }
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT Quantity FROM OrderDetail WHERE ProductId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{order.getProductId()});

        if (cursor.moveToFirst()) {
            // Sản phẩm đã tồn tại, tăng số lượng
            int currentQuantity = cursor.getInt(0); // Lấy số lượng hiện tại
            int newQuantity = currentQuantity + Integer.parseInt(order.getQuantity());

            ContentValues values = new ContentValues();
            values.put("Quantity", String.valueOf(newQuantity));
            db.update("OrderDetail", values, "ProductId = ?", new String[]{order.getProductId()});
        } else {
            // Sản phẩm chưa tồn tại, thêm mới
            ContentValues values = new ContentValues();
            values.put("ProductId", order.getProductId());
            values.put("ProductName", order.getProductName());
            values.put("Price", order.getPrice());
            values.put("Quantity", order.getQuantity());
            values.put("Discount", order.getDiscount());
            values.put("isBuy", order.getIsBuy());
            db.insert("OrderDetail", null, values);
        }
        cursor.close(); // Đóng Cursor sau khi sử dụng
        db.close();
    }

    public void updateQuantity(String productId, String newQuantity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Quantity", newQuantity);

        db.update("OrderDetail", values, "ProductId = ?", new String[]{productId});

        db.close();
    }

    public void updateIsBuy(String productId, String buy) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isBuy", buy);

        db.update("OrderDetail", values, "ProductId = ?", new String[]{productId});

        db.close();
    }


    public void removeItems(Order order) {
        SQLiteDatabase db = getWritableDatabase(); // Lấy cơ sở dữ liệu ở chế độ ghi
        String whereClause = "ProductId = ?";
        String[] whereArgs = {order.getProductId()};

        db.delete("OrderDetail", whereClause, whereArgs);
        db.close();
    }

    public void cleanCart() {
        deleteTable("OrderDetail");
    }

    ////////////////////////////////

    public void deleteTable(String table) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM %s", table);
        sqLiteDatabase.execSQL(query);
    }
}

package com.exmample.android.inventoryapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "inventory.db";
    private static final int DB_VERSION = 1;

    public ProductDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProductDataBaseObject.CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertItem(ProductItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(item);
        return db.insert(ProductDataBaseObject.TABLE_NAME, null, values);
    }

    private ContentValues getContentValues(ProductItem item) {
        ContentValues values = new ContentValues();
        values.put(ProductDataBaseObject.PRODUCT_NAME_FIELD, item.getName());
        values.put(ProductDataBaseObject.PRODUCT_PRICE_FIELD, item.getPrice());
        values.put(ProductDataBaseObject.PRODUCT_QUANTITY_FIELD, item.getQuantity());
        values.put(ProductDataBaseObject.PRODUCT_SUPPLIER_NAME, item.getSupplierName());
        values.put(ProductDataBaseObject.PRODUCT_SUPPLIER_PHONE_NUMBER, item.getSupplierPhone());
        values.put(ProductDataBaseObject.PRODUCT_SUPPLIER_EMAIL, item.getSupplierEmailId());
        values.put(ProductDataBaseObject.PRODUCT_ICON_FIELD, item.getIcon());
        return values;
    }

    public Cursor readAllProducts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                ProductDataBaseObject.TABLE_NAME,
                ProductDataBaseObject.projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor readProduct(int productId) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = ProductDataBaseObject.PRODUCT_ID_FIELD + "=?";
        String[] selectionArgs = new String[]{String.valueOf(productId)};
        Cursor cursor = db.query(
                ProductDataBaseObject.TABLE_NAME,
                ProductDataBaseObject.projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    public int updateProduct(int productId, ProductItem item) {
        ContentValues values = getContentValues(item);
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ProductDataBaseObject.PRODUCT_ID_FIELD + "=?";
        String[] whereArgs = new String[]{String.valueOf(productId)};
        return db.update(ProductDataBaseObject.TABLE_NAME, values, whereClause, whereArgs);
    }

    public int deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ProductDataBaseObject.PRODUCT_ID_FIELD + "=?";
        String[] whereArgs = new String[]{String.valueOf(productId)};
        return db.delete(ProductDataBaseObject.TABLE_NAME, whereClause, whereArgs);
    }

    public void deleteAllProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductDataBaseObject.TABLE_NAME, null, null);
    }

    public void sellOneItem(int productId, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductDataBaseObject.PRODUCT_QUANTITY_FIELD, quantity);
        String whereClause = ProductDataBaseObject.PRODUCT_ID_FIELD + "=?";
        String[] whereArgs = new String[]{String.valueOf(productId)};
        db.update(ProductDataBaseObject.TABLE_NAME,
                values, whereClause, whereArgs);
    }
}

package com.exmample.android.inventoryapp;


import android.provider.BaseColumns;

public class ProductDataBaseObject {

    public static final String PRODUCT_ID_FIELD = "_Id";
    public static final String TABLE_NAME = "product";
    public static final String PRODUCT_NAME_FIELD = "name";
    public static final String PRODUCT_PRICE_FIELD = "price";
    public static final String PRODUCT_QUANTITY_FIELD = "quantity";
    public static final String PRODUCT_SUPPLIER_NAME = "supplierName";
    public static final String PRODUCT_SUPPLIER_PHONE_NUMBER = "supplierPhoneNumber";
    public static final String PRODUCT_SUPPLIER_EMAIL = "supplierEmail";
    public static final String PRODUCT_ICON_FIELD = "icon";

    public static String[] projection = {
            PRODUCT_ID_FIELD, PRODUCT_NAME_FIELD, PRODUCT_PRICE_FIELD, PRODUCT_QUANTITY_FIELD, PRODUCT_SUPPLIER_NAME,
            PRODUCT_SUPPLIER_PHONE_NUMBER, PRODUCT_SUPPLIER_EMAIL, PRODUCT_ICON_FIELD
    };

    public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE " +
            TABLE_NAME + "(" +
            PRODUCT_ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PRODUCT_NAME_FIELD + " TEXT NOT NULL," +
            PRODUCT_PRICE_FIELD + " INTEGER NOT NULL DEFAULT 0, " +
            PRODUCT_QUANTITY_FIELD + " INTEGER NOT NULL DEFAULT 0," +
            PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL," +
            PRODUCT_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL DEFAULT 0," +
            PRODUCT_SUPPLIER_EMAIL + " TEXT NOT NULL," +
            PRODUCT_ICON_FIELD + " TEXT NOT NULL" + ");";



}

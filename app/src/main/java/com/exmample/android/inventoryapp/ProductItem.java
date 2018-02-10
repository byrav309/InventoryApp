package com.exmample.android.inventoryapp;


import android.database.Cursor;

public class ProductItem {

    private int productID;
    private String name;
    private int price;
    private int quantity;
    private String supplierName;
    private int supplierPhone;
    private String supplierEmailId;
    private String icon;

    public ProductItem() {

    }

    public ProductItem(String name, int price, int quantity, String supplierName, int supplierPhone, String supplierEmailId, String icon) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
        this.supplierEmailId = supplierEmailId;
        this.icon = icon;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getSupplierPhone() {
        return supplierPhone;
    }

    public String getSupplierEmailId() {
        return supplierEmailId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getIcon() {
        return icon;
    }

    public void initializeFromCursor(Cursor cursor) {
        productID = cursor.getInt(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_ID_FIELD));
        name = cursor.getString(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_NAME_FIELD));
        price = cursor.getInt(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_PRICE_FIELD));
        quantity = cursor.getInt(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_QUANTITY_FIELD));
        supplierName = cursor.getString(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_SUPPLIER_NAME));
        supplierPhone = cursor.getInt(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_SUPPLIER_PHONE_NUMBER));
        supplierEmailId = cursor.getString(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_SUPPLIER_EMAIL));
        icon = cursor.getString(cursor.getColumnIndex(ProductDataBaseObject.PRODUCT_ICON_FIELD));
    }

}

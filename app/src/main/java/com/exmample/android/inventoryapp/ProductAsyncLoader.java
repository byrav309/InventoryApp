package com.exmample.android.inventoryapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class ProductAsyncLoader extends AsyncTaskLoader<List<ProductItem>> {

    private ProductDBHelper dbHelper;

    public ProductAsyncLoader(Context context) {
        super(context);
        dbHelper = new ProductDBHelper(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ProductItem> loadInBackground() {
        Cursor cursor = dbHelper.readAllProducts();
        if (cursor != null && cursor.getCount() > 0) {
            return readProductData(cursor);
        }
        return new ArrayList<>();
    }

    private List<ProductItem> readProductData(Cursor cursor) {
        List<ProductItem> productItems = new ArrayList<>();
        ProductItem productItem;
        cursor.moveToFirst();
        do {
            productItem = new ProductItem();
            productItem.initializeFromCursor(cursor);
            productItems.add(productItem);
        } while (cursor.moveToNext());
        cursor.close();
        return productItems;
    }
}

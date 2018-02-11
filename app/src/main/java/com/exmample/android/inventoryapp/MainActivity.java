package com.exmample.android.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ProductItem>>, OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProductDBHelper dbHelper;
    private TextView noProductTextView;
    private final int LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new ProductDBHelper(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        noProductTextView = (TextView) findViewById(R.id.text_no_product_found);
        adapter = new ProductAdapter(new ArrayList<ProductItem>(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new_data:
                addNewData();
                break;
            case R.id.action_delete_all:
                deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewData() {
        Intent intent = new Intent(this, AddNewDataActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<List<ProductItem>> onCreateLoader(int id, Bundle args) {
        return new ProductAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<ProductItem>> loader, List<ProductItem> data) {
        if (data.size() <= 0) {
            noProductTextView.setVisibility(View.VISIBLE);
        } else {
            noProductTextView.setVisibility(View.GONE);
        }
        adapter.setItems(data);
    }

    @Override
    public void onLoaderReset(Loader<List<ProductItem>> loader) {

    }

    private void deleteAll() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_all_caution)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteAllProducts();
                        getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                        dialog.dismiss();
                    }
                }).
                setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();

    }

    @Override
    public void onItemClick(ProductItem item) {
        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
        intent.putExtra(InventoryConstants.PRODUCT_ID, item.getProductID());
        startActivity(intent);
    }

    @Override
    public void onSaleButtonClicked(ProductItem item) {
        int remainingQuantity;
        if (item.getQuantity() > 0) {
            remainingQuantity = item.getQuantity() - 1;
            dbHelper.sellOneItem(item.getProductID(), remainingQuantity);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            return;
        }
    }
}

package com.exmample.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ProductDBHelper dbHelper;
    private ProductItem productItem;
    private EditText productNameInput;
    private EditText productPriceInput;
    private EditText productQuantityInput;
    private ImageButton decreaseQuantityButton;
    private ImageButton increaseQuantityButton;
    private EditText supplierNameInput;
    private EditText supplierPhoneInput;
    private EditText supplierEmailIdInput;
    private Button selectImageButton;
    private ImageView productImage;
    private Button updateProductButton;
    private final int PICK_IMAGE_REQUEST = 200;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ProductDBHelper(getApplicationContext());
        setContentView(R.layout.view_and_update_product);
        productItem = new ProductItem();
        initialize();
    }

    private void initialize(){
        Cursor cursor = dbHelper.readProduct(getIntent().getIntExtra(InventoryConstants.PRODUCT_ID, 0));
        if(cursor != null && cursor.getCount() >= 0){
            cursor.moveToFirst();
            productItem.initializeFromCursor(cursor);
            cursor.close();
        }


        productNameInput = (EditText) findViewById(R.id.product_name_edit);
        productNameInput.setText(productItem.getName());
        productPriceInput = (EditText) findViewById(R.id.product_price_edit);
        productPriceInput.setText(String.valueOf(productItem.getPrice()));
        productQuantityInput = (EditText) findViewById(R.id.quantity_edit);
        productQuantityInput.setText(String.valueOf(productItem.getQuantity()));
        decreaseQuantityButton = (ImageButton) findViewById(R.id.decrease_quantity);
        decreaseQuantityButton.setOnClickListener(this);
        increaseQuantityButton = (ImageButton) findViewById(R.id.increase_quantity);
        increaseQuantityButton.setOnClickListener(this);
        supplierNameInput = (EditText) findViewById(R.id.supplier_name_edit);
        supplierNameInput.setText(productItem.getSupplierName());
        supplierPhoneInput = (EditText) findViewById(R.id.supplier_phone_edit);
        supplierPhoneInput.setText(String.valueOf(productItem.getSupplierPhone()));
        supplierEmailIdInput = (EditText) findViewById(R.id.supplier_email_edit);
        supplierEmailIdInput.setText(productItem.getSupplierEmailId());
        selectImageButton = (Button) findViewById(R.id.select_image);
        selectImageButton.setOnClickListener(this);
        imageUri = Uri.parse(productItem.getIcon());
        productImage = (ImageView) findViewById(R.id.product_image);
        if(imageUri != null){
            productImage.setVisibility(View.VISIBLE);
            productImage.setImageURI(imageUri);
            productImage.invalidate();
        }
        updateProductButton = (Button) findViewById(R.id.update_product);
        updateProductButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_image:
                selectImageForProduct();
                break;
            case R.id.decrease_quantity:
                decreaseQuantity();
                break;
            case R.id.increase_quantity:
                increaseQuantity();
                break;
            case R.id.update_product:
               updateProduct();
                break;
        }
    }

    private void showMessage(int resourceId) {
        Toast.makeText(getApplicationContext(), resourceId, Toast.LENGTH_LONG).show();
    }

    private boolean updateProduct(){
        {
            boolean status = true;
            if (!checkIfValueSet(productNameInput, R.string.product_name)) {
                status = false;
            }
            if (!checkIfValueSet(productPriceInput, R.string.product_price)) {
                status = false;
            }
            if (!checkIfValueSet(productQuantityInput, R.string.quantity)) {
                status = false;
            }
            if (!checkIfValueSet(supplierNameInput, R.string.supplier_name_title)) {
                status = false;
            }
            if (!checkIfValueSet(supplierPhoneInput, R.string.supplier_phone_title)) {
                status = false;
            }
            if (!checkIfValueSet(supplierEmailIdInput, R.string.supplier_email_title)) {
                status = false;
            }
            if (imageUri == null) {
                status = false;
                selectImageButton.setError(getString(R.string.missing) + " : " + getString(R.string.image_title));
            }
            if (!status) {
                return false;
            }
            if (dbHelper.updateProduct(productItem.getProductID(), productItem) != 0) {
                showMessage(R.string.update_successful);
            } else {
                showMessage(R.string.update_failed);
                status = false;
            }
            return status;
        }
    }

    private boolean checkIfValueSet(EditText text, int resourceId) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError(getString(R.string.missing) + " : " + getString(resourceId));
            return false;
        }
        return true;
    }

    private void selectImageForProduct() {
        Intent intent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image_for_product)), PICK_IMAGE_REQUEST);
    }

    private void decreaseQuantity() {
        String previousValueString = productQuantityInput.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            productQuantityInput.setText(String.valueOf(previousValue - 1));
        }
    }

    private void increaseQuantity() {
        String previousValueString = productQuantityInput.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        productQuantityInput.setText(String.valueOf(previousValue + 1));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                productImage.setVisibility(View.VISIBLE);
                productImage.setImageURI(imageUri);
                productImage.invalidate();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_product:
                deleteProduct();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteProduct() {
        if(dbHelper.deleteProduct(productItem.getProductID()) != 0){
            showMessage(R.string.product_deleted_successfully);
            finish();
        } else {
            showMessage(R.string.product_deletion_failed);
        }
    }

    private void clearAllField(){
        productNameInput.setText("");
        productPriceInput.setText("");
        productQuantityInput.setText("");
        supplierNameInput.setText("");
        supplierPhoneInput.setText("");
        supplierEmailIdInput.setText("");
        productImage.setVisibility(View.GONE);
    }

}

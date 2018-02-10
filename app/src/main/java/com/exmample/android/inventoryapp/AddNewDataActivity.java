package com.exmample.android.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class AddNewDataActivity extends AppCompatActivity implements View.OnClickListener {

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
    private Button saveProductButton;
    private Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 200;
    private ProductDBHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_data);
        dbHelper = new ProductDBHelper(getApplicationContext());
        initialize();
    }

    private void initialize() {
        productNameInput = (EditText) findViewById(R.id.product_name_edit);
        productPriceInput = (EditText) findViewById(R.id.product_price_edit);
        productQuantityInput = (EditText) findViewById(R.id.quantity_edit);
        decreaseQuantityButton = (ImageButton) findViewById(R.id.decrease_quantity);
        decreaseQuantityButton.setOnClickListener(this);
        increaseQuantityButton = (ImageButton) findViewById(R.id.increase_quantity);
        increaseQuantityButton.setOnClickListener(this);
        supplierNameInput = (EditText) findViewById(R.id.supplier_name_edit);
        supplierPhoneInput = (EditText) findViewById(R.id.supplier_phone_edit);
        supplierEmailIdInput = (EditText) findViewById(R.id.supplier_email_edit);
        selectImageButton = (Button) findViewById(R.id.select_image);
        selectImageButton.setOnClickListener(this);
        productImage = (ImageView) findViewById(R.id.product_image);
        saveProductButton = (Button) findViewById(R.id.save_product);
        saveProductButton.setOnClickListener(this);
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
            case R.id.save_product:
                if (saveProduct()) {
                    showDialogToAddAnotherProduct();
                }
                break;
        }
    }

    private void showDialogToAddAnotherProduct() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.add_another_product)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearAllField();
                        dialog.dismiss();
                    }
                }).
                setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
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

    private boolean saveProduct() {
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

        ProductItem productItem = new ProductItem(productNameInput.getText().toString().trim(), Integer.parseInt(productPriceInput.getText().toString().trim()),
                Integer.parseInt(productQuantityInput.getText().toString().trim()), supplierNameInput.getText().toString().trim(),
                Integer.parseInt(supplierPhoneInput.getText().toString().trim()), supplierEmailIdInput.getText().toString().trim(), imageUri.toString());
        if (dbHelper.insertItem(productItem) != 0) {
            showMessage(R.string.product_addition_successful);
        } else {
            showMessage(R.string.product_addition_failure);
            status = false;
        }
        return status;
    }

    private void showMessage(int resourceId) {
        Toast.makeText(getApplicationContext(), resourceId, Toast.LENGTH_LONG).show();
    }

    private boolean checkIfValueSet(EditText text, int resourceId) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError(getString(R.string.missing) + " : " + getString(resourceId));
            return false;
        }
        return true;
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
}

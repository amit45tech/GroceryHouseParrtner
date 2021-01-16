package com.sarkstechsolution.ghstorepartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton settingButton;
    Button orderButton, pOutOfStockBtn, productStatusBtn, addProductBtn,manageProductBtn;
    CardView todaySaleCardBtn, previousSaleCardBtn;
    String IdStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingButton = findViewById(R.id.settingBtn);
        orderButton = findViewById(R.id.ordersBtn);
        todaySaleCardBtn = findViewById(R.id.todaySaleCardView);
        previousSaleCardBtn = findViewById(R.id.previousSaleCardView);
        pOutOfStockBtn = findViewById(R.id.POOS);
        productStatusBtn = findViewById(R.id.AvailStatus);
        addProductBtn = findViewById(R.id.addProduct);
        manageProductBtn = findViewById(R.id.manageProduct);

        IdStore = getIntent().getStringExtra("STOREID");


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OrdersActivity.class);
                startActivity(intent);
            }
        });

        todaySaleCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodaySalesActivity.class);
                startActivity(intent);
            }
        });

        previousSaleCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreviousSaleActivity.class);
                startActivity(intent);
            }
        });

        pOutOfStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OutOfStockActivity.class);
                intent.putExtra("STOREId", IdStore);
                startActivity(intent);
            }
        });

        productStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductAvailabilityStatusActivity.class);
                intent.putExtra("STOREId", IdStore);
                startActivity(intent);
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                intent.putExtra("STOREId", IdStore);
                startActivity(intent);
            }
        });

        manageProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllProductsActivity.class);
                intent.putExtra("STOREId", IdStore);
                startActivity(intent);
            }
        });




    }
}
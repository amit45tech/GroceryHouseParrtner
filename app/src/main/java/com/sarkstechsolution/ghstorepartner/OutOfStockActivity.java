package com.sarkstechsolution.ghstorepartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarkstechsolution.ghstorepartner.Adapter.OutOfStockAdapter;
import com.sarkstechsolution.ghstorepartner.Adapter.ProductsAdapter;
import com.sarkstechsolution.ghstorepartner.Model.Products;

public class OutOfStockActivity extends AppCompatActivity {

    ImageButton imageButton;
    OutOfStockAdapter adapter;
    DatabaseReference productRef;
    RecyclerView recyclerView;
    String STOREID;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_of_stock);

        imageButton = findViewById(R.id.imageButton);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productRef = FirebaseDatabase.getInstance().getReference();

        STOREID = getIntent().getStringExtra("STOREId");


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ShowAllProducts();
    }

    private void ShowAllProducts() {

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef.child("Products").child(STOREID).orderByChild("availability").equalTo("Out Of Stock"), Products.class)
                .build();

        adapter = new OutOfStockAdapter(options,this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
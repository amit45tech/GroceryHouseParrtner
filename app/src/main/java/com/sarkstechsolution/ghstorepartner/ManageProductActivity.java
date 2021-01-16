package com.sarkstechsolution.ghstorepartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ManageProductActivity extends AppCompatActivity {

    ImageButton imageButton;
    String pDescription, pName, pMrp, pSp, pUnit, pAvailabiliy, pCategory, downloadImageUrl, saveCurrentDate, saveCurrentTime, productRandomKey, StoreID, ProductID;

    ImageButton productImgBtn;
    EditText ProName, ProMrp, ProSp, ProUnit, ProDescription, ProCategory;
    Button changesBtn;
    Uri uri;
    ProgressDialog loadingBar;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    int selectedRadioButtonId;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    AnstronCoreHelper coreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        imageButton = findViewById(R.id.imageButton);



        productImgBtn = findViewById(R.id.productImg);
        ProName = findViewById(R.id.productName);
        ProMrp = findViewById(R.id.productMRP);
        ProSp = findViewById(R.id.productSP);
        ProUnit = findViewById(R.id.productUnit);
        ProDescription = findViewById(R.id.productDescription);
        changesBtn = findViewById(R.id.applyChangesBtn);

        ProCategory = findViewById(R.id.productCategory);
        radioGroup = findViewById(R.id.radiogroup);
        loadingBar = new ProgressDialog(this);

        coreHelper = new AnstronCoreHelper(this);

        Bundle bundle = getIntent().getExtras();

        StoreID = bundle.getString("storeId");
        ProductID = bundle.getString("productId");
        downloadImageUrl = bundle.getString("productImageUrl");




        ProductImagesRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadImageUrl);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });

//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DeleteProduct();
//            }
//        });


        DisplaySpecificProductInfo();


    }

    private void DeleteProduct()
    {
        loadingBar.setTitle("Deleting Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        ProductImagesRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ManageProductActivity.this, "Image is deleted successfully!", Toast.LENGTH_SHORT).show();

            }
        });
        ProductsRef.child(StoreID).child(ProductID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                Intent intent = new Intent(ManageProductActivity.this, AllProductsActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(ManageProductActivity.this, "Product is deleted successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ApplyChanges()
    {
        String Pname = ProName.getText().toString();
        String Pmrp = ProName.getText().toString();
        String Psp = ProName.getText().toString();
        String Pcategory = ProName.getText().toString();
        String Punit = ProName.getText().toString();
        String Pdescription = ProName.getText().toString();

        if (Pname.equals(""))
        {
            Toast.makeText(this, "Write down Product Name", Toast.LENGTH_SHORT).show();
        }
        else  if (Pmrp.equals(""))
        {
            Toast.makeText(this, "Write down Product MRP", Toast.LENGTH_SHORT).show();
        }
        else  if (Pdescription.equals(""))
        {
            Toast.makeText(this, "Write down Product Description", Toast.LENGTH_SHORT).show();
        }
        else  if (Psp.equals(""))
        {
            Toast.makeText(this, "Write down Product Selling Price", Toast.LENGTH_SHORT).show();
        }
        else  if (Pcategory.equals(""))
        {
            Toast.makeText(this, "Write down Product Category", Toast.LENGTH_SHORT).show();
        }
        else  if (Punit.equals(""))
        {
            Toast.makeText(this, "Write down Product Unit", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("description", Pdescription);
            productMap.put("category", Pcategory);
            productMap.put("mrp", Pmrp);
            productMap.put("sp", Psp);
            productMap.put("name", Pname);
            productMap.put("unit", Punit);

            ProductsRef.child(StoreID).child(ProductID).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(ManageProductActivity.this, "Changes applied Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ManageProductActivity.this, AllProductsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

    }


    private void DisplaySpecificProductInfo()
    {
        ProductsRef.child(StoreID).child(ProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                   String Name = snapshot.child("name").getValue().toString();
                   String mrp = snapshot.child("mrp").getValue().toString();
                   String sp = snapshot.child("sp").getValue().toString();
                   String unit = snapshot.child("unit").getValue().toString();
                   String category = snapshot.child("category").getValue().toString();
                   String descrp = snapshot.child("description").getValue().toString();
                   String image = snapshot.child("image").getValue().toString();


                   ProName.setText(Name);
                   ProMrp.setText(mrp);
                   ProSp.setText(sp);
                   ProUnit.setText(unit);
                   ProCategory.setText(category);
                   ProDescription.setText(descrp);
                   Picasso.get().load(image).into(productImgBtn);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
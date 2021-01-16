package com.sarkstechsolution.ghstorepartner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    String pDescription, pName, pMrp, pSp, pUnit, pAvailabiliy, pCategory, downloadImageUrl, saveCurrentDate, saveCurrentTime, productRandomKey, StoreID;
    ImageButton imageButton;
    ImageButton productImgBtn;
    EditText ProName, ProMrp, ProSp, ProUnit, ProDescription, ProCategory;
    Button ProUploadBtn;
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
        setContentView(R.layout.activity_add_product);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        imageButton = findViewById(R.id.imageButton);
        productImgBtn = findViewById(R.id.productImg);
        ProName = findViewById(R.id.productName);
        ProMrp = findViewById(R.id.productMRP);
        ProSp = findViewById(R.id.productSP);
        ProUnit = findViewById(R.id.productUnit);
        ProDescription = findViewById(R.id.productDescription);
        ProUploadBtn = findViewById(R.id.applyChangesBtn);
        ProCategory = findViewById(R.id.productCategory);
        radioGroup = findViewById(R.id.radiogroup);
        loadingBar = new ProgressDialog(this);

        coreHelper = new AnstronCoreHelper(this);

        StoreID = getIntent().getStringExtra("STOREId");




        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseProductImage();
            }
        });

        ProUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---------------Stock radio Button------------------
                selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if(selectedRadioButtonId != -1) {
                selectedRadioButton = findViewById(selectedRadioButtonId);
                pAvailabiliy = selectedRadioButton.getText().toString();
            }

                //---------------Stock radio Button end -------------
                ValidateProductData();
            }
        });


    }

    private void ValidateProductData()
    {
        pName = ProName.getText().toString();
        pMrp = ProMrp.getText().toString();
        pSp = ProSp.getText().toString();
        pUnit = ProUnit.getText().toString();
        pDescription = ProDescription.getText().toString();
        pCategory = ProCategory.getText().toString();


        if (uri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pName))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pMrp))
        {
            Toast.makeText(this, "Please write product Mrp...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pSp))
        {
            Toast.makeText(this, "Please write product pSp...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pUnit))
        {
            Toast.makeText(this, "Please write product pUnit...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pDescription))
        {
            Toast.makeText(this, "Please write product pDescription...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pCategory))
        {
            Toast.makeText(this, "Please write product pCategory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pAvailabiliy))
        {
            Toast.makeText(this, "Please write product Availabiliy...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();

        }

    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait, while we are adding...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate +saveCurrentTime;

//
//        File file = new File(SiliCompressor.with(this).compress(FileUtils.getPath(this, uri), new File(this.getCacheDir(), "temp")));
//        Uri compressedImgUri = Uri.fromFile(file);

        final  StorageReference filePath = ProductImagesRef.child(productRandomKey + "__" + uri.getLastPathSegment());

        final UploadTask uploadTask = filePath.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddProductActivity.this,"Error"+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddProductActivity.this,"Product Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();


                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                          downloadImageUrl = task.getResult().toString();
                          Toast.makeText(AddProductActivity.this, "Getting Product Image url Successfully..", Toast.LENGTH_SHORT).show();

                          SaveProductInfoToDatabase();

                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", pDescription);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", pCategory);
        productMap.put("mrp", pMrp);
        productMap.put("sp", pSp);
        productMap.put("name", pName);
        productMap.put("unit", pUnit);
        productMap.put("availability", pAvailabiliy);
        productMap.put("sid", StoreID);


       ProductsRef.child(StoreID).child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful())
               {
                  loadingBar.dismiss();
                  Toast.makeText(AddProductActivity.this,"Product added Successfully...", Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(AddProductActivity.this, AddProductActivity.class);
                  startActivity(intent);
                  finish();

               }
               else {
                   loadingBar.dismiss();
                   String message = task.getException().toString();
                   Toast.makeText(AddProductActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
               }
           }
       });
    }


    private void ChooseProductImage()
    {
        CropImage.startPickImageActivity(AddProductActivity.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {
                startCrop(imageuri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                uri = result.getUri();
                productImgBtn.setImageURI(result.getUri());
                Toast.makeText(this, "Image set", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(false)
                .start(this);
    }

}
package com.sarkstechsolution.ghstorepartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarkstechsolution.ghstorepartner.Model.Stores;
import com.sarkstechsolution.ghstorepartner.Prevalent.Prevalent;

import java.util.HashMap;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;
    EditText storeId,  password;
    EditText store,  pass;
    private ProgressDialog loadingBar;
    private String parentDbName = "RegisteredStores";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.button);
        storeId = findViewById(R.id.storeId);
        password = findViewById(R.id.password);
        store = findViewById(R.id.editTextTextPersonName);
        pass = findViewById(R.id.editTextTextPersonName2);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.rememberCheckBox);
        Paper.init(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CreateAccount();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccount();

            }
        });



        String StoreIdKey = Paper.book().read(Prevalent.StoreIdKey);
        String StorePasswordKey = Paper.book().read(Prevalent.StorePasswordKey);

        if (StoreIdKey != "" && StorePasswordKey != "")
        {
            if (!TextUtils.isEmpty(StoreIdKey) && !TextUtils.isEmpty(StorePasswordKey))
            {
                AllowAccess(StoreIdKey, StorePasswordKey);

                loadingBar.setTitle("Already logged In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }

        }
        
        
    }

    private void AllowAccess(final String storeIdKey, final String storePasswordKey) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("RegisteredStores").child(storeIdKey).exists())
                {
                    Stores StoresData = dataSnapshot.child("RegisteredStores").child(storeIdKey).getValue(Stores.class);

                    if (StoresData.getStoreid().equals(storeIdKey))
                    {
                        if (StoresData.getPassword().equals(storePasswordKey))
                        {
                            Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Prevalent.currentOnlineStore = StoresData;
                            intent.putExtra("STOREID", StoresData.getStoreid());
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect!", Toast.LENGTH_SHORT).show();

                        }

                    }


                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with"+ storeIdKey+"does't exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    private void LoginAccount()
    {
        String StoreId = storeId.getText().toString();
        String Password = password.getText().toString();

        if(TextUtils.isEmpty(StoreId))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait, while we are checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(StoreId, Password);

        }
    }

    private void AllowAccessToAccount(final String storeId, final String password)
    {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.StoreIdKey, storeId);
            Paper.book().write(Prevalent.StorePasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(storeId).exists())
                {
                    Stores storesData = dataSnapshot.child(parentDbName).child(storeId).getValue(Stores.class);

                    if(storesData.getStoreid().equals(storeId))
                    {
                        if(storesData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("RegisteredStores"))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Prevalent.currentOnlineStore = storesData;
                                intent.putExtra("STOREID", storesData.getStoreid());
                                startActivity(intent);
                                finish();
                            }

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password incorrect!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Phone Number is incorrect!.", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with"+ storeId +"does't exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Check the  Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CreateAccount()
    {
        String SId = store.getText().toString();
        String password = pass.getText().toString();

        if(TextUtils.isEmpty(SId)){
            Toast.makeText(this, "Please write ID", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(SId, password);
        }

        }

    private void ValidatePhoneNumber(final String sId, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("RegisteredStores").child(sId).exists()))
                {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("storeid",sId);
                    userdataMap.put("password",password);


                    RootRef.child("RegisteredStores").child(sId).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(LoginActivity.this, "Congratulations, Your account is registered", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();



                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(LoginActivity.this, "Network Error, Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "this"+ sId +"already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
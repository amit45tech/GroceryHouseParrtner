package com.sarkstechsolution.ghstorepartner.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sarkstechsolution.ghstorepartner.AllProductsActivity;
import com.sarkstechsolution.ghstorepartner.ManageProductActivity;
import com.sarkstechsolution.ghstorepartner.Model.Products;
import com.sarkstechsolution.ghstorepartner.R;
import com.squareup.picasso.Picasso;


public class ProductsAdapter extends FirebaseRecyclerAdapter<Products, ProductsAdapter.ProductItemViewHolder> {

    private Context context;

    public ProductsAdapter(@NonNull FirebaseRecyclerOptions<Products> options, Context context) {
        super(options);
        this.context = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull ProductsAdapter.ProductItemViewHolder holder, int position, @NonNull Products model) {

        holder.Pname.setText(model.getName());
        holder.Psp.setText("\u20B9" + model.getSp());
        holder.Pstatus.setText(model.getAvailability());
        holder.Pcategory.setText(model.getCategory());
        holder.PMrp.setText("\u20B9" + model.getMrp());
        holder.PMrp.setPaintFlags(holder.PMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.Punit.setText(model.getUnit());

        Picasso.get().load(model.getImage()).into(holder.PImg);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.loadingBar.setTitle("Deleting Product");
                holder.loadingBar.setMessage("Please wait...");
                holder.loadingBar.setCanceledOnTouchOutside(false);
                holder.loadingBar.show();

                FirebaseStorage.getInstance().getReferenceFromUrl(model.getImage()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Image is deleted successfully!", Toast.LENGTH_SHORT).show();

                    }
                });
                holder.ProductsRef.child(model.getSid()).child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder.loadingBar.dismiss();

                        Toast.makeText(context, "Product is deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("productId", model.getPid());
                bundle.putString("storeId", model.getSid());
                bundle.putString("productImageUrl", model.getImage());

                Intent intent = new Intent(context, ManageProductActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_layout, parent, false);
        return new ProductsAdapter.ProductItemViewHolder(view);
    }

    class ProductItemViewHolder extends RecyclerView.ViewHolder {

        ImageView PImg;
        TextView Pname, Pcategory, PMrp, Psp, Punit, Pstatus, deleteBtn;
        private StorageReference ProductImagesRef;
        private DatabaseReference ProductsRef;
        ProgressDialog loadingBar;
        ConstraintLayout layout;

        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);


            PImg = itemView.findViewById(R.id.pImgView);
            Pname = itemView.findViewById(R.id.pNameView);
            PMrp = itemView.findViewById(R.id.pMrpView);
            Pcategory = itemView.findViewById(R.id.pCategoryView);
            Pstatus = itemView.findViewById(R.id.pAvailStatusView);
            Psp = itemView.findViewById(R.id.pSpView);
            Punit = itemView.findViewById(R.id.pUnitView);
            deleteBtn = itemView.findViewById(R.id.delete_product);
            ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

            layout = itemView.findViewById(R.id.pLayout);
            loadingBar = new ProgressDialog(context);


        }
    }
}

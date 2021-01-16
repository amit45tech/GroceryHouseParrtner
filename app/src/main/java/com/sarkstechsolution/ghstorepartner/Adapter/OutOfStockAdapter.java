package com.sarkstechsolution.ghstorepartner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarkstechsolution.ghstorepartner.Model.Products;
import com.sarkstechsolution.ghstorepartner.OutOfStockActivity;
import com.sarkstechsolution.ghstorepartner.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class OutOfStockAdapter extends FirebaseRecyclerAdapter<Products, OutOfStockAdapter.ProductViewHolder> {

    private Context context;

    public OutOfStockAdapter(@NonNull FirebaseRecyclerOptions<Products> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
        holder.Pname.setText(model.getName());
        holder.Psp.setText("\u20B9" + model.getSp());
        holder.Pstatus.setText(model.getAvailability());
        holder.Pcategory.setText(model.getCategory());
        holder.PMrp.setText("\u20B9" + model.getMrp());
        holder.PMrp.setPaintFlags(holder.PMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.Punit.setText(model.getUnit());

        Picasso.get().load(model.getImage()).into(holder.PImg);

        holder.markIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.productMap.put("availability", "In Stock");
                holder.Preference.child("Products").child(model.getSid()).child(model.getPid()).updateChildren(holder.productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Marked !", Toast.LENGTH_SHORT).show();



                        }
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_outofstrock_product_layout, parent, false);
        return new OutOfStockAdapter.ProductViewHolder(view);
    }

    public static class ProductViewHolder  extends RecyclerView.ViewHolder {

        ImageView PImg;
        TextView Pname, Pcategory, PMrp, Psp, Punit, Pstatus, markIS;
        DatabaseReference Preference;
        HashMap<String, Object> productMap;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            PImg = itemView.findViewById(R.id.pImgView);
            Pname = itemView.findViewById(R.id.pNameView);
            PMrp = itemView.findViewById(R.id.pMrpView);
            Pcategory = itemView.findViewById(R.id.pCategoryView);
            Pstatus = itemView.findViewById(R.id.pAvailStatusView);
            Psp = itemView.findViewById(R.id.pSpView);
            Punit = itemView.findViewById(R.id.pUnitView);
            markIS = itemView.findViewById(R.id.mark_is);
            Preference = FirebaseDatabase.getInstance().getReference();
            productMap = new HashMap<>();

        }
    }
}

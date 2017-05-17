package com.entersnowman.internetshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.entersnowman.internetshop.ProductActivity;
import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import static com.entersnowman.internetshop.GeneralActivity.FIREBASE;

/**
 * Created by Valentin on 02.05.2017.
 */

public class BasketAdapter  extends RecyclerView.Adapter<BasketAdapter.ProductHolder> {
    ArrayList<Product> products;
    Context context;
    DatabaseReference mDatabaseReference;
    String uid;
    @Override
    public BasketAdapter.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_basket_item, parent, false);
        BasketAdapter.ProductHolder vh = new BasketAdapter.ProductHolder(v);
        return vh;
    }

    public BasketAdapter(ArrayList<Product> products, Context context,String  uid) {
        this.uid = uid;
        this.products = products;
        this.context = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(this.uid);
    }

    @Override
    public void onBindViewHolder(BasketAdapter.ProductHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Получили экземпляр
        imageLoader.init(ImageLoaderConfiguration.createDefault(context)); // Проинициализировали конфигом по умолчанию
        imageLoader.displayImage(products.get(position).getPhoto_url(), holder.productPhoto); // Запустили асинхронный показ картинки
        holder.productName_tv.setText(products.get(position).getName());
        holder.productPrice_tv.setText(Float.toString(products.get(position).getPrice())+" UAH");
        if (products.get(position).isAvailable())
            holder.productAvailability.setText(context.getString(R.string.available));
        else
            holder.productAvailability.setText(context.getString(R.string.unavailable));
        holder.productCategory_tv.setText(products.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName_tv;
        TextView productCategory_tv;
        TextView productPrice_tv;
        ImageView productPhoto;
        ImageView removeButton;
        TextView productAvailability;
        public ProductHolder(View itemView) {
            super(itemView);
            removeButton = (ImageView) itemView.findViewById(R.id.remove_from_basket_button);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(FIREBASE,products.get(getAdapterPosition()).getCategory()+"_"+products.get(getAdapterPosition()).getId());
                    mDatabaseReference.child("basket")
                            .child(products.get(getAdapterPosition()).getCategory()+"_"+products.get(getAdapterPosition()).getId())
                            .setValue(null)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context,products.get(getAdapterPosition()).getName()+" "+context.getString(R.string.product_removed_from_basket),Toast.LENGTH_SHORT).show();
                                    products.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            });
                }
            });
            productCategory_tv = (TextView) itemView.findViewById(R.id.product_category);
            productName_tv = (TextView) itemView.findViewById(R.id.product_name);
            productPrice_tv = (TextView) itemView.findViewById(R.id.product_price);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            productAvailability = (TextView) itemView.findViewById(R.id.product_availability);
        }
    }
}

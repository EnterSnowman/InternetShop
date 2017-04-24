package com.entersnowman.internetshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.model.Product;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import static com.entersnowman.internetshop.R.id.imageView;

/**
 * Created by Valentin on 24.04.2017.
 */

public class BestProductAdapter extends RecyclerView.Adapter<BestProductAdapter.ProductHolder> {
    String category;
    ArrayList<Product> products;
    Context context;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public BestProductAdapter(String category, ArrayList<Product> products, Context context) {

        this.category = category;
        this.products = products;
        this.context = context;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_product_item, parent, false);
        ProductHolder vh = new ProductHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Получили экземпляр
        imageLoader.init(ImageLoaderConfiguration.createDefault(context)); // Проинициализировали конфигом по умолчанию
        imageLoader.displayImage(products.get(position).getPhoto_url(), holder.productPhoto); // Запустили асинхронный показ картинки
        holder.productName_tv.setText(products.get(position).getName());
        holder.productPrice_tv.setText(Float.toString(products.get(position).getPrice())+" UAH");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName_tv;
        TextView productPrice_tv;
        ImageView productPhoto;
        public ProductHolder(View itemView) {
            super(itemView);
            productName_tv = (TextView) itemView.findViewById(R.id.product_name);
            productPrice_tv = (TextView) itemView.findViewById(R.id.product_price);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
        }
    }
}

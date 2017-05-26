package com.entersnowman.internetshop.adapter;

import android.content.Context;
import android.graphics.Paint;
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

/**
 * Created by Valentin on 26.05.2017.
 */

public class DiscountAdapter  extends RecyclerView.Adapter<DiscountAdapter.ProductHolder> {
    String category;
    ArrayList<Product> products;
    Context context;
    BestProductAdapter.ListItemClickListener listItemClickListener;
    public BestProductAdapter.ListItemClickListener getListItemClickListener() {
        return listItemClickListener;
    }

    public void setListItemClickListener(BestProductAdapter.ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

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

    public  interface ListItemClickListener{
        void onItemClick(int position);
    }
    public DiscountAdapter( ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public DiscountAdapter.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_product_item_with_discounts, parent, false);
        DiscountAdapter.ProductHolder vh = new DiscountAdapter.ProductHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DiscountAdapter.ProductHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Получили экземпляр
        imageLoader.init(ImageLoaderConfiguration.createDefault(context)); // Проинициализировали конфигом по умолчанию
        imageLoader.displayImage(products.get(position).getPhoto_url(), holder.productPhoto); // Запустили асинхронный показ картинки
        holder.productName_tv.setText(products.get(position).getName());
        holder.productPrice_tv.setText(Float.toString(products.get(position).getPrice())+" UAH");
        holder.productPriceWithDiscount_tv.setText(Float.toString(products.get(position).getPrice()*(1-products.get(position).getDiscount()))+" UAH");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView productName_tv;
        TextView productPrice_tv;
        TextView productPriceWithDiscount_tv;
        ImageView productPhoto;
        public ProductHolder(View itemView) {
            super(itemView);
            productName_tv = (TextView) itemView.findViewById(R.id.product_name);
            productPriceWithDiscount_tv = (TextView) itemView.findViewById(R.id.product_price_with_discount);
            productPrice_tv = (TextView) itemView.findViewById(R.id.product_price);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            productPriceWithDiscount_tv.setPaintFlags(productPriceWithDiscount_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}

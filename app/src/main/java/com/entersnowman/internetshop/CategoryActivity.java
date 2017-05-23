package com.entersnowman.internetshop;

import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.entersnowman.internetshop.adapter.CategoryAdapter;
import com.entersnowman.internetshop.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.slideup.SlideUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.list_of_all_products) RecyclerView listOfAllProducts;
    private DatabaseReference mDatabase;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.slideView) View view;
    @BindView(R.id.sort_parameters)
    RadioGroup radioGroup;
    CategoryAdapter categoryAdapter;
    SlideUp slideUp;
    ArrayList<Product> products;
    Comparator<Product> c1,c2,c3,c4,c5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("category").substring(0, 1).toUpperCase()+getIntent().getStringExtra("category").substring(1));
        products = new ArrayList<>();
        c1 = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return (int) (o1.getPrice()-o2.getPrice());
            }
        };
        c2 = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return -(int) (o1.getPrice()-o2.getPrice());
            }
        };
        c3 = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return -(int) (o1.getRating()-o2.getRating())*100;
            }
        };
        c4 = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return (o1.getName().compareTo(o2.getName()));
            }
        };
        c5 = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return -(o1.getName().compareTo(o2.getName()));
            }
        };
        slideUp = new SlideUp.Builder(view)
                .withListeners(new SlideUp.Listener() {
            @Override
            public void onSlide(float percent) {
            }

            @Override
            public void onVisibilityChanged(int visibility) {
                if (visibility == View.GONE){
                    floatingActionButton.show();
                }
            }
        }).withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .build();

        categoryAdapter = new CategoryAdapter(getIntent().getStringExtra("category"),products,this);
        listOfAllProducts.setLayoutManager(new GridLayoutManager(this,2));
        listOfAllProducts.setAdapter(categoryAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("products").child(getIntent().getStringExtra("category"));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Product p = d.getValue(Product.class);
                    p.setId(d.getKey());
                    products.add(p);
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp.show();
                floatingActionButton.hide();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.cheap_expensive:
                        Collections.sort(products,c1);
                        slideUp.hide();
                        floatingActionButton.show();
                        categoryAdapter.notifyDataSetChanged();
                        break;
                    case R.id.expensive_cheap:
                        Collections.sort(products,c2);
                        slideUp.hide();
                        floatingActionButton.show();
                        categoryAdapter.notifyDataSetChanged();
                        break;
                    case R.id.consumer_rating:
                        Collections.sort(products,c3);
                        slideUp.hide();
                        floatingActionButton.show();
                        categoryAdapter.notifyDataSetChanged();
                        break;
                    case R.id.A_Z:
                        Collections.sort(products,c4);
                        slideUp.hide();
                        floatingActionButton.show();
                        categoryAdapter.notifyDataSetChanged();
                        break;
                    case R.id.Z_A:
                        Collections.sort(products,c5);
                        slideUp.hide();
                        floatingActionButton.show();
                        categoryAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

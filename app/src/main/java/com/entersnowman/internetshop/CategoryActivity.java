package com.entersnowman.internetshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.entersnowman.internetshop.adapter.CategoryAdapter;
import com.entersnowman.internetshop.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.list_of_all_products) RecyclerView listOfAllProducts;
    private DatabaseReference mDatabase;
    CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("category").substring(0, 1).toUpperCase()+getIntent().getStringExtra("category").substring(1));
        final ArrayList<Product> products = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getIntent().getStringExtra("category"),products,this);
        listOfAllProducts.setLayoutManager(new GridLayoutManager(this,2));
        listOfAllProducts.setAdapter(categoryAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("products").child(getIntent().getStringExtra("category"));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    products.add(d.getValue(Product.class));
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

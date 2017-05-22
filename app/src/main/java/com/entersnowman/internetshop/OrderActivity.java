package com.entersnowman.internetshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.entersnowman.internetshop.adapter.OrdersAdapter;
import com.entersnowman.internetshop.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.listOfOrders)
    RecyclerView listOfOrders;

    OrdersAdapter ordersAdapter;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ArrayList<Order> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        orders = new ArrayList<Order>();
        ordersAdapter = new OrdersAdapter(orders);
        listOfOrders.setLayoutManager(new LinearLayoutManager(this));
        listOfOrders.setAdapter(ordersAdapter);
        getSupportActionBar().setTitle(R.string.your_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("orders");
        final SimpleDateFormat sfd = new SimpleDateFormat("dd MM yyyy");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Log.d("order",d.toString());
                    Order order = new Order();
                    order.setId(d.getKey());
                    order.setStatus(d.child("status").getValue(String.class));
                    order.setCity(d.child("city").getValue(String.class));
                    order.setCountOfProducts(String.valueOf(d.child("products").getChildrenCount()));
                    order.setDateOfMaking(sfd.format(new Date(d.child("timestamp").getValue(Long.class))));
                    order.setKindOfPayment(d.child("kindOfPayment").getValue(String.class));
                    order.setWarehouse(d.child("warehouse").getValue(String.class));
                    orders.add(order);
                    ordersAdapter.notifyDataSetChanged();
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

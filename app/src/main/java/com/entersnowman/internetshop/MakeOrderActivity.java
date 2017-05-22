package com.entersnowman.internetshop;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.utils.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakeOrderActivity extends AppCompatActivity {
    @BindView(R.id.find_city)
    SearchView searchView;
    NetworkUtils networkUtils;
    @BindView(R.id.finded_city)
    TextView textView;
    @BindView(R.id.spinner_hint) TextView spinner_hint;
    @BindView(R.id.textView3) TextView payment_method;
    @BindView(R.id.warehouse_spinner)
    Spinner warehouseSpinner;
    @BindView(R.id.payment_method_radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.make_order_button) Button makeOrderButton;


    private SimpleCursorAdapter mAdapter;
    ArrayAdapter<String> adapter;
    ArrayList<String> warehouses;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        ButterKnife.bind(this);
        networkUtils = new NetworkUtils();
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        makeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (warehouseSpinner.getSelectedItemPosition()== AdapterView.INVALID_POSITION||radioGroup.getCheckedRadioButtonId()==-1)
                    Toast.makeText(MakeOrderActivity.this,R.string.select_all_options,Toast.LENGTH_SHORT).show();
                else {
                    final HashMap<String,Object> map = new HashMap<String, Object>();
                    map.put("timestamp", ServerValue.TIMESTAMP);
                    map.put("city",textView.getText());
                    map.put("warehouse",warehouseSpinner.getSelectedItem());
                    if (radioGroup.getCheckedRadioButtonId()==R.id.cash_radioButton)
                    map.put("kindOfPayment","cash");
                    else
                    map.put("kindOfPayment","card");
                    final HashMap<String,Object> products = new HashMap<String, Object>();
                    mDatabase.child("basket").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d: dataSnapshot.getChildren())
                                products.put(d.getKey(),"order");
                            String id = mDatabase.child("orders").push().getKey();
                            mDatabase.child("orders").child(id).setValue(map);
                            mDatabase.child("orders").child(id).child("products").setValue(products);
                            Toast.makeText(MakeOrderActivity.this,"Order added",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(mAdapter);
        //searchView.setQueryHint(getString(R.string.input_city));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                networkUtils.getCities(newText,mAdapter);
                return false;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                textView.setText(((Cursor)mAdapter.getItem(position)).getString(1));
                adapter = new ArrayAdapter<String>(MakeOrderActivity.this,R.layout.my_spinner_item);
                networkUtils.getWarehouses(textView.getText().toString(),adapter);
                warehouseSpinner.setAdapter(adapter);
                warehouseSpinner.setVisibility(View.VISIBLE);
                makeOrderButton.setVisibility(View.VISIBLE);
                spinner_hint.setVisibility(View.VISIBLE);
                payment_method.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                Log.d("search", ((Cursor)mAdapter.getItem(position)).getString(1));
                return true;
            }
        });

    }

}

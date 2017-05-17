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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakeOrderActivity extends AppCompatActivity {
    @BindView(R.id.find_city)
    SearchView searchView;
    NetworkUtils networkUtils;
    @BindView(R.id.finded_city)
    TextView textView;
    @BindView(R.id.warehouse_spinner)
    Spinner warehouseSpinner;
    private SimpleCursorAdapter mAdapter;
    ArrayAdapter<String> adapter;
    ArrayList<String> warehouses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        ButterKnife.bind(this);

        networkUtils = new NetworkUtils();
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

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
                Log.d("search", ((Cursor)mAdapter.getItem(position)).getString(1));
                return true;
            }
        });

    }

}

package com.entersnowman.internetshop;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakeOrderActivity extends AppCompatActivity {
    @BindView(R.id.find_city) SearchView searchView;
    @BindView(R.id.make_request)
    Button button;
    @BindView(R.id.editText)
    EditText editText;
    NetworkUtils networkUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        ButterKnife.bind(this);
        networkUtils = new NetworkUtils();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.input_city));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkUtils.getCities(editText.getText().toString());
            }
        });
    }
}

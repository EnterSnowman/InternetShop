package com.entersnowman.internetshop;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entersnowman.internetshop.adapter.BestProductAdapter;
import com.entersnowman.internetshop.adapter.CategoryAdapter;
import com.entersnowman.internetshop.adapter.FavoritesAdapter;
import com.entersnowman.internetshop.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeneralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    final static String FIREBASE = "FIREBASE";
    @BindView(R.id.content) LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("products");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.main_page));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView nameLabel = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        nameLabel.setText(mAuth.getCurrentUser().getDisplayName());
        Log.d(FIREBASE,"NAme is: "+mAuth.getCurrentUser().getDisplayName());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ArrayList<Product> products = new ArrayList<Product>();
                    for (DataSnapshot p: d.getChildren()){
                        Product pr = p.getValue(Product.class);
                        pr.setId(p.getKey());
                        products.add(pr);
                    }
                    addCategoryView(d.getKey(),products);
                    Log.d(FIREBASE,"Cat "+d.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void addCategoryView(final String category, ArrayList<Product> products){
        View v = getLayoutInflater().inflate(R.layout.category_item,null,false);
        ((TextView) v.findViewById(R.id.category)).setText(category);
        v.findViewById(R.id.showMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneralActivity.this, CategoryActivity.class);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.list_of_goods_in_categoty);
        final BestProductAdapter bestProductAdapter = new BestProductAdapter("Some category", products, this);
        BestProductAdapter.ListItemClickListener listItemClickListener = new BestProductAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CLICK",category+" "+position);
                Intent intent = new Intent(GeneralActivity.this,ProductActivity.class);
                intent.putExtra("category",category);
                intent.putExtra("product_id",bestProductAdapter.getProducts().get(position).getId());
                intent.putExtra("product_name",bestProductAdapter.getProducts().get(position).getName());
                startActivity(intent);
            }
        };
        bestProductAdapter.setListItemClickListener(listItemClickListener);
        recyclerView.setAdapter(bestProductAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        linearLayout.addView(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.general, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) GeneralActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(GeneralActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_logout){
            mAuth.signOut();
            if (mAuth.getCurrentUser()==null){
                Log.d(LoginActivity.TAG, "No current user");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
        if (id ==  R.id.nav_basket){
            startActivity(new Intent(this,BasketActivity.class));
        }

        if (id ==  R.id.nav_favor){
            startActivity(new Intent(this, FavoritesActivity.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

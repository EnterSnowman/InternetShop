package com.entersnowman.internetshop;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.entersnowman.internetshop.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.entersnowman.internetshop.GeneralActivity.FIREBASE;

public class ProductActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabasePhotos;
    private DatabaseReference mDatabaseBasket;
    private DatabaseReference mDatabaseFavorites;
    private StorageReference mStorageRef;
    private Product currentProduct;
    boolean isInBasket;
    boolean isInFavorite;
    @BindView(R.id.slider) SliderLayout slider;
    @BindView(R.id.reviews_container) TableRow review_container;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_price) TextView productPrice;
    @BindView(R.id.availability) TextView availability;
    @BindView(R.id.descriptionTitle) TextView descriptionTitle;
    @BindView(R.id.product_description) TextView productDescription;
    @BindView(R.id.charTitle) TextView charTitle;
    @BindView(R.id.product_characteristics) TextView productCharacteristics;
    @BindView(R.id.product_rating) RatingBar ratingBar;
    @BindView(R.id.like) ImageView likeBtn;
    @BindView(R.id.in_basket) ImageView basketBtn;
    ArrayList<String> photos_URLs;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        photos_URLs = new ArrayList<>();
        slider.stopAutoCycle();
        mAuth = FirebaseAuth.getInstance();
        mDatabasePhotos = FirebaseDatabase.getInstance().getReference()
                .child("product_photos")
                .child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("product_id"));
        mDatabase  = FirebaseDatabase.getInstance().getReference()
                .child("products")
                .child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("product_id"));
        mDatabaseBasket = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("basket");
        mDatabaseFavorites = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("favorite");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        descriptionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(productDescription, (ImageView) findViewById(R.id.expand_collapse_description));
            }
        });
        charTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(productCharacteristics, (ImageView) findViewById(R.id.expand_collapse_char));
            }
        });
        review_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, ReviewsActivity.class);
                startActivity(intent);
            }
        });
        setTitle(getIntent().getStringExtra("product_name"));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabasePhotos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Log.d(FIREBASE,d.getKey()+d.getValue(String.class));
                    mStorageRef.child("product_photos/"+getIntent().getStringExtra("category")+"/"+getIntent().getStringExtra("product_id")+"/"+d.getKey()+d.getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(FIREBASE,uri.toString());
                            photos_URLs.add(uri.toString());
                            TextSliderView textSliderView = new TextSliderView(ProductActivity.this);
                            textSliderView.image(uri.toString());
                            slider.addSlider(textSliderView);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        productDescription.setVisibility(View.GONE);
        productCharacteristics.setVisibility(View.GONE);
        bindProductInfo();
        basketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInBasket)
                addToBasket();
                else
                removeFromBasket();
            }
        });
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInFavorite)
                    addToFavorites();
                else
                    removeFromFavorites();
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



    public void toggle_contents(View v,ImageView arrow){
        arrow.setImageResource(v.isShown()? R.drawable.ic_expand_small_holo_light : R.drawable.ic_collapse_small_holo_light);
        v.setVisibility( v.isShown()
                ? View.GONE
                : View.VISIBLE );


    }
    public void bindProductInfo(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Product.class);
                productName.setText(currentProduct.getName());
                productPrice.setText(Float.toString(currentProduct.getPrice()));
                if(currentProduct.isAvailable())
                    availability.setText(getString(R.string.available));
                else
                    availability.setText(getString(R.string.unavailable));
                ratingBar.setIsIndicator(true);
                ratingBar.setRating(currentProduct.getRating());
                productDescription.setText(currentProduct.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //filling imageViews
        initImageButtons();
    }
    public void addToBasket(){
        mDatabaseBasket.child(getIntent().getStringExtra("category")+"_"+getIntent().getStringExtra("product_id")).setValue("onBasket").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProductActivity.this,getString(R.string.product_added_to_basket),Toast.LENGTH_SHORT).show();
                basketBtn.setImageResource(R.mipmap.ic_basket_green);
                isInBasket = true;
            }
        });
    }

    public void removeFromBasket(){
        mDatabaseBasket.child(getIntent().getStringExtra("category")+"_"+getIntent().getStringExtra("product_id")).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProductActivity.this,getString(R.string.product_removed_from_basket),Toast.LENGTH_SHORT).show();
                basketBtn.setImageResource(R.mipmap.ic_basket);
                isInBasket = false;
            }
        });
    }

    public void addToFavorites(){
        mDatabaseFavorites.child(getIntent().getStringExtra("category")+"_"+getIntent().getStringExtra("product_id")).setValue("onBasket").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProductActivity.this,getString(R.string.product_added_to_fav),Toast.LENGTH_SHORT).show();
                likeBtn.setImageResource(R.mipmap.ic_heart_green);
                isInFavorite = true;
            }
        });
    }

    public void removeFromFavorites(){
        mDatabaseFavorites.child(getIntent().getStringExtra("category")+"_"+getIntent().getStringExtra("product_id")).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProductActivity.this,getString(R.string.product_removed_from_fav),Toast.LENGTH_SHORT).show();
                likeBtn.setImageResource(R.mipmap.ic_heart);
                isInFavorite = false;
            }
        });
    }

    public void initImageButtons(){
        mDatabaseBasket.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getIntent().getStringExtra("category")+"_"+getIntent().getStringExtra("product_id"))){
                    basketBtn.setImageResource(R.mipmap.ic_basket_green);
                    isInBasket = true;
                }
                else
                    isInBasket = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getIntent().getStringExtra("category")+"_"+getIntent().getStringExtra("product_id"))){
                    likeBtn.setImageResource(R.mipmap.ic_heart_green);
                    isInFavorite = true;
                }
                else
                    isInFavorite = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }
}

package com.entersnowman.internetshop;

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
    private StorageReference mStorageRef;
    private Product currentProduct;
    boolean isInBasket;
    @BindView(R.id.slider) SliderLayout slider;
    @BindView(R.id.product_name) TextView productName;
    @BindView(R.id.product_price) TextView productPrice;
    @BindView(R.id.availability) TextView availability;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    }
    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }
}

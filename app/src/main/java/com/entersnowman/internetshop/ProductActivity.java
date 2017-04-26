package com.entersnowman.internetshop;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private StorageReference mStorageRef;
    @BindView(R.id.slider) SliderLayout slider;
    ArrayList<String> photos_URLs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        photos_URLs = new ArrayList<>();
        slider.stopAutoCycle();
        mDatabasePhotos = FirebaseDatabase.getInstance().getReference()
                .child("product_photos")
                .child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("product_id"));
        mDatabase  = FirebaseDatabase.getInstance().getReference()
                .child("products")
                .child(getIntent().getStringExtra("category"))
                .child(getIntent().getStringExtra("product_id"));
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

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }
}

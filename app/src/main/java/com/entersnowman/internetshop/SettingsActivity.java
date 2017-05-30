package com.entersnowman.internetshop;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.entersnowman.internetshop.GeneralActivity.FIREBASE;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.reset_password)
    Button resetPasswordButton;
    @BindView(R.id.apply_changes)
    Button applyChangesButton;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    boolean isAvatarChanged;
    private StorageReference mStorageRef;
    ByteArrayOutputStream baos;

    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bm = GeneralActivity.getImageBitmap(firebaseAuth.getCurrentUser().getPhotoUrl().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profileImage.setImageBitmap(bm);
                    }
                });
            }
        });
        thread.start();
        isAvatarChanged   = false;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("photos/"+firebaseAuth.getCurrentUser().getUid()+"/photo.jpg");
        baos = new ByteArrayOutputStream();
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.account_setting);
        progressDialog = new ProgressDialog(this);
        editText.setText(firebaseAuth.getCurrentUser().getDisplayName());
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.cancel();
                        Log.d(FIREBASE,firebaseAuth.getCurrentUser().getEmail());
                        Toast.makeText(SettingsActivity.this,R.string.email_sended,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final int[] i = {0};
                if (!editText.getText().toString().equals("")) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                            .build();
                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(LoginActivity.TAG, "User profile updated.");
                                    }
                                    i[0]++;
                                    if (i[0]==2)
                                    progressDialog.cancel();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            i[0]++;
                            if (i[0]==2)
                                progressDialog.cancel();
                        }
                    });

                if (isAvatarChanged) {
                    UploadTask uploadTask = mStorageRef.putBytes(baos.toByteArray());
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(FIREBASE, "Update photo " + taskSnapshot.getDownloadUrl().toString());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(taskSnapshot.getDownloadUrl())
                                    .build();
                            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(LoginActivity.TAG, "User profile updated.");
                                            }
                                            i[0]++;
                                            if (i[0]==2)
                                                progressDialog.cancel();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    i[0]++;
                                    if (i[0]==2)
                                        progressDialog.cancel();
                                }
                            });
                        }
                    });
                }
                else {i[0]++;}
            }
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            isAvatarChanged = true;
            Log.d("IMAGES",picturePath);
            bitmap = BitmapFactory.decodeFile(picturePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            profileImage.setImageBitmap(bitmap);

        }


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

package com.entersnowman.internetshop;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.entersnowman.internetshop.GeneralActivity.FIREBASE;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.reset_password)
    Button resetPasswordButton;
    @BindView(R.id.apply_changes)
    Button applyChangesButton;
    @BindView(R.id.editText)
    EditText editText;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
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
                if (!editText.getText().toString().equals("")){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(editText.getText().toString())
                            .build();
                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(LoginActivity.TAG, "User profile updated.");
                                    }
                                    progressDialog.cancel();
                                }
                            });
                }
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
